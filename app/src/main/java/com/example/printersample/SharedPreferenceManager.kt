package com.example.printersample

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceManager {
    private const val PERMISSION_PREF = "permission_prefs"
    private lateinit var permissionPref: SharedPreferences

    fun init(context: Context) {
        permissionPref = context.getSharedPreferences(PERMISSION_PREF, Context.MODE_PRIVATE)
    }
    fun wasCameraRequested(): Boolean {
        return permissionPref.getBoolean("camera_requested", false)
    }

    fun markCameraRequested() {
        permissionPref.edit().putBoolean("camera_requested", true)
    }

    fun wasGalleryRequested(): Boolean {
        return permissionPref.getBoolean("gallery_requested", false)
    }

    fun markGalleryRequested() {
        permissionPref.edit().putBoolean("gallery_requested", true)

    }
}