package com.example.printersample.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.printersample.utils.Constant
import com.sunmi.printerx.PrinterSdk

class PrinterViewModel : ViewModel() {

    fun initPrinter(context: Context) {
        PrinterSdk.getInstance().getPrinter(context, object : PrinterSdk.PrinterListen {

            override fun onDefPrinter(printer: PrinterSdk.Printer?) {
                Constant.selectPrinter = printer
            }

            override fun onPrinters(printers: MutableList<PrinterSdk.Printer>?) {
            }
        })
    }


}