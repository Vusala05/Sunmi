package com.example.printersample.viewModel

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.printersample.R
import com.example.printersample.utils.Constant
import com.sunmi.printerx.api.PrintResult
import com.sunmi.printerx.enums.Align
import com.sunmi.printerx.enums.DividingLine
import com.sunmi.printerx.enums.ErrorLevel
import com.sunmi.printerx.enums.HumanReadable
import com.sunmi.printerx.enums.ImageAlgorithm
import com.sunmi.printerx.style.BarcodeStyle
import com.sunmi.printerx.style.BaseStyle
import com.sunmi.printerx.style.BitmapStyle
import com.sunmi.printerx.style.QrStyle
import com.sunmi.printerx.style.TextStyle

class TicketViewModel : ViewModel() {

    fun printBarcode(url: String) {

        Constant.selectPrinter?.lineApi()?.run {
            val barcodeStyle = BarcodeStyle.getStyle()
                .setAlign(Align.CENTER)
                .setDotWidth(2)
                .setBarHeight(50)
                .setReadable(HumanReadable.POS_THREE)
            barcodeStyle.setWidth(284)
            printBarCode(url, barcodeStyle)
            autoOut()
        }
    }

    fun qrCode(url: String) {
        Constant.selectPrinter?.lineApi()?.run {
            val qrCodeStyle = QrStyle.getStyle()
                .setAlign(Align.CENTER)
                .setDot(9)
                .setErrorLevel(ErrorLevel.H)
            printQrCode(url, qrCodeStyle)
            autoOut()
        }
    }

    fun printBitmap(view: View) {
        Constant.selectPrinter?.lineApi()?.run {
            val option: BitmapFactory.Options = BitmapFactory.Options().apply {
                inScaled = false
            }
            val bitmap =
                BitmapFactory.decodeResource(view.context.resources, R.drawable.sunmi, option)
            printBitmap(bitmap,
                BitmapStyle.getStyle().setAlign(Align.CENTER)
                    .setAlgorithm(ImageAlgorithm.BINARIZATION).setValue(130).setWidth(384)
                    .setHeight(150)
            )
            printBitmap(bitmap,
                BitmapStyle.getStyle().setAlign(Align.CENTER).setAlgorithm(ImageAlgorithm.DITHERING)
                    .setWidth(384).setHeight(150)
            )
            autoOut()
        }
    }

    fun printTicket(context: Context, view: View, url: String) {
        Constant.selectPrinter?.lineApi()?.run {
            enableTransMode(true)

            initLine(BaseStyle.getStyle().setAlign(Align.CENTER))

            printText("******", TextStyle.getStyle())
            printText(
                "WELCOME TO SUNMI",
                TextStyle.getStyle().enableBold(true).setTextWidthRatio(1).setTextHeightRatio(1)
            )
            printText("******", TextStyle.getStyle())

            val options = BitmapFactory.Options().apply {
                inScaled = false
            }

            val bitmap =
                BitmapFactory.decodeResource(view.context.resources, R.drawable.sunmi, options)

            printBitmap(
                bitmap,
                BitmapStyle.getStyle().setAlign(Align.CENTER)
                    .setAlgorithm(ImageAlgorithm.BINARIZATION).setValue(120).setWidth(384)
                    .setHeight(150)
            )
            printDividingLine(DividingLine.EMPTY, 10)

            printDividingLine(DividingLine.DOTTED, 2)

            printText("----Bar Code----", TextStyle.getStyle().setTextSize(48))
            printBarCode(
                url,
                BarcodeStyle.getStyle()
                    .setAlign(Align.CENTER)
                    .setHeight(100)
                    .setDotWidth(5)
                    .setReadable(HumanReadable.HIDE)
            )

            printDividingLine(DividingLine.EMPTY, 10)

            printText("----QR Code----", TextStyle.getStyle().setTextSize(48))
            printQrCode(
                url,
                QrStyle.getStyle()
                    .setAlign(Align.CENTER)
                    .setDot(10)
            )

            autoOut()

            printTrans(object : PrintResult() {
                override fun onResult(resultCode: Int, message: String?) {
                    enableTransMode(false)

                    (context as? Activity)?.runOnUiThread {
                        if (resultCode == 0) {
                            Toast.makeText(context, "PRINT SUCCESSFULLY COMPLETED)", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "PRINT FAILED: $message", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            })


        }
    }
}