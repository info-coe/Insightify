package com.infomerica.insightify.ui.composables.recentOrderRating


sealed class RecentOrderRatingEvent {

    data class SaveAssistantConversationRatingToFirebase(
        val ratingValue: Int
    ) : RecentOrderRatingEvent()

    data object FetchRecentOrdersFromFirebase : RecentOrderRatingEvent()

    data class UpdateItemLikeCount(val itemName : String) : RecentOrderRatingEvent()

    data class UpdateDisLikeCount(val itemName: String) : RecentOrderRatingEvent()
}


data class RecentOrderUiState(
    val isLoading : Boolean = false,
    val orders : List<String>? = null,
    val error : String? = null
)