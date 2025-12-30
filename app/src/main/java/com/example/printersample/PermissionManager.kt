package com.example.printersample

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class PermissionManager(private val context: Context) {

    fun getGalleryPermission(): String =
        if (Build.VERSION.SDK_INT >= 33) {
            Manifest.permission.READ_MEDIA_IMAGES
        }
        else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    fun isGranted(permission: String): Boolean = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}
