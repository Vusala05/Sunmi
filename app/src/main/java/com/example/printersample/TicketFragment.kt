package com.example.printersample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.printersample.databinding.FragmentTicketBinding

class TicketFragment : Fragment(R.layout.fragment_ticket) {

    private var _binding: FragmentTicketBinding? = null
    private val binding get() = _binding!!

    private val printerViewModel: PrinterViewModel by viewModels()
    private val ticketViewModel: TicketViewModel by viewModels()

    private var url: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTicketBinding.bind(view)

        printerViewModel.initPrinter(requireContext())

        binding.qrPrint.setOnClickListener {
            url = binding.url.text.toString().trim()
            if (!url.isNullOrBlank()) {
                ticketViewModel.qrCode(url!!)
            } else {
                showError()
            }
        }

        binding.barcodePrint.setOnClickListener {
            url = binding.url.text.toString().trim()
            if (!url.isNullOrBlank()) {
                ticketViewModel.printBarcode(url!!)
            } else {
                showError()
            }
        }

        binding.printReceipt.setOnClickListener {
            url = binding.url.text.toString().trim()
            if (!url.isNullOrBlank()) {
                ticketViewModel.printTicket(
                    requireContext(),
                    requireView(),
                    url!!
                )
            } else {
                showError()
            }
        }
        binding.logoPrint.setOnClickListener {
            binding.url.clearFocus()
            ticketViewModel.printBitmap(requireView())
        }
    }

    private fun showError() {
        Toast.makeText(requireContext(), "URL can't be empty, please fill it", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
