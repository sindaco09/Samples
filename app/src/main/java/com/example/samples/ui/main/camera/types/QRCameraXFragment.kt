package com.example.samples.ui.main.camera.types

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.ExperimentalGetImage
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.samples.R
import com.example.samples.databinding.FragmentQrCameraxBinding
import com.example.samples.ui.base.DataBindingFragment
import com.example.samples.ui.main.camera.CameraViewModel
import com.example.samples.util.camera.barcode.camerax.CameraPreview
import com.example.samples.util.camera.barcode.camerax.QRCodeAnalyzer
import com.example.samples.util.common.PermissionsUtil
import com.google.mlkit.vision.barcode.Barcode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

/*
 * This class is meant to explore Camerax library combined with google vision classes
 * like QR code scanner
 */
@AndroidEntryPoint
@ExperimentalGetImage
class QRCameraXFragment : DataBindingFragment<FragmentQrCameraxBinding>(R.layout.fragment_qr_camerax) {

    private val cameraViewModel: CameraViewModel by viewModels()

    private lateinit var permissionsUtil: PermissionsUtil
    private var qrCodeAnalyzer: QRCodeAnalyzer? = null

    private val mainDispatcher = Dispatchers.Main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        initPermissionUtil()

        initCamera()

        initUI()
    }

    private fun initUI() {
        binding.noCameraIcon.setOnClickListener { displayNoCameraAvailable() }
    }

    private fun initPermissionUtil() {
        permissionsUtil = PermissionsUtil(this, PermissionsUtil.Type.CAMERAX) { granted ->
            if (granted) {
                initCamera()
            } else {
                permissionsUtil.shouldOpenSettingsDialog { intent ->
                    intent?.let { i -> startActivity(i) }
                }
            }
        }
    }

    private fun initCamera() {
        if (permissionsUtil.allPermissionsGranted()) {

            qrCodeAnalyzer = QRCodeAnalyzer { barcode: Barcode ->
                cameraViewModel.processQRCode(barcode.rawValue).observe(viewLifecycleOwner, Observer(::postProcessCode))
            }

            startCameraPreview(CameraPreview(requireContext(), qrCodeAnalyzer))
        } else {
            permissionsUtil.requestPermission()
        }
    }

    private fun startCameraPreview(cameraPreview: CameraPreview) {
        cameraPreview.startCamera(
            viewLifecycleOwner,
            mainDispatcher.asExecutor(),
            binding.preview.surfaceProvider) { errorCode ->
            if (errorCode == CameraPreview.NO_CAMERA_CODE) {
                displayNoCameraAvailable()
            }
        }
    }

    private fun displayNoCameraAvailable() {
        binding.noCameraIcon.visibility = View.VISIBLE
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage("No camera available")
            .setPositiveButton("OK",null)
            .show()
    }

    private fun postProcessCode(result: String?) {
        Toast.makeText(requireContext(),
            if (result == null) "null code received..." else "QR code is: $result",
            Toast.LENGTH_SHORT)
            .show()

        // Important to let QR Code Analyzer to resume scanning images for QR Codes
        qrCodeAnalyzer?.resume()
    }
}