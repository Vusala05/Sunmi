package com.example.printersample

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore

object BitmapResultHelper {
    fun getBitmapFromResult(context: Context, resultIntent: Intent?, action: Action?): Bitmap? {
        return when (action) {
            Action.CAMERA -> resultIntent?.extras?.get("data") as? Bitmap

            Action.GALLERY -> {
                val uri = resultIntent?.data ?: return null

                if (Build.VERSION.SDK_INT >= 28) {
                    val source =
                        ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        uri
                    )
                }
            }

            else -> null
        }
    }
}