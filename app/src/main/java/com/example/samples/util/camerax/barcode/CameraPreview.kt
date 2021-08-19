package com.example.samples.util.camerax.barcode

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executor

class CameraPreview(val context: Context, val imageAnalyzer: ImageAnalysis.Analyzer? = null) {

    companion object {
        private const val TAG = "BarcodeCamera"
        const val NO_CAMERA_CODE = 1
    }

    private var _imageCapture: ImageCapture? = null
        get() {
            if (field == null)
                field = ImageCapture.Builder().build()
            return field
        }

    private fun buildImageAnalyzer(executor: Executor, imageAnalyzer: ImageAnalysis.Analyzer) =
        ImageAnalysis.Builder().build().also { it.setAnalyzer(executor, imageAnalyzer) }

    private fun buildPreview(surfaceProvider: Preview.SurfaceProvider) =
        Preview.Builder().build().also { it.setSurfaceProvider(surfaceProvider) }

    fun startCamera(
        owner: LifecycleOwner,
        executor: Executor,
        surfaceProvider: Preview.SurfaceProvider,
        onError: (Int) -> Unit
    ) {
        Log.d("TAG","startCamera")
        val cameraProvider = ProcessCameraProvider.getInstance(context)

        cameraProvider.addListener({
            cameraProvider.get().apply {


                    try {
                        // Unbind use cases before rebinding
                        unbindAll()

                        val cameraSelector = when {
                            hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) -> CameraSelector.DEFAULT_BACK_CAMERA
                            hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) -> CameraSelector.DEFAULT_FRONT_CAMERA
                            else -> null
                        }

                        if (cameraSelector != null) {
                            Log.d("TAG","startCamera camera available: ${cameraSelector.toString()}")

                            // Bind use cases to camera
                            if (imageAnalyzer != null) {
                                Log.d("TAG","startCamera has imageAnalyzer")
                                bindToLifecycle(
                                    owner,
                                    cameraSelector,
                                    buildPreview(surfaceProvider),
                                    _imageCapture,
                                    buildImageAnalyzer(executor, imageAnalyzer)
                                )
                            } else {
                                Log.d("TAG","startCamera has NO imageAnalyzer")
                                bindToLifecycle(
                                    owner,
                                    cameraSelector,
                                    buildPreview(surfaceProvider),
                                    _imageCapture
                                )
                            }
                        } else {
                            Log.d("TAG","startCamera no camera")
                            onError.invoke(NO_CAMERA_CODE)
                        }
                    } catch (exc: Exception) {
                        Log.e(TAG, "Use case binding failed", exc)
                    }

            }

        }, executor)
    }
}