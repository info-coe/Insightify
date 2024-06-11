package com.infomerica.insightify.ui.activites


sealed class InsightifyEvent {

    data object FetchSystemKeys : InsightifyEvent()


}