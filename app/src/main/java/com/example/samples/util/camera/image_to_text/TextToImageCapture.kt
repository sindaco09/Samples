package com.example.samples.util.camera.image_to_text

import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import java.util.concurrent.Executor

@ExperimentalGetImage
class TextToImageCapture {

    companion object {
        private const val TAG = "TextToImageCapture"
    }

    private var analyzer: ImageToTextAnalyzer = ImageToTextAnalyzer()

    fun takePhoto(_imageCapture: ImageCapture?, executor: Executor) {
        Log.d(TAG,"takePhoto: ${_imageCapture == null}")
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = _imageCapture ?: return

        imageCapture.takePicture(executor, object: ImageCapture.OnImageCapturedCallback() {
            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
            }

            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                Log.d(TAG,"onCaptureSuccess:")
                processText(image)
            }
        })
    }

    private fun processText(imageProxy: ImageProxy) {
        analyzer.analyze(imageProxy)
    }
}