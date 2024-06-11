package com.infomerica.insightify.ui.composables.chatbot.personal_assistant.recentconversation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.infomerica.insightify.db.dao.RecentSessionDao
import com.infomerica.insightify.db.dao.UserProfileDao
import com.infomerica.insightify.db.entites.toUserProfileDto
import com.infomerica.insightify.manager.OpenAiManager
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationModel
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ConversationType
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.DropDownItem
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.MessageOrigin
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.MessageType
import com.infomerica.insightify.ui.composables.chatbot.personal_assistant.ResponseType
import com.infomerica.insightify.ui.composables.login.google_sign_in.UserProfileDto
import com.infomerica.insightify.util.Constants
import com.infomerica.insightify.util.Resource
import com.infomerica.insightify.util.enumValueOfOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


/**
 * ViewModel used for handling Recent conversation.
 */
@HiltViewModel
class RecentConversationViewModel @Inject constructor(
    private val openAiManager: OpenAiManager,
    private val firebaseFireStore: FirebaseFirestore,
    private val userProfileDao: UserProfileDao,
    private val recentSessionDao: RecentSessionDao,
) : ViewModel() {


    init {
        Timber.tag(RECENT_CONVERSATION_VM).d("created")
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is IllegalArgumentException -> {
                Timber.tag(RECENT_CONVERSATION_VM).d("exception caught")
                Timber.e(throwable)
                mutableRecentConversationUIState.update {
                    it.copy(
                        isLoading = false,
                        error = "something went wrong"
                    )
                }
            }
            else -> {
                Timber.tag(RECENT_CONVERSATION_VM).d("exception caught ")
                Timber.e(throwable)
                mutableRecentConversationUIState.update {
                    it.copy(
                        isLoading = false,
                        error = "something went wrong"
                    )
                }
            }
        }
    }

    //Used for response form System
    private val mutableRecentConversationResponseUIState =
        MutableStateFlow(RecentConversationResponseUiState())
    val recentConversationResponseUiState = mutableRecentConversationResponseUIState.asStateFlow()

    //Used to fetch recent conversations form Firebase
    private val mutableRecentConversationUIState = MutableStateFlow(RecentConversationUIState())
    val recentConversationUIState = mutableRecentConversationUIState.asStateFlow()

    //Used to save conversations to Firebase
    private val mutableRecentConversationSavingUIState =
        MutableStateFlow(RecentConversationSavingUiState())
    val recentConversationSavingUIState = mutableRecentConversationSavingUIState.asStateFlow()

    //Used to hold conversation.
    private val mutableConversationUIState = MutableStateFlow<List<ConversationModel>>(listOf())
    val conversationUiState: StateFlow<List<ConversationModel>> =
        mutableConversationUIState.asStateFlow()

    fun onTriggerEvent(event: RecentConversationEvent) {
        when (event) {
            is RecentConversationEvent.ExecuteQuery -> executeQuery(event.query)
            is RecentConversationEvent.SaveConversationToFirebase -> saveRecentSessionConversationToFirebase(
                event.sessionId
            )

            is RecentConversationEvent.FetchRecentConversation -> fetchRecentConversationFromFirebase(
                event.sessionId
            )

            is RecentConversationEvent.AddMessageToConversation -> addMessageToConversation(event.conversationModel)
        }
    }

    private fun addMessageToConversation(conversationModel: ConversationModel) {
        mutableConversationUIState.value = mutableConversationUIState.value + conversationModel
    }

    private fun fetchRecentConversationFromFirebase(sessionId: String) {
        // Log that fetching has started with the given sessionId
        Timber.tag(RECENT_CONVERSATION_VM).d("fetching started with $sessionId")

        // Update UI state to indicate loading
        mutableRecentConversationUIState.update {
            it.copy(isLoading = true)
        }

        // Launch a coroutine in the viewModelScope
        viewModelScope.launch(exceptionHandler) {

            val userProfile = getUserDataFromRoom()

            //fetching from Room.
            userProfile.takeIf { it != null && it.email?.isNotEmpty() == true }
                ?.let { userProfileDto ->
                    val recentSessionFromRoom = recentSessionDao.getRecentSessionData(sessionId)
                        .firstOrNull()
                    recentSessionFromRoom.takeIf { it != null }
                        ?.let { recentSessionEntity ->
                            recentSessionEntity.conversation?.takeIf { it.isNotEmpty() }
                                ?.let { recentConversations ->

                                    userProfileDto.email?.takeIf { it.isNotEmpty() }?.let { email ->
                                        fetchSystemConversationFromFirebase(sessionId, email)
                                    }

                                    mutableRecentConversationUIState.update {
                                        it.copy(
                                            isLoading = false,
                                            recentConversations = recentConversations
                                        )
                                    }
                                    mutableConversationUIState.update {
                                        it + recentConversations
                                    }

                                    Timber.tag(RECENT_CONVERSATION_VM)
                                        .d("Fetched from Room using ID : $sessionId .")
                                }
                        } ?: run {
                        //Failed to get from room so we using Firebase.
                        userProfileDto.email?.takeIf { it.isNotEmpty() }?.let { email ->
                            val documentSnapshot = firebaseFireStore
                                .collection(Constants.RecentSessionPath.USERS.name)
                                .document(email)
                                .collection(Constants.RecentSessionPath.CONVERSATIONS.name)
                                .document(sessionId)
                                .get()
                                .await()

                            fetchSystemConversationFromFirebase(sessionId, email)

                            mapConversationFormFirebase(documentSnapshot)?.run {
                                takeIf { it.isNotEmpty() }?.let { recentConversations ->
                                    saveRecentSessionConversationToRoom(
                                        sessionId, recentConversations
                                    )
                                    mutableRecentConversationUIState.update {
                                        it.copy(
                                            isLoading = false,
                                            recentConversations = recentConversations
                                        )
                                    }
                                    mutableConversationUIState.update {
                                        it + recentConversations
                                    }
                                    Timber.tag(RECENT_CONVERSATION_VM)
                                        .d("Fetched from Firebase using ID : $sessionId .")
                                }
                            } ?: throw IllegalArgumentException()
                        }
                    }?: throw IllegalArgumentException()
                } ?: throw IllegalArgumentException()
        }
    }

    private suspend fun mapConversationFormFirebase(documentSnapshot: DocumentSnapshot): List<ConversationModel>? {
        return withContext(Dispatchers.IO) {
            documentSnapshot.takeIf { it.exists() }?.let { documentSnap ->
                Timber.tag(RECENT_CONVERSATION_VM)
                    .d("Document Exist in firebase.")
                (documentSnap.get("conversation") as? List<Map<String, Any>>)?.let { conversationMaps ->
                    conversationMaps.mapNotNull { map ->
                        try {
                            // Convert map to ConversationModel
                            val dropDownMap =
                                (map["dropDownItem"] as? Map<String, Any>)
                                    ?: emptyMap()

                            // Retrieve various properties from the map
                            val retrievedConversationTypeString =
                                map["conversationType"] as? String ?: ""
                            val retrievedMessageTypeString =
                                map["messageType"] as? String ?: ""
                            val retrievedMessageOriginString =
                                map["messageOrigin"] as? String ?: ""
                            val retrievedResponseTypeString =
                                map["responseType"] as? String ?: ""

                            // Convert retrieved strings to enum types
                            val retrievedConversationType =
                                enumValueOfOrNull<ConversationType>(
                                    retrievedConversationTypeString
                                )
                            val retrievedMessageType =
                                enumValueOfOrNull<MessageType>(
                                    retrievedMessageTypeString
                                )
                            val retrievedMessageOrigin =
                                enumValueOfOrNull<MessageOrigin>(
                                    retrievedMessageOriginString
                                )
                            val retrievedResponseType =
                                enumValueOfOrNull<ResponseType>(
                                    retrievedResponseTypeString
                                )

                            // Create ConversationModel
                            ConversationModel(
                                message = map["message"] as? String ?: "",
                                conversationType = retrievedConversationType
                                    ?: ConversationType.TEXT,
                                messageType = retrievedMessageType
                                    ?: MessageType.STATIC,
                                messageOrigin = retrievedMessageOrigin
                                    ?: MessageOrigin.ASSISTANT,
                                responseType = retrievedResponseType
                                    ?: ResponseType.DEFAULT,
                                dropDownItem = DropDownItem(
                                    question = dropDownMap["question"] as? String
                                        ?: "",
                                    answer = dropDownMap["answer"] as? String
                                        ?: "",
                                    codeBlock = dropDownMap["codeBlock"] as? String
                                        ?: "",
                                    referenceText = dropDownMap["referenceText"] as? String
                                        ?: "",
                                    referenceUrl = dropDownMap["referenceUrl"] as? String
                                        ?: ""
                                )
                            ).also {
                                Timber.tag(RECENT_CONVERSATION_VM)
                                    .d("Document Mapping finished ${it.toString()}")
                            }
                        } catch (e: Exception) {
                            // Log and handle the exception
                            Timber.e(
                                e,
                                "Error converting map to ConversationModel"
                            )
                            throw e
                            null
                        }
                    }
                }
            }
        }
    }

    private suspend fun mapSystemConversationToFirebase(systemConversations: List<ChatMessage>): List<Map<String, Any?>> {
        // Use coroutine to perform asynchronous operations
        return withContext(Dispatchers.IO) {
            // Convert ChatMessage objects to a list of maps suitable for Firestore
            val systemConversationsMap = systemConversations.map { chatMessage ->
                mapOf(
                    "role" to chatMessage.role,
                    "content" to chatMessage.content
                )
            }
            systemConversationsMap
        }
    }
    private suspend fun fetchSystemConversationFromFirebase(sessionId: String, email: String) {
        // Use coroutine to perform asynchronous operations
        withContext(Dispatchers.IO) {
            try {
                // Fetch document snapshot from Firestore
                val documentSnapshot = firebaseFireStore
                    .collection(Constants.UserProfilePath.USERS.name)
                    .document(email)
                    .collection("CONVERSATIONS")
                    .document(sessionId)
                    .get()
                    .await()

                // Retrieve system conversations from the document snapshot
                val systemConversations =
                    documentSnapshot?.get("systemConversation") as? List<Map<String, Any>>

                // Map the system conversations to ChatMessage objects
                val conversations = systemConversations?.map { map ->
                    ChatMessage(
                        role = map["role"] as? ChatRole ?: ChatRole.System,
                        content = map["content"] as? String
                    )
                }

                // Check if conversations is not null and not empty
                conversations?.takeIf { it.isNotEmpty() }?.let { nonEmptySystemConversations ->
                    // Log non-empty system conversations
                    Timber.tag(RECENT_CONVERSATION_VM).i(nonEmptySystemConversations.toString())
                    // Set the conversation history in openAiManager
                    openAiManager.setConversationHistory(nonEmptySystemConversations.toMutableList())
                } ?: run {
                    // Log if systemConversations is null or empty
                    Timber.tag(RECENT_CONVERSATION_VM).i("System conversations is null or empty.")
                    throw IllegalArgumentException()
                    // Handle the case when systemConversations is null or empty
                }
            } catch (e: Exception) {
                // Log and handle the exception
                Timber.e(e, "Error fetching system conversations from Firestore.")
                // Handle the exception
            }
        }
    }


    private fun executeQuery(query: String) {
        viewModelScope.launch {
            openAiManager.getResponse(query)
                .distinctUntilChanged()
                .onEach { queryResponse ->
                    when (queryResponse) {
                        is Resource.Loading -> {
                            mutableRecentConversationResponseUIState.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                        }

                        is Resource.Success -> {
                            mutableRecentConversationResponseUIState.update {
                                it.copy(
                                    isLoading = false,
                                    queryResponse = queryResponse.data.toString()
                                )
                            }
                        }

                        is Resource.Error -> {
                            mutableRecentConversationResponseUIState.update {
                                it.copy(
                                    isLoading = false,
                                    error = queryResponse.message ?: ""
                                )
                            }
                        }
                    }
                }
                .launchIn(this)
        }
    }


    private suspend fun getUserDataFromRoom(): UserProfileDto? {
        return withContext(Dispatchers.IO) {
            userProfileDao.getUserProfile()
                .firstOrNull()?.run {
                    toUserProfileDto()
                }
        }
    }


    private suspend fun saveRecentSessionConversationToRoom(
        sessionId: String,
        updateConversation: List<ConversationModel>
    ): Int {
        return withContext(Dispatchers.IO) {
            recentSessionDao
                .updateRecentSessionField(
                    sessionId,
                    updateConversation
                )
        }
    }

    //TODO: FIx this after testing for now we setting the whole conversation to firebase, Instead we only want to set the recent conversation only.
