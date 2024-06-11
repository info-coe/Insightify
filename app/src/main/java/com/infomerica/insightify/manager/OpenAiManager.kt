package com.infomerica.insightify.manager

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.assistant.Assistant
import com.aallam.openai.api.assistant.AssistantId
import com.aallam.openai.api.assistant.AssistantRequest
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.core.RequestOptions
import com.aallam.openai.api.core.Role
import com.aallam.openai.api.core.Status
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.message.MessageContent
import com.aallam.openai.api.message.messageRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.api.run.RunRequest
import com.aallam.openai.api.thread.Thread
import com.aallam.openai.api.thread.ThreadId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIHost
import com.aallam.openai.client.RetryStrategy
import com.infomerica.insightify.ui.composables.chatbot.generic_assistant.components.AssistantConversationModel
import com.infomerica.insightify.util.Constants
import com.infomerica.insightify.util.Constants.GPT_MODEL
import com.infomerica.insightify.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


class OpenAiManager @Inject constructor() {

    init {
        Timber.tag(OPEN_API_MANAGER)
            .i("created")
    }

    private var apiKey: String = ""

    @OptIn(BetaOpenAI::class)
    private var assistant: Assistant? = null

    @OptIn(BetaOpenAI::class)
    private var thread: Thread? = null
    private val openAI by lazy {
        OpenAI(
            token = apiKey.ifEmpty { "" },
            host = OpenAIHost.OpenAI,
            timeout = Timeout(socket = 120.seconds),
            retry = RetryStrategy()
        )
    }


    private val conversationHistory = mutableListOf<ChatMessage>()

    @OptIn(BetaOpenAI::class)
    suspend fun createThread(): String {
        return withContext(Dispatchers.IO) {
            openAI.thread().let { thread: Thread ->
                return@withContext thread.id.id
            }
        }
    }

    @OptIn(BetaOpenAI::class)
    suspend fun getAssistantAndInitializeThread(
        assistantID: String,
        threadID: String
    ) {
        assistant = openAI.assistant(
            id = AssistantId(assistantID),
            request = AssistantRequest(
                model = ModelId("gpt-4-turbo"),
                instructions = Constants.OPEN_AI_INSTRUCTIONS
            ),
            requestOptions = RequestOptions(
                
            )
        )
        thread = openAI.thread(ThreadId(id = threadID))
        assistant?.let { assistant: Assistant ->
            Timber
                .tag(ASSISTANT_MANAGER)
                .i("assistant Created")
            Timber
                .tag(ASSISTANT_MANAGER)
                .i("Assistant Initialized with ID : ${thread?.id?.id}")
            Timber
                .tag(ASSISTANT_MANAGER)
                .i("assistant Info : $assistant")
        }
    }

    @OptIn(BetaOpenAI::class)
    suspend fun fetchAssistantConversation(): Flow<Resource<List<AssistantConversationModel>>> =
        flow {
            emit(Resource.Loading())
            thread?.let { thread ->
                Timber.tag(ASSISTANT_MANAGER_PREVIOUS_CONVERSATION)
                    .i("Fetching conversation from thread ID: ${thread.id.id}")

                try {
                    val assistantMessages = openAI.messages(thread.id).reversed()

                    if (assistantMessages.isNotEmpty()) {
                        val recentConversations = assistantMessages.flatMap { messages ->
                            messages.content.map {
                                val textValue = (it as? MessageContent.Text)?.text?.value.orEmpty()
                                AssistantConversationModel(
                                    message = textValue,
                                    isFromUser = messages.role == Role.User
                                )
                            }
                        }.onEach {
                            Timber.tag(ASSISTANT_MANAGER_PREVIOUS_CONVERSATION).d(it.toString())
                        }
                        emit(Resource.Success(recentConversations))
                    } else {
                        Timber.tag(ASSISTANT_MANAGER_PREVIOUS_CONVERSATION)
                            .i("Conversations not found")
                        emit(Resource.Error("Conversations not found", emptyList()))
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Error fetching assistant conversation")
                    emit(Resource.Error("Error fetching assistant conversation", emptyList()))
                }
            }
        }


    @OptIn(BetaOpenAI::class)
    suspend fun executeMessage(message: String) = flow {
        var assistantResponse: String? = null
        emit(Resource.Loading())
        assistant?.let { assistant ->
            thread?.let { thread ->
                openAI.message(
                    threadId = thread.id,
                    messageRequest {
                        content = message
                        role = Role.User
                    }
                )
                val runRequest = openAI.createRun(
                    thread.id,
                    request = RunRequest(
                        assistantId = assistant.id,
                        model = assistant.model,
                        instructions = assistant.instructions,
                        metadata = assistant.metadata
                    )
                )
                do {
                    delay(1000)
                    val retrievedRun = openAI.getRun(threadId = thread.id, runId = runRequest.id)
                } while (retrievedRun.status != Status.Completed)
                val assistantMessages = openAI.messages(thread.id)
                for (responseMessages in assistantMessages.reversed()) {
                    assistantResponse =
                        (responseMessages.content.first() as? MessageContent.Text)?.text?.value
                            ?: error("Expected MessageContent.Text")
                }
                assistantResponse.takeIf { it != null }?.let {
                    emit(Resource.Success(it))
                }
            }
        }

    }

    fun updateApiKey(updatedApiKey: String) {
        apiKey = updatedApiKey
        Timber.tag(OPEN_API_MANAGER)
            .i("key updated - $updatedApiKey")
    }

    fun setRules(rules: String) {
        conversationHistory.add(
            ChatMessage(
                role = ChatRole.System,
                content = rules,
            )
        )
        Timber.tag(OPEN_API_MANAGER)
            .i("rules updated - $rules")
    }

    fun getConversationHistory() = conversationHistory

    fun setConversationHistory(recentConversation: MutableList<ChatMessage>) {
        conversationHistory.addAll(recentConversation)
        Timber.tag(OPEN_API_MANAGER)
            .i("conversationHistory updated - $recentConversation")
    }

    suspend fun getResponse(query: String) = flow {
        emit(Resource.Loading())
        conversationHistory.add(
            ChatMessage(
                role = ChatRole.User,
                content = query,
            )
        )
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId(GPT_MODEL),
            messages = conversationHistory,
            maxTokens = 1024,
            temperature = 0.5,
            topP = 0.8
        )

        val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)
        completion.choices[0].message.content?.let { response ->
            if (response.isNotEmpty()) {
                emit(Resource.Success(response))
                conversationHistory.add(
                    ChatMessage(
                        role = ChatRole.Assistant,
                        content = response,
                    )
                )
                Timber.tag(OPEN_API_MANAGER)
                    .i("response  - $response")
            } else {
                emit(Resource.Success(""))
            }
        }
    }.catch { exception ->
        emit(
            Resource.Error(
                message = "Api call failed"
            )
        )
        Timber.tag(OPEN_API_MANAGER)
            .e(exception)
    }
}


private const val OPEN_API_MANAGER = "OpenApiManager"
private const val ASSISTANT_MANAGER = "AssistantManager"
private const val ASSISTANT_MANAGER_PREVIOUS_CONVERSATION = "AssistantManagerPreviousConversation"