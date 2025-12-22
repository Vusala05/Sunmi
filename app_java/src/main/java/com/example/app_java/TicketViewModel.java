package com.example.app_java;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Toast;
import androidx.lifecycle.ViewModel;
import com.sunmi.printerx.api.PrintResult;
import com.sunmi.printerx.enums.Align;
import com.sunmi.printerx.enums.DividingLine;
import com.sunmi.printerx.enums.ErrorLevel;
import com.sunmi.printerx.enums.HumanReadable;
import com.sunmi.printerx.enums.ImageAlgorithm;
import com.sunmi.printerx.style.BarcodeStyle;
import com.sunmi.printerx.style.BaseStyle;
import com.sunmi.printerx.style.BitmapStyle;
import com.sunmi.printerx.style.QrStyle;
import com.sunmi.printerx.style.TextStyle;

public class TicketViewModel extends ViewModel {

    public void printBarcode(String url) {
        if (PrinterHolder.selectPrinter != null) {
            try {
                BarcodeStyle barcodeStyle = BarcodeStyle.getStyle()
                        .setAlign(Align.CENTER)
                        .setDotWidth(4)
                        .setBarHeight(100)
                        .setReadable(HumanReadable.POS_THREE);
                barcodeStyle.setWidth(384);

                PrinterHolder.selectPrinter.lineApi().printBarCode(url, barcodeStyle);
                PrinterHolder.selectPrinter.lineApi().autoOut();
            } catch (com.sunmi.printerx.SdkException e) {
                e.printStackTrace();
            }
        }
    }

    public void qrCode(String url) {
        if (PrinterHolder.selectPrinter != null) {
            try {
                QrStyle qrCodeStyle = QrStyle.getStyle()
                        .setAlign(Align.CENTER)
                        .setDot(11)
                        .setErrorLevel(ErrorLevel.H);

                PrinterHolder.selectPrinter.lineApi().printQrCode(url, qrCodeStyle);
                PrinterHolder.selectPrinter.lineApi().autoOut();
            } catch (com.sunmi.printerx.SdkException e) {
                e.printStackTrace();
            }
        }
    }

    public void printBitmap(View view) {
        if (PrinterHolder.selectPrinter != null) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;

                Bitmap bitmap = BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.sunmi, options);

                PrinterHolder.selectPrinter.lineApi().printBitmap(
                        bitmap,
                        BitmapStyle.getStyle()
                                .setAlign(Align.CENTER)
                                .setAlgorithm(ImageAlgorithm.BINARIZATION)
                                .setValue(130)
                                .setWidth(384)
                                .setHeight(150)
                );

                PrinterHolder.selectPrinter.lineApi().printBitmap(
                        bitmap,
                        BitmapStyle.getStyle()
                                .setAlign(Align.CENTER)
                                .setAlgorithm(ImageAlgorithm.DITHERING)
                                .setWidth(384)
                                .setHeight(150)
                );

                PrinterHolder.selectPrinter.lineApi().autoOut();
            } catch (com.sunmi.printerx.SdkException e) {
                e.printStackTrace();
            }
        }
    }

    public void printTicket(Context context, View view, String url) {
        if (PrinterHolder.selectPrinter != null) {
            try {
                PrinterHolder.selectPrinter.lineApi().enableTransMode(true);

                PrinterHolder.selectPrinter.lineApi().initLine(BaseStyle.getStyle().setAlign(Align.CENTER));

                PrinterHolder.selectPrinter.lineApi().printText("******", TextStyle.getStyle());
                PrinterHolder.selectPrinter.lineApi().printText(
                        "WELCOME TO SUNMI",
                        TextStyle.getStyle().enableBold(true).setTextWidthRatio(1).setTextHeightRatio(1)
                );
                PrinterHolder.selectPrinter.lineApi().printText("******", TextStyle.getStyle());

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                Bitmap bitmap = BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.sunmi, options);

                PrinterHolder.selectPrinter.lineApi().printBitmap(
                        bitmap,
                        BitmapStyle.getStyle()
                                .setAlign(Align.CENTER)
                                .setAlgorithm(ImageAlgorithm.BINARIZATION)
                                .setValue(120)
                                .setWidth(384)
                                .setHeight(150)
                );

                PrinterHolder.selectPrinter.lineApi().printDividingLine(DividingLine.DOTTED, 2);

                PrinterHolder.selectPrinter.lineApi().printText("----Bar Code----", TextStyle.getStyle().setTextSize(48));
                PrinterHolder.selectPrinter.lineApi().printBarCode(
                        url,
                        BarcodeStyle.getStyle()
                                .setAlign(Align.CENTER)
                                .setHeight(100)
                                .setDotWidth(5)
                                .setReadable(HumanReadable.POS_TWO)
                );

                PrinterHolder.selectPrinter.lineApi().printDividingLine(DividingLine.DOTTED, 2);

                PrinterHolder.selectPrinter.lineApi().printText("----QR Code----", TextStyle.getStyle().setTextSize(48));
                PrinterHolder.selectPrinter.lineApi().printQrCode(
                        url,
                        QrStyle.getStyle()
                                .setAlign(Align.CENTER)
                                .setDot(10)
                );

                PrinterHolder.selectPrinter.lineApi().autoOut();

                PrinterHolder.selectPrinter.lineApi().printTrans(new PrintResult() {
                    @Override
                    public void onResult(int resultCode, String message) {
                        try {
                            PrinterHolder.selectPrinter.lineApi().enableTransMode(false);
                        } catch (com.sunmi.printerx.SdkException e) {
                            e.printStackTrace();
                        }
                        if (resultCode == 0) {
                            Toast.makeText(context, "Çap uğurla tamamlandı", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Çap uğursuz oldu: " + message, Toast.LENGTH_LONG).show();
                        }
                    }
                });

            } catch (com.sunmi.printerx.SdkException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Printer tapılmadı", Toast.LENGTH_SHORT).show();
        }
    }

}
