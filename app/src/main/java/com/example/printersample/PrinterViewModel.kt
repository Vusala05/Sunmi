package com.example.printersample

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunmi.printerx.PrinterSdk
import com.sunmi.printerx.enums.PrinterInfo
import com.sunmi.printerx.enums.PrinterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

var selectPrinter: PrinterSdk.Printer? = null

class PrinterViewModel : ViewModel() {

    fun initPrinter(context: Context) {
        PrinterSdk.getInstance().getPrinter(context, object : PrinterSdk.PrinterListen {

            override fun onDefPrinter(printer: PrinterSdk.Printer?) {
                selectPrinter = printer
            }

            override fun onPrinters(printers: MutableList<PrinterSdk.Printer>?) {
            }
        })
    }


}