//    private fun filterConversation(
//        previousConversations: List<ConversationModel>,
//        newConversations: List<ConversationModel>
//    ): List<ConversationModel> {
//        Timber.tag(RECENT_CONVERSATION_VM).d("Filtering started previous conversation : ${previousConversations.size}")
//        Timber.tag(RECENT_CONVERSATION_VM).d("Previous conversation messages: ${previousConversations.map { it.message }}")
//
//        Timber.tag(RECENT_CONVERSATION_VM).d("Filtering started new conversation : ${newConversations.size}")
//        Timber.tag(RECENT_CONVERSATION_VM).d("New conversation messages: ${newConversations.map { it.message }}")
//
//        val filteredList = previousConversations.filterNot { prevConversation ->
//            newConversations.any { newConversation ->
//                calculateLevenshteinDistance(prevConversation.message, newConversation.message) < 3
//            }
//        }
//
//        Timber.tag(RECENT_CONVERSATION_VM).d("Filtering finished final conversation : ${filteredList.size}")
//
//        return filteredList
//    }
//
//    private fun calculateLevenshteinDistance(str1: String, str2: String): Int {
//        return LevenshteinDistance().apply(str1, str2)
//    }

    private fun saveRecentSessionConversationToFirebase(sessionId: String) {
        Timber.tag(RECENT_CONVERSATION_VM).d("saving started")
        mutableRecentConversationSavingUIState.update {
            it.copy(isSaving = true)
        }
        viewModelScope.launch(exceptionHandler) {
            // Fetch user data from Room
            requireNotNull(getUserDataFromRoom()).run {
                Timber.tag(RECENT_CONVERSATION_VM).d("user profile not empty.")
                // Require email for fetching data
                requireNotNull(email).let { email ->
                    Timber.tag(RECENT_CONVERSATION_VM).d("email not empty $email")

                    // Fetch document snapshot from Firebase
                    firebaseFireStore
                        .collection(Constants.RecentSessionPath.USERS.name)
                        .document(email)
                        .collection(Constants.RecentSessionPath.CONVERSATIONS.name)
                        .document(sessionId)
                        .set(
                            mapOf("conversation" to mapConversationToFirebase(mutableConversationUIState.value)),
                            SetOptions.merge()
                        )
                        .await()

                    firebaseFireStore
                        .collection(Constants.RecentSessionPath.USERS.name)
                        .document(email)
                        .collection(Constants.RecentSessionPath.CONVERSATIONS.name)
                        .document(sessionId)
                        .set(
                            mapOf("systemConversation" to mapSystemConversationToFirebase(openAiManager.getConversationHistory())),
                            SetOptions.merge()
                        )
                        .await()
                    Timber.tag(RECENT_CONVERSATION_VM).d("recent session entitiy saving completed")

                    mutableRecentConversationSavingUIState.update {
                        it.copy(isSaving = false, saveCompleted = true)
                    }
                }
            }
        }
    }

    private suspend fun mapConversationToFirebase(conversationList: List<ConversationModel>): List<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            conversationList.map { conversationModel ->
                try {
                    // Convert ConversationModel to map
                    val dropDownItemMap = mapOf(
                        "question" to conversationModel.dropDownItem.question,
                        "answer" to conversationModel.dropDownItem.answer,
                        "referenceText" to conversationModel.dropDownItem.referenceText,
                        "referenceUrl" to conversationModel.dropDownItem.referenceUrl
                    )

                    // Convert enum types to strings
                    val conversationTypeString = conversationModel.conversationType.name
                    val messageTypeString = conversationModel.messageType.name
                    val messageOriginString = conversationModel.messageOrigin.name
                    val responseTypeString = conversationModel.responseType.name

                    // Create map for Firestore
                    mapOf(
                        "message" to conversationModel.message,
                        "conversationType" to conversationTypeString,
                        "messageType" to messageTypeString,
                        "messageOrigin" to messageOriginString,
                        "responseType" to responseTypeString,
                        "dropDownItem" to dropDownItemMap
                    ).also {
                        Timber.tag(RECENT_CONVERSATION_VM)
                            .d("Conversion to Firestore map finished ${it.toString()}")
                    }
                } catch (e: Exception) {
                    // Log and handle the exception
                    Timber.e(e, "Error converting ConversationModel to map")
                    throw e
                }
            }
        }
    }


