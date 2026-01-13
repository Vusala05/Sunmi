package com.example.printersample.utils

import android.Manifest
import android.os.Build

object GalleryPermissionUtils {
    fun galleryPermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE
    }
    }