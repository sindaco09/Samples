package com.example.samples.util.camerax.barcode

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicBoolean

class CameraPreview(val context: Context, val imageAnalyzer: ImageAnalysis.Analyzer? = null) {

    companion object {
        private const val TAG = "BarcodeCamera"
    }

    private var _imageCapture: ImageCapture? = null
        get() {
            if (field == null)
                field = ImageCapture.Builder().build()
            return field
        }
    val imageCapture: ImageCapture? get() = _imageCapture

    private fun buildImageAnalyzer(executor: Executor, imageAnalyzer: ImageAnalysis.Analyzer) = ImageAnalysis.Builder()
        .build()
        .also {
            it.setAnalyzer(executor, imageAnalyzer)
        }

    private fun buildPreview(surfaceProvider: Preview.SurfaceProvider) = Preview.Builder()
        .build()
        .also { it.setSurfaceProvider(surfaceProvider) }

    private fun getCameraSelector() = CameraSelector.DEFAULT_BACK_CAMERA

    fun startCamera(
        owner: LifecycleOwner,
        executor: Executor,
        surfaceProvider: Preview.SurfaceProvider
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            cameraProviderFuture.get().apply {
                try {
                    // Unbind use cases before rebinding
                    unbindAll()

                    // Bind use cases to camera
                    if (imageAnalyzer != null) {
                        bindToLifecycle(
                            owner,
                            getCameraSelector(),
                            buildPreview(surfaceProvider),
                            _imageCapture,
                            buildImageAnalyzer(executor, imageAnalyzer)
                        )
                    } else {
                        bindToLifecycle(
                            owner,
                            getCameraSelector(),
                            buildPreview(surfaceProvider),
                            _imageCapture
                        )
                    }

                } catch (exc: Exception) {
                    Log.e(TAG, "Use case binding failed", exc)
                }
            }

        }, executor)
    }
}