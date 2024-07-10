package com.infomericainc.insightify.util

class Exceptions {
}

sealed class HomeException(override val message : String) : Exception() {
    class UserProfileNotFoundInRoomException(override val message: String) : HomeException(message)
    class DocumentNotFoundException(override val message: String) : HomeException(message)
}