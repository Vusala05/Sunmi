package com.example.printersample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.printersample.databinding.FragmentPhotoBinding

class PhotoFragment : Fragment(R.layout.fragment_photo) {
    private lateinit var binding: FragmentPhotoBinding
    private val printerViewModel: PrinterViewModel by viewModels()
    private lateinit var printerManager: PrinterManager

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraAndGalleryLauncher: ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPhotoBinding.bind(view)

        printerViewModel.initPrinter(requireContext())
        printerManager = PrinterManager(requireContext())

        setupLaunchers()
        setupListeners()
    }
    private fun setupListeners() {
        binding.withCamera.setOnClickListener {
            handleCameraAndGalleryAction(Action.CAMERA, Manifest.permission.CAMERA, "Give an permission to access Camera)")
        }
        binding.withGallery.setOnClickListener {
            val permission = GalleryPermissionUtils.galleryPermission()
            handleCameraAndGalleryAction(Action.GALLERY, permission, "Give an permission to access Gallery)")
        }
    }

    private fun handleCameraAndGalleryAction(action: Action, permission: String, rationaleMsg: String) {
        Constant.pendingAction = action
        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
               executePendingAction()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                PermissionUiHelper.showRationale(binding.root, permissionLauncher, permission, rationaleMsg)
            }
            else -> permissionLauncher.launch(permission)
        }
    }

    private fun setupLaunchers() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                executePendingAction()
            } else {
                handlePermissionDenied()
            }
        }

        cameraAndGalleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult

                val bitmap = BitmapResultHelper.getBitmapFromResult(
                    context = requireContext(),
                    resultIntent = result.data,
                    action = Constant.pendingAction
                )

                bitmap?.let {
                    val preparedBitmap = BitmapUtils.prepareForPrinter(it)
                    printerManager.printBitmap(preparedBitmap)
                }
            }
    }
    private fun executePendingAction() {
        when (Constant.pendingAction) {
            Action.CAMERA -> openCamera()
            Action.GALLERY -> openGallery()
            else -> Toast.makeText(requireContext(), "Xəta baş verdi", Toast.LENGTH_SHORT).show()
        }
    }




    private fun handlePermissionDenied() {
        val action = Constant.pendingAction
        val permission = if (action == Action.CAMERA) {
            Manifest.permission.CAMERA
        } else GalleryPermissionUtils.galleryPermission()

        if (!shouldShowRequestPermissionRationale(permission)) {
            PermissionUiHelper.showPermanentlyDenied(binding.root, ::openAppSettings)
        } else {
            Toast.makeText(requireContext(), "İcazə rədd edildi", Toast.LENGTH_SHORT).show()
        }
    }



    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraAndGalleryLauncher.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        cameraAndGalleryLauncher.launch(intent)
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", requireContext().packageName, null))
        startActivity(intent)
    }
}

