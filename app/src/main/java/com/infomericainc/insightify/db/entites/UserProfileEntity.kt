package com.infomericainc.insightify.db.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserProfileDto

@Entity(
    tableName = UserProfileEntity.TABLE_NAME
)
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = USER_ID) val userID : String = "",
    @ColumnInfo(name = USERNAME) val username : String?,
    @ColumnInfo(name = EMAIL) val email : String?,
    @ColumnInfo(name = PROFILE_URL) val profileUrl : String?,
    @ColumnInfo(name = REGISTERED_ON) val registeredOn : String?,
    ) {
    companion object {
        const val TABLE_NAME = "user_details"
        const val USERNAME = "username"
        const val EMAIL = "email"
        const val USER_ID = "user_id"
        const val REGISTERED_ON = "registered_on"
        const val PROFILE_URL = "profile_url"
    }
}

fun UserProfileEntity.toUserProfileDto() : UserProfileDto {
    return UserProfileDto(
        userId = userID,
        username, profileUrl, email, registeredOn
    )
}