package com.indaco.samples.ui.main.camera.types

import android.os.Bundle
import android.view.View
import androidx.camera.core.ExperimentalGetImage
import com.indaco.samples.R
import com.indaco.samples.databinding.FragmentImageToTextCameraBinding
import com.indaco.samples.ui.base.DataBindingFragment
import com.indaco.samples.util.camera.barcode.camerax.CameraPreview
import com.indaco.samples.util.camera.image_to_text.ImageToTextAnalyzer
import com.indaco.samples.util.camera.image_to_text.TextToImageCapture
import com.indaco.samples.util.common.PermissionsUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

@ExperimentalGetImage
class ImageToTextCameraXFragment: DataBindingFragment<FragmentImageToTextCameraBinding>(R.layout.fragment_image_to_text_camera) {

    companion object {
        private const val TAG = "CameraXBasic"
    }

    private lateinit var permissionsUtil: PermissionsUtil
    private lateinit var cameraPreview: CameraPreview

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