//    private fun saveRecentSessionConversationToFirebase(sessionId: String) {
//        Timber.tag(RECENT_CONVERSATION_VM).d("saving started")
//        mutableConversationSavingUIState.update {
//            it.copy(isSaving = true)
//        }
//        viewModelScope.launch(exceptionHandler) {
//            // Fetch user data from Room
//            requireNotNull(getUserDataFromRoom()).run {
//                Timber.tag(RECENT_CONVERSATION_VM).d("user profile not empty.")
//                // Require email for fetching data
//                requireNotNull(email).let { email ->
//                    Timber.tag(RECENT_CONVERSATION_VM).d("email not empty $email")
//                    val recentConversation = recentSessionDao
//                        .getRecentSessionData(sessionId)
//                        .firstOrNull()
//                    recentConversation.takeIf { it != null }?.let { recentSessionEntity ->
//                        Timber.tag(RECENT_CONVERSATION_VM).d("recent session entitiy not null")
//                        recentSessionEntity.conversation.takeIf { it.isNullOrEmpty().not() }
//                            ?.let { previousConversation ->
//                                Timber.tag(RECENT_CONVERSATION_VM).d("recent session entitiy conversation not null")
//                               filterConversation(previousConversation,mutableConversationUIState.value).takeIf { it.isNotEmpty() }?.let { filteredConversation ->
//
//                                   // Convert ConversationModel to Map for Firestore
//                                   val filteredConversationMapList = filteredConversation.map {
//                                       mapOf(
//                                           "message" to it.message,
//                                           "conversationType" to it.conversationType.name,
//                                           "messageType" to it.messageType.name,
//                                           "messageOrigin" to it.messageOrigin.name,
//                                           "responseType" to it.responseType.name,
//                                           "dropDownItem" to mapOf(
//                                               "question" to it.dropDownItem.question,
//                                               "answer" to it.dropDownItem.answer,
//                                               "referenceText" to it.dropDownItem.referenceText,
//                                               "referenceUrl" to it.dropDownItem.referenceUrl
//                                           )
//                                       )
//                                   }
//
//                                   // Fetch document snapshot from Firebase
//                                   firebaseFireStore
//                                       .collection(Constants.RecentSessionPath.USERS.name)
//                                       .document(email)
//                                       .collection(Constants.RecentSessionPath.CONVERSATIONS.name)
//                                       .document(sessionId)
//                                       .update("conversation",FieldValue.arrayUnion(filteredConversationMapList))
//                                       .await()
//                                   Timber.tag(RECENT_CONVERSATION_VM).d("recent session entitiy saving completed")
//
//                                   mutableConversationSavingUIState.update {
//                                       it.copy(isSaving = false,saveCompleted = true)
//                                   }
//                               } ?: run {
//                                   mutableConversationSavingUIState.update {
//                                       it.copy(isSaving = false,saveCompleted = true)
//                                   }
//                               }
//                            }
//                    } ?: run {
//                        Timber.tag(RECENT_CONVERSATION_VM).d("recent_session_empty")
//                    }
//                }
//            }
//        }
//    }
}

private const val RECENT_CONVERSATION_VM = "RecentConversation VM"