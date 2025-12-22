package com.example.app_java;
import android.content.Context;
import android.os.Bundle;
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

        binding.qrPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = binding.url.getText().toString().trim();
                if (url != null && !url.isEmpty()) {
                    ticketViewModel.qrCode(url);
                } else {
                    showError();
                }
            }
        });

        binding.barcodePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = binding.url.getText().toString().trim();
                if (url != null && !url.isEmpty()) {
                    ticketViewModel.printBarcode(url);
                } else {
                    showError();
                }
            }
        });

        binding.printReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = binding.url.getText().toString().trim();
                if (url != null && !url.isEmpty()) {
                    ticketViewModel.printTicket(requireContext(), requireView(), url);
                } else {
                    showError();
                }
            }
        });

        binding.logoPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.url.clearFocus();
                ticketViewModel.printBitmap(requireView());
            }
        });
    }

    private void showError() {
        Toast.makeText(requireContext(), "URL can't be empty, please fill it", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}