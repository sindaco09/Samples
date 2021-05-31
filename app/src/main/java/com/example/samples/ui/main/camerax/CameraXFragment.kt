package com.example.samples.ui.main.camerax

import android.os.Bundle
import android.view.View
import androidx.camera.core.ExperimentalGetImage
import com.example.samples.R
import com.example.samples.databinding.FragmentCameraBinding
import com.example.samples.ui.base.DataBindingFragment
import com.example.samples.util.camerax.BarcodeCameraPreview
import com.example.samples.util.camerax.PhotoCameraCapture
import com.example.samples.util.camerax.TextToImageCapture
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

    private lateinit var permissionsUtil: PermissionsUtil
    private lateinit var barcodeCameraPreview: BarcodeCameraPreview
//    private lateinit var photoCameraCapture: PhotoCameraCapture
    private lateinit var textToImageCapture: TextToImageCapture

    private val mainDispatcher = Dispatchers.Main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        permissionsUtil = PermissionsUtil(this) { granted ->
            if (granted) {
                startCameraPreview()
            } else {
                permissionsUtil.shouldOpenSettingsDialog { intent ->
                    intent?.let { i -> startActivity(i) }
                }
            }
        }

        barcodeCameraPreview = BarcodeCameraPreview(requireContext())

//        photoCameraCapture = PhotoCameraCapture(requireContext())

        textToImageCapture = TextToImageCapture()

        initCamera()

        initUI()
    }

    private fun initUI() =
        binding.cameraCaptureButton.setOnClickListener {
            textToImageCapture.takePhoto(
                barcodeCameraPreview.imageCapture,
                mainDispatcher.asExecutor()
            )
        }

    private fun initCamera() {
        if (permissionsUtil.allPermissionsGranted()) {
            startCameraPreview()
        } else {
            permissionsUtil.requestPermission()
        }
    }

    private fun startCameraPreview() {
        barcodeCameraPreview.startCamera(
            viewLifecycleOwner,
            mainDispatcher.asExecutor(),
            binding.preview.surfaceProvider)
    }
}