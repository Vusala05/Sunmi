package com.example.printersample

import android.os.Bundle
import android.util.Log
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
        binding.qrPrint.setOnClickListener(::setOnClick)
        binding.barcodePrint.setOnClickListener(::setOnClick)
        binding.printReceipt.setOnClickListener(::setOnClick)
        binding.logoPrint.setOnClickListener(::setOnClick)
    }

    private fun getSafeUrl(): String? {
        url = binding.url.text.toString().trim()
            if (!url.isNullOrBlank()) {
               return  url;
            }
        showError()
        return null
    }

    private  fun setOnClick(view: View){
        val tag: Int = view.tag.toString().toInt()
        val url = getSafeUrl()
        if(url ==null) return
        when(tag){
            0 -> Log.e("test","invalid button")
            1-> {
                binding.url.clearFocus()
                ticketViewModel.printBitmap(view)
            }
            2->  ticketViewModel.qrCode(url)
            3-> ticketViewModel.printBarcode(url)
            4-> ticketViewModel.printTicket(requireContext(),view,url)

        }
    }

    private fun showError(message: String? =null){
        Toast.makeText(requireContext(), message?:"URL can't be empty, please fill it", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
