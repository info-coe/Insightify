package com.infomericainc.insightify.manager

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

class PermissionManager {
    companion object {

        private val currentSdk = Build.VERSION.SDK_INT

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        val permissions = if(currentSdk >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            listOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            listOf(
                Manifest.permission.RECORD_AUDIO
            )
        }

        /**
         * This function used to handle the outcome of the
         * Runtime permissions this function takes
         */
        @OptIn(ExperimentalPermissionsApi::class)
        fun handlePermission(
            permissionState: PermissionState,
            onSuccess: () -> Unit,
            shouldShowRationale: () -> Unit,
            onRejected: () -> Unit
        ) {
            when {
                permissionState.hasPermission -> {
                    onSuccess()
                }

                permissionState.shouldShowRationale -> {
                    shouldShowRationale()
                }

                !permissionState.hasPermission && !permissionState.shouldShowRationale -> {
                    onRejected()
                }
            }
        }
    }
}