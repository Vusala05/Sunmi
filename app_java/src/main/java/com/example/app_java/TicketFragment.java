package com.example.app_java;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_java.databinding.FragmentTicketBinding;


public class TicketFragment extends Fragment {

    private FragmentTicketBinding binding;
    private PrinterViewModel printerViewModel;
    private TicketViewModel ticketViewModel;
    private String url;

    public TicketFragment() {
        super(R.layout.fragment_ticket);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentTicketBinding.bind(view);

        printerViewModel = new ViewModelProvider(this).get(PrinterViewModel.class);
        ticketViewModel = new ViewModelProvider(this).get(TicketViewModel.class);

        printerViewModel.initPrinter(requireContext());

        binding.qrPrint.setOnClickListener(this::setOnClick);

        binding.barcodePrint.setOnClickListener(this::setOnClick);
        binding.printReceipt.setOnClickListener(this::setOnClick);

        binding.logoPrint.setOnClickListener(this::setOnClick);
         
    }

    @NonNull
     private String getSafeUrl() throws  NullPointerException {
         url = binding.url.getText().toString().trim();
         if (url.isEmpty()  || url.isBlank()) throw new NullPointerException();

         return  url;


     }

    void setOnClick(View view){
            try {
                int tag = Integer.parseInt(view.getTag().toString());
            String url = getSafeUrl();
            switch (tag) {
                case 0:
                    Log.e("test", "test");
                    break;
                case 1: {
                    binding.url.clearFocus();
                    ticketViewModel.printBitmap(requireView());
                    break;
                }
                case 2:
                    ticketViewModel.qrCode(url);
                    break;
                case 3:
                    ticketViewModel.printBarcode(url);
                    break;
                case 4:
                    ticketViewModel.printTicket(requireContext(), requireView(), url);
                    break;
            }

            } catch (Exception e) {
               Log.e("Error",e.getLocalizedMessage());
            }

    }



    private void showError(String message) {
        Toast.makeText(requireContext(), message!=null? message:"URL can't be empty, please fill it", Toast.LENGTH_SHORT).show();
    }

    private void showError() {
        showError(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}