package com.example.samples.ui.main.camerax

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.ExperimentalGetImage
import com.example.samples.R
import com.example.samples.databinding.FragmentCameraBinding
import com.example.samples.ui.base.DataBindingFragment
import com.example.samples.util.camerax.PhotoCameraCapture
import com.example.samples.util.camerax.barcode.CameraPreview
import com.example.samples.util.camerax.barcode.QRCodeAnalyzer
import com.example.samples.util.camerax.image_to_text.ImageToTextAnalyzer
import com.example.samples.util.camerax.image_to_text.TextToImageCapture
import com.example.samples.util.common.PermissionsUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

/*
 * This class is meant to explore Camerax library combined with google vision classes
 * like QR code scanner
 */
@ExperimentalGetImage
class CameraXFragment : DataBindingFragment<FragmentCameraBinding>(R.layout.fragment_camera) {

    companion object {
        private const val TAG = "CameraXBasic"
    }

    private enum class Mode {BARCODE, IMAGE_TO_TEXT}

    private lateinit var permissionsUtil: PermissionsUtil
    private lateinit var cameraPreview: CameraPreview

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
                    cameraPreview.imageCapture,
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
                    val qrCodeAnalyzer = QRCodeAnalyzer {
                        // do something with barcode
                        Log.d(TAG,"barcode: $it")
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

    private fun startCameraPreview() {
        cameraPreview.startCamera(
            viewLifecycleOwner,
            mainDispatcher.asExecutor(),
            binding.preview.surfaceProvider)
    }
}