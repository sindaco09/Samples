package com.example.samples.ui.main.camerax

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.samples.R
import com.example.samples.databinding.FragmentCameraBinding
import com.example.samples.ui.base.DataBindingFragment
import com.example.samples.util.camerax.QRCodeAnalyzer
import com.example.samples.util.common.PermissionsUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/*
 * This class is meant to explore Camerax library combined with google vision classes
 * like QR code scanner
 */
class CameraXFragment : DataBindingFragment<FragmentCameraBinding>(R.layout.fragment_camera) {

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var permissionsUtil: PermissionsUtil

    private val mainDispatcher = Dispatchers.Main

    private var imageCapture: ImageCapture? = null

    private val permissionRequestLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        Log.d("TAG","permissionGranted: $it")
        if (it) {
            startCamera()
        } else {
            permissionsUtil.shouldOpenSettingsDialog { intent ->
                intent?.let { i -> startActivity(i) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        permissionsUtil = PermissionsUtil(requireContext())

        initCamera()

        initUI()

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun initUI() {
        binding.cameraCaptureButton.setOnClickListener { takePhoto() }
    }

    private fun initCamera() {
        if (permissionsUtil.allPermissionsGranted()) {
            startCamera()
        } else {
            permissionRequestLauncher.launch(PermissionsUtil.REQUIRED_CAMERA_PERMISSIONS.first())
        }
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions, mainDispatcher.asExecutor(), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            })
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({

            // Preview
            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(binding.preview.surfaceProvider) }

            imageCapture = ImageCapture.Builder().build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QRCodeAnalyzer { barcode ->
                        Log.d(TAG, "QRCodeAnalyzer: ${barcode.url}")
                    })
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProviderFuture.get().apply {
                try {
                    // Unbind use cases before rebinding
                    unbindAll()

                    // Bind use cases to camera
                    bindToLifecycle(
                        viewLifecycleOwner, cameraSelector, preview, imageCapture, imageAnalyzer)

                } catch (exc: Exception) {
                    Log.e(TAG, "Use case binding failed", exc)
                }
            }

        }, mainDispatcher.asExecutor())
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity()
            .externalMediaDirs
            .firstOrNull()?.let {
                File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
            }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }
}