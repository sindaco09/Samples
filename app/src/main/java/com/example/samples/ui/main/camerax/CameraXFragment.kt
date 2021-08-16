package com.example.samples.ui.main.camerax

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.ExperimentalGetImage
import androidx.fragment.app.viewModels
import com.example.samples.R
import com.example.samples.databinding.FragmentCameraBinding
import com.example.samples.ui.base.DataBindingFragment
import com.example.samples.util.camerax.barcode.CameraPreview
import com.example.samples.util.camerax.barcode.QRCodeAnalyzer
import com.example.samples.util.camerax.image_to_text.ImageToTextAnalyzer
import com.example.samples.util.camerax.image_to_text.TextToImageCapture
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
class CameraXFragment : DataBindingFragment<FragmentCameraBinding>(R.layout.fragment_camera) {

    companion object {
        private const val TAG = "CameraXBasic"
    }

    private val cameraViewModel: CameraViewModel by viewModels()

    private enum class Mode {BARCODE, IMAGE_TO_TEXT}

    private lateinit var permissionsUtil: PermissionsUtil
    private lateinit var cameraPreview: CameraPreview
    private lateinit var qrCodeAnalyzer: QRCodeAnalyzer

    private var textToImageCapture: TextToImageCapture? = null

    private var mode: Mode = Mode.BARCODE
    private val mainDispatcher = Dispatchers.Main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        initPermissionUtil()

        launchModePicker()

        initUI()
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

    private fun launchModePicker() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.camera_mode_title))
            .setItems(Mode.values().map { it.name }.toTypedArray()) { _, i ->
                mode = Mode.values()[i]
                binding.cameraCaptureButton.alpha = if (mode == Mode.BARCODE) 0.5F else 1.0F
                initCamera()
            }
            .show()
    }

    private fun initUI() {
        binding.cameraCaptureButton.setOnClickListener {
            if (mode == Mode.IMAGE_TO_TEXT) {
                textToImageCapture?.takePhoto(
                    cameraPreview. imageCapture,
                    mainDispatcher.asExecutor()
                )
            } else {
                Toast.makeText(requireContext(), getString(R.string.capture_disabled), Toast.LENGTH_SHORT).show()
            }
        }

        binding.changeMode.setOnClickListener { launchModePicker() }
    }

    private fun initCamera() {
        if (permissionsUtil.allPermissionsGranted()) {

            when(mode) {
                Mode.BARCODE -> {
                    qrCodeAnalyzer = QRCodeAnalyzer { barcode: Barcode ->
                        cameraViewModel.processQRCode(barcode.rawValue).observe(viewLifecycleOwner) { result: String? ->
                            postProcessCode(result)
                            qrCodeAnalyzer.resume()
                        }
                    }

                    cameraPreview = CameraPreview(requireContext(), qrCodeAnalyzer)
                }
                Mode.IMAGE_TO_TEXT -> {
                    cameraPreview = CameraPreview(requireContext(), ImageToTextAnalyzer())

                    textToImageCapture = TextToImageCapture()

                }
            }

            startCameraPreview()
        } else {
            permissionsUtil.requestPermission()
        }
    }

    private fun postProcessCode(result: String?) {
        val message = if (result == null) {
            "null code received..."
        } else {
            "QR code is: $result"
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun startCameraPreview() {
        cameraPreview.startCamera(
            viewLifecycleOwner,
            mainDispatcher.asExecutor(),
            binding.preview.surfaceProvider)
    }
}