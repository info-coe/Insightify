package com.infomerica.insightify.ui.navigation.home

sealed class HomeScreens(val route : String) {
    data object HomeScreen : HomeScreens("home_screen")
    data object ConversationScreen : HomeScreens("conversation_screen")
    data object RecentConversationScreen : HomeScreens("recent_conversation_screen")
    data object GenericAssistantScreen : HomeScreens("generic_assistant_screen")
    data object RecentConversationListScreen : HomeScreens("recent_conversation_list_screens")
    data object RecentOrderScreen : HomeScreens("recent_order_screen")
    data object RecentOrderReviewScreen : HomeScreens("recent_order_review_screen/{orders}") {
        fun navigateWithOrders(orders : Array<String>) : String {
            return "recent_order_review_screen/$orders"
        }
    }

    data object AboutUsScreenSpec : HomeScreens("about_us_screen")
}
