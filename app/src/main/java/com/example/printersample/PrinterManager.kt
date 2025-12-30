package com.example.printersample

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.sunmi.printerx.enums.Align
import com.sunmi.printerx.enums.ImageAlgorithm
import com.sunmi.printerx.style.BitmapStyle

class PrinterManager {

    fun print(bitmap: Bitmap, context: Context) {
        val printer = Constant.selectPrinter
        val lineApi = printer?.lineApi()

        if (lineApi != null) {
            lineApi.printBitmap(
                bitmap,
                BitmapStyle.getStyle()
                    .setAlign(Align.CENTER)
                    .setWidth(384)
                    .setAlgorithm(ImageAlgorithm.DITHERING)
            )
            lineApi.autoOut()
        } else {
            Toast.makeText(context, "PRINTER NOT READY", Toast.LENGTH_SHORT).show()
        }
    }
}
