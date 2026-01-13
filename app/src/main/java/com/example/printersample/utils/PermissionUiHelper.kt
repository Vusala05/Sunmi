package com.example.printersample.utils

import android.view.View
import androidx.activity.result.ActivityResultLauncher
import com.google.android.material.snackbar.Snackbar

object PermissionUiHelper {
    fun showRationale(rootView: View, permissionLauncher: ActivityResultLauncher<String>, permission: String, message: String = "Permission required") {
        Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)
            .setAction("Allow") {
                permissionLauncher.launch(permission)
            }.show()
    }

    fun showPermanentlyDenied(rootView: View, openSettings: () -> Unit, message: String = "Permission permanently denied. Enable it from Settings)") {
        Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)
            .setAction("Settings") {
                openSettings()
            }.show()
    }
}