package com.example.app_java;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.sunmi.printerx.PrinterSdk;
import com.sunmi.printerx.SdkException;

import java.util.List;

public class PrinterViewModel extends ViewModel {

    public void initPrinter(Context context) {

        try {
            PrinterSdk.getInstance().getPrinter(context, new PrinterSdk.PrinterListen() {

                @Override
                public void onDefPrinter(PrinterSdk.Printer printer) {
                    PrinterHolder.selectPrinter = printer;
                }

                @Override
                public void onPrinters(List<PrinterSdk.Printer> printers) {
                }
            });
        } catch (SdkException e) {
            e.printStackTrace();
        }
    }
}
