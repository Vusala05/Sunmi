package com.example.printersample

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color

object BitmapUtils {

    fun prepareForPrinter(bitmap: Bitmap): Bitmap {
        val printerWidth = 384
        val height = bitmap.height * printerWidth / bitmap.width

        val scaled = Bitmap.createScaledBitmap(bitmap, printerWidth, height, true)
        val result = Bitmap.createBitmap(
            scaled.width,
            scaled.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(result)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(scaled, 0f, 0f, null)

        return result
    }
}
