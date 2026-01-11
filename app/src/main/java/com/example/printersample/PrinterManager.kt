package com.example.printersample

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.sunmi.printerx.enums.Align
import com.sunmi.printerx.enums.ImageAlgorithm
import com.sunmi.printerx.style.BitmapStyle

class PrinterManager(private val context: Context) {

    fun printBitmap(bitmap: Bitmap) {
        val printer = Constant.selectPrinter
        val lineApi = printer?.lineApi()

        if (lineApi == null) {
            Toast.makeText(context, "PRINTER NOT READY", Toast.LENGTH_SHORT).show()
            return
        }

        lineApi.printBitmap(
            bitmap,
            BitmapStyle.getStyle()
                .setAlign(Align.CENTER)
                .setWidth(384)
                .setAlgorithm(ImageAlgorithm.DITHERING)
        )

        lineApi.autoOut()
    }
}

