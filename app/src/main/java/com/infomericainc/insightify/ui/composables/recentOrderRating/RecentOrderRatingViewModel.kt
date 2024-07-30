package com.infomericainc.insightify.ui.composables.recentOrderRating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.infomericainc.insightify.ui.composables.genericassistant.order.RecentOrderDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RecentOrderRatingViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseCrashlytics: FirebaseCrashlytics
) : ViewModel() {


    private val mutableRecentOrdersUiState =
        MutableStateFlow(RecentOrderUiState())
    val recentOrderUiState = mutableRecentOrdersUiState.asStateFlow()

    fun onTriggerEvent(event: RecentOrderRatingEvent) {
        when (event) {
            is RecentOrderRatingEvent.SaveAssistantConversationRatingToFirebase -> saveAssistantConversationRatingToFirebase(
                event.ratingValue
            )

            is RecentOrderRatingEvent.FetchRecentOrdersFromFirebase -> fetchRecentOrders()
            is RecentOrderRatingEvent.UpdateItemLikeCount -> updateLikeCount(event.itemName)
            is RecentOrderRatingEvent.UpdateDisLikeCount -> updateDisLikeCount(event.itemName)
        }
    }


    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        firebaseCrashlytics
            .recordException(throwable)
    }

    private fun saveAssistantConversationRatingToFirebase(rating: Int) {
        viewModelScope.launch(exceptionHandler) {
            val ratingPath = fireStore
                .collection("RATINGS")
                .document("assistantRating")
                .get()
                .await()

            val totalRatings = ratingPath["rating", Int::class.java]
            val totalUsers = ratingPath["total_rating", Int::class.java]

            totalRatings.takeIf { it != null }?.let { totalRating ->
                val finalRating = totalRating + rating
                totalUsers.takeIf { it != null && it != 0 }?.let { totalUser ->
                    val finalTotalUsers = totalUser + 1
                    val finalRatePercent = finalRating / totalUser
                    fireStore
                        .collection("RATINGS")
                        .document("assistantRating")
                        .update("rating", finalRating)
                        .await()
                    fireStore
                        .collection("RATINGS")
                        .document("assistantRating")
                        .update("total_rating", finalTotalUsers)
                        .await()
                    fireStore
                        .collection("RATINGS")
                        .document("assistantRating")
                        .update("final_rate_percent", finalRatePercent.toDouble())
                        .await()
                }
            }
        }
    }

    private fun fetchRecentOrders() {
        viewModelScope.launch(exceptionHandler) {
            mutableRecentOrdersUiState
                .update {
                    it.copy(
                        isLoading = true
                    )
                }
            val allOrders = mutableListOf<String>()
            fireStore
                .collection("ORDERS")
                .whereEqualTo("tableNumber", 7)
                .orderBy("orderTime", Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    if (value == null) {
                        return@addSnapshotListener
                    }
                    val recentOrder = value.documents[0].toObject(RecentOrderDto::class.java)
                    recentOrder?.orders?.entries?.forEach {
                        it.value?.forEach {
                            it?.entries?.forEach {
                                allOrders
                                    .add(it.key)
                            }
                        }
                        if (allOrders.isEmpty()) {
                            mutableRecentOrdersUiState
                                .update {
                                    it.copy(
                                        isLoading = false,
                                        error = "Unable to get your orders."
                                    )
                                }
                        } else {
                            mutableRecentOrdersUiState
                                .update {
                                    it.copy(
                                        isLoading = false,
                                        orders = allOrders
                                    )
                                }
                        }
                    }
                }
        }
    }

    private fun updateLikeCount(itemName: String) {
        var currentLikes: Int?
        var currentTotalResponses: Int?

        viewModelScope.launch(exceptionHandler) {
            currentLikes = fireStore
                .collection("RATINGS")
                .document("Item Ratings")
                .collection(itemName.capitalize(Locale.ENGLISH))
                .document("responses")
                .get()
                .await()
                .get("likes", Int::class.java)

            currentTotalResponses = fireStore
                .collection("RATINGS")
                .document("Item Ratings")
                .collection(itemName.capitalize(Locale.ENGLISH))
                .document("responses")
                .get()
                .await()
                .get("total_responses", Int::class.java)

            if ((currentLikes == null) or (currentTotalResponses == null)) {
                val initialItemRating = mapOf(
                    "dislike_percent" to 0,
                    "dislike" to 0,
                    "like_percent" to 100,
                    "likes" to 1,
                    "total_responses" to 1
                )

                fireStore
                    .collection("RATINGS")
                    .document("Item Ratings")
                    .collection(itemName.capitalize(Locale.ENGLISH))
                    .document("responses")
                    .set(initialItemRating)
                    .await()
            } else {
                val finalCurrentLikes = (currentLikes ?: return@launch) + 1
                val finalTotalReactions = (currentTotalResponses ?: return@launch) + 1
                val finalLikePercent = (finalCurrentLikes.toDouble() / finalTotalReactions) * 100
                val finalDisLikePercent = 100 - finalLikePercent

                fireStore
                    .collection("RATINGS")
                    .document("Item Ratings")
                    .collection(itemName.capitalize(Locale.ENGLISH))
                    .document("responses")
                    .update("likes", finalCurrentLikes)
                    .await()
                fireStore
                    .collection("RATINGS")
                    .document("Item Ratings")
                    .collection(itemName.capitalize(Locale.ENGLISH))
                    .document("responses")
                    .update("total_responses", finalTotalReactions)
                    .await()
                fireStore
                    .collection("RATINGS")
                    .document("Item Ratings")
                    .collection(itemName.capitalize(Locale.ENGLISH))
                    .document("responses")
                    .update("like_percent", finalLikePercent)
                    .await()
                fireStore
                    .collection("RATINGS")
                    .document("Item Ratings")
                    .collection(itemName.capitalize(Locale.ENGLISH))
                    .document("responses")
                    .update("dislike_percent", finalDisLikePercent)
                    .await()
            }
        }
    }

    private fun updateDisLikeCount(itemName: String) {
        var currentDisLikes: Int?
        var currentTotalResponses: Int?

        viewModelScope.launch(exceptionHandler) {
            currentDisLikes = fireStore
                .collection("RATINGS")
                .document("Item Ratings")
                .collection(itemName.capitalize(Locale.ENGLISH))
                .document("responses")
                .get()
                .await()
                .get("dislikes", Int::class.java)

            currentTotalResponses = fireStore
                .collection("RATINGS")
                .document("Item Ratings")
                .collection(itemName.capitalize(Locale.ENGLISH))
                .document("responses")
                .get()
                .await()
                .get("total_responses", Int::class.java)

            if ((currentDisLikes == null) or (currentTotalResponses == null)) {
                val initialItemRating = mapOf(
                    "dislike_percent" to 100,
                    "dislikes" to 1,
                    "like_percent" to 0,
                    "likes" to 0,
                    "total_responses" to 1
                )

                fireStore
                    .collection("RATINGS")
                    .document("Item Ratings")
                    .collection(itemName.capitalize(Locale.ENGLISH))
                    .document("responses")
                    .set(initialItemRating)
                    .await()
            } else {
                val finalCurrentDisLikes = (currentDisLikes ?: return@launch) + 1
                val finalTotalReactions = (currentTotalResponses ?: return@launch) + 1
                val finalDisLikePercent =
                    ((finalCurrentDisLikes.toDouble() / finalTotalReactions) * 100)
                val finalLikePercent = 100 - finalDisLikePercent
                Timber
                    .tag("DISLIKE_PERCENT")
                    .d(finalDisLikePercent.toString())

                fireStore
                    .collection("RATINGS")
                    .document("Item Ratings")
                    .collection(itemName.capitalize(Locale.ENGLISH))
                    .document("responses")
                    .update("dislikes", finalCurrentDisLikes)
                    .await()
                fireStore
                    .collection("RATINGS")
                    .document("Item Ratings")
                    .collection(itemName.capitalize(Locale.ENGLISH))
                    .document("responses")
                    .update("total_responses", finalTotalReactions)
                    .await()
                fireStore
                    .collection("RATINGS")
                    .document("Item Ratings")
                    .collection(itemName.capitalize(Locale.ENGLISH))
                    .document("responses")
                    .update("dislike_percent", finalDisLikePercent)
                    .await()
                fireStore
                    .collection("RATINGS")
                    .document("Item Ratings")
                    .collection(itemName.capitalize(Locale.ENGLISH))
                    .document("responses")
                    .update("like_percent", finalLikePercent)
                    .await()
            }
        }
    }
}