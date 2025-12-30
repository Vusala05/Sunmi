package com.example.printersample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoViewModel : ViewModel() {

    private val _uiEvent = MutableLiveData<UiEvent>()
    val uiEvent: LiveData<UiEvent> = _uiEvent

    fun onCameraClicked() {
        _uiEvent.value = UiEvent.RequestCamera
    }

    fun onGalleryClicked() {
        _uiEvent.value = UiEvent.RequestGallery
    }

    fun onPermissionGranted(action: Action) {
        _uiEvent.value = UiEvent.OpenSource(action)
    }
}
