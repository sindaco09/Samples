package com.indaco.camera.ui.screens.types

import android.os.Bundle
import android.view.View
import androidx.camera.core.ExperimentalGetImage
import androidx.fragment.app.Fragment
import com.indaco.camera.R
import com.indaco.camera.databinding.FragmentImageToTextCameraBinding
import com.indaco.camera.util.barcode.camerax.CameraPreview
import com.indaco.camera.util.image_to_text.ImageToTextAnalyzer
import com.indaco.camera.util.image_to_text.TextToImageCapture
import com.indaco.samples.util.common.PermissionsUtil
import com.indaco.samples.util.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

@ExperimentalGetImage
class ImageToTextCameraXFragment: Fragment(R.layout.fragment_image_to_text_camera) {

    private val binding by viewBinding(FragmentImageToTextCameraBinding::bind)
    private lateinit var permissionsUtil: PermissionsUtil

    private var textToImageCapture: TextToImageCapture? = null

    private val mainDispatcher = Dispatchers.Main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPermissionUtil()
    }

    override fun onStart() {
        super.onStart()

        initCamera()
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

            val cameraPreview = CameraPreview(requireContext(), ImageToTextAnalyzer())

            textToImageCapture = TextToImageCapture()

            startCameraPreview(cameraPreview)
        } else {
            permissionsUtil.requestPermission()
        }
    }

    private fun startCameraPreview(cameraPreview: CameraPreview) {
        cameraPreview.startCamera(
            viewLifecycleOwner,
            mainDispatcher.asExecutor(),
            binding.preview.surfaceProvider) {
        }
    }
}