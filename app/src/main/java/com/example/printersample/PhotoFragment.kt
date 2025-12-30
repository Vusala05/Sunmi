package com.example.printersample

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.printersample.databinding.FragmentPhotoBinding
import com.google.android.material.snackbar.Snackbar
import com.sunmi.printerx.enums.Align
import com.sunmi.printerx.enums.ImageAlgorithm
import com.sunmi.printerx.style.BitmapStyle

class PhotoFragment : Fragment(R.layout.fragment_photo) {

    private lateinit var binding: FragmentPhotoBinding
    private val printerViewModel: PrinterViewModel by viewModels()



    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->

            if (granted) {
                when (Constant.pendingAction) {
                    Action.CAMERA -> openCamera()
                    Action.GALLERY -> openGallery()
                    null -> Toast.makeText(requireContext(),"Invalid permission", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }





    private val cameraAndGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val bitmap = when (Constant.pendingAction) {
                    Action.CAMERA -> result.data?.extras?.get("data") as? Bitmap
                    Action.GALLERY -> {
                        val intentFromResult = result.data
                        intentFromResult?.let {
                            val uri = intentFromResult.data
                            uri?.let {
                                if (Build.VERSION.SDK_INT >= 28) {
                                    val source = ImageDecoder.createSource(
                                        requireContext().contentResolver,
                                        uri)
                                    ImageDecoder.decodeBitmap(source)
                                }
                                else{
                                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver,uri)
                                }
                            }
                        }

                    }
                    else -> null
                }

                bitmap?.let {
                    val prepared = prepareBitmapForPrinter(it)
                    printBitmap(prepared)
                }
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPhotoBinding.bind(view)
        printerViewModel.initPrinter(requireContext())

        binding.withCamera.setOnClickListener {
            Constant.pendingAction = Action.CAMERA
            checkCameraPermission()
        }

        binding.withGallery.setOnClickListener {
            Constant.pendingAction = Action.GALLERY
            checkGalleryPermission()
        }
    }
    private fun getGalleryPermission() : String{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            Manifest.permission.READ_MEDIA_IMAGES
        } else{
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }


    private fun checkGalleryPermission() {
       val permission = getGalleryPermission()
            when {
                ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                    openGallery()
                }

                shouldShowRequestPermissionRationale(permission) -> {
                    Snackbar.make(binding.root, "Gallery permission required", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Allow") {
                            permissionLauncher.launch(permission)
                        }.show()
                }

                !SharedPreferenceManager.wasGalleryRequested() -> {
                    SharedPreferenceManager.markGalleryRequested()
                    permissionLauncher.launch(permission)
                }

                else -> {
                    Snackbar.make(binding.root, "Permission permanently denied. Enable it from Settings.", Snackbar.LENGTH_INDEFINITE).setAction("Settings") {
                        openAppSettings()
                    }.show()
                }
            }
        }




    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Snackbar.make(binding.root, "Camera permission is required to take photo", Snackbar.LENGTH_INDEFINITE).setAction("Allow") {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }.show()
            }

            !SharedPreferenceManager.wasCameraRequested() -> {
                SharedPreferenceManager.markCameraRequested()
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }

            else -> {
                Snackbar.make(binding.root, "Permission permanently denied. Enable it from Settings.", Snackbar.LENGTH_INDEFINITE).setAction("Settings") {
                    openAppSettings()
                }.show()
            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", requireContext().packageName, null))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraAndGalleryLauncher.launch(intent)
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        cameraAndGalleryLauncher.launch(intent)
    }

    private fun prepareBitmapForPrinter(bitmap: Bitmap): Bitmap {
        val printerWidth = 384
        val height = bitmap.height * printerWidth / bitmap.width

        val scaled = Bitmap.createScaledBitmap(bitmap, printerWidth, height, true)
        val result = Bitmap.createBitmap(
            scaled.width,
            scaled.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(result)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(scaled, 0f, 0f, null)

        return result
    }

    private fun printBitmap(bitmap: Bitmap) {
        val printer = Constant.selectPrinter
        val lineApi = printer?.lineApi()

        if (lineApi != null) {
            lineApi.printBitmap(bitmap, BitmapStyle.getStyle().setAlign(Align.CENTER).setWidth(384).setAlgorithm(ImageAlgorithm.DITHERING))
            lineApi.autoOut()
            Toast.makeText(requireContext(), "PRINT SUCCESS", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "PRINTER NOT READY", Toast.LENGTH_SHORT).show()
        }
    }
}
