package com.example.printersample

sealed class UiEvent() {
    object RequestCamera : UiEvent()
    object RequestGallery : UiEvent()
    data class OpenSource(val action: Action) : UiEvent()
}
