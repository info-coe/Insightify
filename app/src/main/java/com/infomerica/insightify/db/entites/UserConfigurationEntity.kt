package com.infomerica.insightify.db.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infomerica.insightify.ui.composables.login.google_sign_in.UserConfigurationDto

@Entity(
    tableName = UserConfigurationEntity.TABLE_NAME
)
data class UserConfigurationEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = USER_ID) val userID : String = "",
    @ColumnInfo(name = FREE_SESSIONS) val freeSessions : Int? = null,
    @ColumnInfo(name = PREMIUM_USER) val premiumUser : Boolean? = null
    ) {
    companion object {
        const val TABLE_NAME = "user_configuration"
        const val FREE_SESSIONS = "free_sessions"
        const val PREMIUM_USER = "premium_user"
        const val USER_ID = "user_id"
    }
}

fun UserConfigurationEntity.toUserConfigurationDto() : UserConfigurationDto {
    return UserConfigurationDto(
        userId = userID,
        premiumUser = premiumUser,
        freeSessions = freeSessions
    )
}