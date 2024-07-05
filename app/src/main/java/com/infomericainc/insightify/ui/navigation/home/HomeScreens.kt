package com.infomericainc.insightify.ui.navigation.home

sealed class HomeScreens(val route : String) {
    data object HomeScreen : HomeScreens("home_screen")
    data object GenericAssistantScreen : HomeScreens("generic_assistant_screen")
    data object RecentOrderScreen : HomeScreens("recent_order_screen")
    data object PaymentScreen : HomeScreens("payment_screen")
    data object TransactionScreen : HomeScreens("transaction_screen")
    data object RecentOrderReviewScreen : HomeScreens("recent_order_review_screen/{orders}") {
        fun navigateWithOrders(orders : Array<String>) : String {
            return "recent_order_review_screen/$orders"
        }
    }

    data object AboutUsScreenSpec : HomeScreens("about_us_screen")
}
