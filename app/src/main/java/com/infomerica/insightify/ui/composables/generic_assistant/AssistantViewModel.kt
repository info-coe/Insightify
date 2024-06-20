package com.infomerica.insightify.ui.composables.generic_assistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.infomerica.insightify.db.dao.UserProfileDao
import com.infomerica.insightify.db.entites.UserProfileEntity
import com.infomerica.insightify.manager.OpenAiManager
import com.infomerica.insightify.manager.PaymentManager
import com.infomerica.insightify.ui.composables.generic_assistant.order.Orders
import com.infomerica.insightify.ui.composables.generic_assistant.order.RecentOrderDto
import com.infomerica.insightify.util.Constants
import com.infomerica.insightify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val openAiManager: OpenAiManager,
    private val fireStore: FirebaseFirestore,
    private val firebaseDatabase: DatabaseReference,
    private val userProfileDao: UserProfileDao,
    private val paymentManager: PaymentManager,
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

    }

    init {
        Timber
            .tag(ASSISTANT_VIEW_MODEL)
            .i("Created")
    }

    override fun onCleared() {
        super.onCleared()
        Timber
            .tag(ASSISTANT_VIEW_MODEL)
            .i("Destroyed")
    }

    private val mutableAssistantResponseUiState =
        MutableStateFlow(AssistantResponseUiState())
    val assistantResponseUiState = mutableAssistantResponseUiState.asStateFlow()

    private val mutablePreviousConversationUiState =
        MutableStateFlow(PreviousConversationUiState())
    val previousConversationUiState = mutablePreviousConversationUiState.asStateFlow()

    private val mutableConversationUIState =
        MutableStateFlow<List<AssistantConversationModel>>(listOf())
    val conversationUiState: StateFlow<List<AssistantConversationModel>> =
        mutableConversationUIState.asStateFlow()

    private val mutableConversationDeletionUiState =
        MutableStateFlow(ConversationDeletionUiState())
    val conversationDeletionUiState = mutableConversationDeletionUiState.asStateFlow()

    fun onEvent(event: AssistantEvent) {
        when (event) {
            is AssistantEvent.GetPreviousConversationFromAssistant -> fetchUserThreadFirebase()
            is AssistantEvent.GetResponseFromAssistant -> executeMessage(event.message)
            is AssistantEvent.AddMessageToConversation -> addMessageToConversation(event.conversationModel)
            is AssistantEvent.AddOrdersToFirebase -> addRecentOrdersToRoom(event.orders)
            is AssistantEvent.DeleteConversationFromAssistant -> deleteThreadFormFirebase()
        }
    }

    private fun addMessageToConversation(conversationModel: AssistantConversationModel) {
        Timber
            .tag(ASSISTANT_VIEW_MODEL)
            .d("conversation model added : $conversationModel ")
        mutableConversationUIState.value += conversationModel
    }


    private fun addRecentOrdersToRoom(orders: Orders?) {
        viewModelScope.launch {
            val userProfile = getUserProfile()
            val customerName: String = userProfile?.username ?: ""
            val orderID: String = generateOrderID(orders, userProfile)
            val beverages = orders?.OrderData?.Drinks?.map { mapOf(it.name to it.price) }
            val soups = orders?.OrderData?.Soups?.map { mapOf(it.name to it.price) }
            val appetizer = orders?.OrderData?.Appetizers?.map { mapOf(it.name to it.price) }
            val main = orders?.OrderData?.Main?.map { mapOf(it.name to it.price) }
            val desert = orders?.OrderData?.Desert?.map { mapOf(it.name to it.price) }
            val orderStatus = "PENDING"
            val paymentStatus = "PENDING"
            val orderAcceptedTime: Timestamp? = null
            val orderFinishedTime: Timestamp? = null
            val rejectReason: String? = null
            val taxes: Double? = orders?.OrderData?.Total?.GST
            val totalAmount: Double = orders?.OrderData?.Total?.GrandTotal ?: 0.0
            val amount: Double = orders?.OrderData?.Total?.Amount ?: 0.0
            val recentOrderDto = RecentOrderDto(
                orderID = orderID,
                amount = mapOf(
                    "Dollars" to amount
                ),
                taxes = taxes,
                totalAmount = totalAmount,
                customerName = customerName,
                orderTime = Timestamp.now(),
                orders = mapOf(
                    "beverages" to beverages,
                    "soups" to soups,
                    "appetizers" to appetizer,
                    "main" to main,
                    "desert" to desert,
                ),
                tableNumber = 7,
                orderStatus = orderStatus,
                paymentStatus = paymentStatus,
                orderAcceptedTime = orderAcceptedTime,
                orderFinishedTime = orderFinishedTime,
                orderRejectReason = rejectReason
            )
            fireStore
                .collection("ORDERS")
                .document()
                .set(recentOrderDto)
                .await()
        }
    }


    private suspend fun generateOrderID(
        orders: Orders?,
        userProfileEntity: UserProfileEntity?
    ): String {
        return withContext(Dispatchers.IO) {
            val userId = userProfileEntity?.userID?.substring(0, 4)
            val username = userProfileEntity?.username?.substring(0, 4)
            val randomId = Random.nextInt(from = 100, until = 1000)
            val billAmount = orders?.OrderData?.Total?.GrandTotal?.toInt()
            (userId + username + randomId + billAmount)
        }
    }

    private suspend fun getUserProfile(): UserProfileEntity? {
        return userProfileDao
            .getUserProfile()
            .firstOrNull()
            .also {
                Timber
                    .tag(ASSISTANT_VIEW_MODEL)
                    .e("User Email from Room : ${it?.email}")
            }
    }

    private suspend fun createThread(): String {
        return openAiManager
            .createThread()
    }

    private suspend fun updateUserThreadToFirebase(conversationID: String) {
        withContext(Dispatchers.IO) {
            getUserProfile()?.let { userProfileEntity ->
                fireStore
                    .collection(Constants.UserProfilePath.USERS.name)
                    .document(userProfileEntity.email ?: "")
                    .collection(Constants.UserProfilePath.USER_DATA.name)
                    .document(userProfileEntity.userID)
                    .set(mapOf(Constants.CONVESATION_ID to conversationID), SetOptions.merge())
                    .await()
                Timber
                    .tag(ASSISTANT_VIEW_MODEL)
                    .e("Conversation Id Created : $conversationID and updated to Firebase.")
            }
        }
    }


    private suspend fun getAssistantIDFromFirebase(): String? {
        return withContext(Dispatchers.IO) {
            firebaseDatabase
                .child(Constants.APP_DATA)
                .child(Constants.ASSISTANT_ID)
                .get()
                .await()
                .getValue(String::class.java)
                .also {
                    Timber
                        .tag(ASSISTANT_VIEW_MODEL)
                        .i("Assistant ID Fetched from firebase : $it")
                }
        }
    }

    /**
     * Suspend function used to configure
     * ThreadId for OpenAI.
     */
    private fun fetchUserThreadFirebase() {
        mutablePreviousConversationUiState
            .update {
                it.copy(
                    isLoading = true
                )
            }
        viewModelScope.launch(exceptionHandler) {
            getUserProfile()?.let { userProfileEntity ->
                fireStore
                    .collection(Constants.UserProfilePath.USERS.name)
                    .document(userProfileEntity.email ?: "")
                    .collection(Constants.UserProfilePath.USER_DATA.name)
                    .document(userProfileEntity.userID)
                    .get()
                    .await()
                    .get(Constants.CONVESATION_ID, String::class.java).let { conversationID ->
                        if (conversationID.isNullOrEmpty()) {
                            Timber
                                .tag(ASSISTANT_VIEW_MODEL)
                                .e("Conversation Id not found creating one.")
                            createThread().run {
                                updateUserThreadToFirebase(this)
                                addToThreadPool(
                                    userProfileEntity,
                                    this
                                )
                                getAssistantIDFromFirebase()?.let { assistantID ->
                                    openAiManager
                                        .getAssistantAndInitializeThread(
                                            assistantID = assistantID,
                                            threadID = this
                                        )
                                    getPreviousConversationFromAssistant()
                                }
                            }
                        } else {
                            Timber
                                .tag(ASSISTANT_VIEW_MODEL)
                                .e("Conversation Id found $conversationID")
                            getAssistantIDFromFirebase()?.let { assistantID ->
                                openAiManager
                                    .getAssistantAndInitializeThread(
                                        assistantID = assistantID,
                                        threadID = conversationID
                                    )
                                getPreviousConversationFromAssistant()
                            }
                        }
                    }
            }
        }
    }

    private fun getPreviousConversationFromAssistant() {
        viewModelScope.launch {
            openAiManager
                .fetchAssistantConversation()
                .onEach { previousConversation ->
                    when (previousConversation) {
                        is Resource.Loading -> {
                            Timber
                                .tag(ASSISTANT_VIEW_MODEL)
                                .i("Previous conversation UI State : Loading")
                        }

                        is Resource.Success -> {
                            Timber
                                .tag(ASSISTANT_VIEW_MODEL)
                                .i("Previous conversation UI State : success")
                            mutablePreviousConversationUiState
                                .update {
                                    it.copy(
                                        isLoading = false,
                                        previousConversation = previousConversation.data as List<AssistantConversationModel>
                                    )
                                }
                            mutableConversationUIState.update {
                                it + previousConversation.data as List<AssistantConversationModel>
                            }
                        }

                        is Resource.Error -> {
                            Timber
                                .tag(ASSISTANT_VIEW_MODEL)
                                .i("Previous conversation UI State : Error")
                            mutablePreviousConversationUiState
                                .update {
                                    it.copy(
                                        isLoading = false,
                                        emptyConversation = true,
                                    )
                                }
                        }
                    }
                }.launchIn(this + exceptionHandler)
        }
    }

    private fun executeMessage(message: String) {
        viewModelScope.launch(exceptionHandler) {
            openAiManager
                .executeMessage(message)
                .onEach { assistantResponse ->
                    when (assistantResponse) {
                        is Resource.Loading -> {
                            Timber
                                .tag(ASSISTANT_VIEW_MODEL)
                                .i("Assistant Response UI State : Loading")
                            mutableAssistantResponseUiState
                                .update {
                                    it.copy(
                                        isLoading = true
                                    )
                                }
                        }

                        is Resource.Success -> {
                            Timber
                                .tag(ASSISTANT_VIEW_MODEL)
                                .i("Assistant Response UI State : Success - ${assistantResponse.data}")
                            mutableAssistantResponseUiState
                                .update {
                                    it.copy(
                                        isLoading = false,
                                        assistantResponse = assistantResponse.data
                                    )
                                }
                        }

                        is Resource.Error -> {
                            mutableAssistantResponseUiState
                                .update {
                                    it.copy(
                                        isLoading = false,
                                        error = assistantResponse.message
                                    )
                                }
                        }
                    }
                }.launchIn(this)
        }
    }

    private fun deleteThreadFormFirebase() {
        mutableConversationDeletionUiState
            .update {
                it.copy(
                    isDeleting = true
                )
            }
        viewModelScope.launch(exceptionHandler) {
            val userProfileEntity =
                getUserProfile()
                    ?: return@launch updateDeleteThreadErrorUiState("Unable to get user Data")
            val email = userProfileEntity.email
                ?: return@launch updateDeleteThreadErrorUiState("Email is empty")
            val userID = userProfileEntity.userID
            val conversationID =
                getConversationId(email, userID)
                    ?: return@launch updateDeleteThreadErrorUiState("Unable to get conversationID")

            conversationID
                .let { id ->
                    if (updateThreadPool(userProfileEntity, id)) {
                        delay(1000L)
                        clearConversation(email, userID)
                        delay(1000L)
                        mutableConversationDeletionUiState
                            .update {
                                it.copy(
                                    isDeleting = false,
                                    deleted = true
                                )
                            }
                    } else {
                        return@launch updateDeleteThreadErrorUiState("Unable to update thread")
                    }
                }
        }
    }

    private suspend fun getConversationId(email: String, userID: String): String? {
        return fireStore
            .collection("USERS")
            .document(email)
            .collection("USER_DATA")
            .document(userID)
            .get()
            .await()
            .getString("conversationID")
    }

    private suspend fun clearConversation(email: String, userID: String) {
        fireStore
            .collection("USERS")
            .document(email)
            .collection("USER_DATA")
            .document(userID)
            .update(
                "conversationID", ""
            )
            .await()
    }

    private fun updateDeleteThreadErrorUiState(
        error: String
    ) {
        mutableConversationDeletionUiState
            .update {
                it.copy(
                    isDeleting = false,
                    error = error
                )
            }
    }


    private suspend fun updateThreadPool(
        userProfileEntity: UserProfileEntity?,
        threadId: String?
    ): Boolean = withContext(Dispatchers.IO) {
        fireStore
            .collection("USERS")
            .document(userProfileEntity?.email ?: "")
            .collection("THREAD_POOL")
            .document(threadId!!)
            .set(
                mapOf(
                    "currentlyActive" to false,
                    "lastUsed" to Timestamp.now()
                )
            )
            .await()
        true
    }

    private suspend fun addToThreadPool(
        userProfileEntity: UserProfileEntity?,
        threadId: String?
    ): Boolean = withContext(Dispatchers.IO) {
        fireStore
            .collection("USERS")
            .document(userProfileEntity?.email ?: "")
            .collection("THREAD_POOL")
            .document(threadId!!)
            .set(
                mapOf(
                    "currentlyActive" to true,
                    "lastUsed" to Timestamp.now()
                )
            )
            .await()
        true
    }


}

private const val ASSISTANT_VIEW_MODEL = "AssistantViewModel"