package com.indaco.camera.util.image_to_text

import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class ImageToTextAnalyzer: ImageAnalysis.Analyzer {

    companion object {
        private const val TAG = "ImageToTextAnalyzer"
    }

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->

            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(inputImage)
                .addOnSuccessListener {
                    processText(it)
                }
                .addOnFailureListener {

                }
                .addOnCompleteListener {
                    imageProxy.close()
                }

        }
    }

    fun processText(text: Text) {
        text.textBlocks.forEach { block ->
            block.lines.forEach {
                Log.d(TAG, "processText: block: textline: ${it.text}")
            }
        }
    }
}