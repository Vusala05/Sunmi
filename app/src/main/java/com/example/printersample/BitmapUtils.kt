package com.example.printersample

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color

object BitmapUtils {

    fun prepareForPrinter(bitmap: Bitmap, printerWidth: Int = 384): Bitmap {
        val softwareBitmap = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O &&
            bitmap.config == Bitmap.Config.HARDWARE) {
            bitmap.copy(Bitmap.Config.ARGB_8888, false)
        } else {
            bitmap
        }
        val height = (softwareBitmap.height.toFloat() * printerWidth / softwareBitmap.width).toInt()
        val scaled = Bitmap.createScaledBitmap(softwareBitmap, printerWidth, height, true)

        val result = Bitmap.createBitmap(scaled.width, scaled.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(result)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(scaled, 0f, 0f, null)

        return result
    }
}

