package com.infomericainc.insightify.db.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infomericainc.insightify.ui.composables.login.google_sign_in.UserMetaDataDto

@Entity(
    tableName = UserMetaDataEntity.TABLE_NAME
)
data class UserMetaDataEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = ID) val id : Int = 0,
    @ColumnInfo(name = USER_PROFILE_HASH) val userProfileHash : String? = null,
    @ColumnInfo(name = USER_CONFIGURATION_HASH) val userConfigurationHash : String? = null,
) {
    companion object {
        const val TABLE_NAME = "user_meta_data"
        const val ID = "id"
        const val USER_PROFILE_HASH = "user_profile_hash"
        const val USER_CONFIGURATION_HASH = "user_configuration_hash"
    }
}

fun UserMetaDataEntity.toUserMetaDataDto() : UserMetaDataDto {
    return UserMetaDataDto(
        userProfileHash = userProfileHash,
        userConfigurationHash = userConfigurationHash,
    )
}