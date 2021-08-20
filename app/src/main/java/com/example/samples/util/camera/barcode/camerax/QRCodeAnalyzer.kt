package com.example.samples.util.camera.barcode.camerax

import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @ExperimentalGetImage
 * The getImage() method makes the assumptions that each ImageProxy is the sole owner of the
 * underlying Image which might not be the case. In the case where the Image is shared by multiple
 * ImageProxy, if the Image is closed then it will invalidate multiple ImageProxy without a way to
 * clearly indicate this has occurred.
 */
open class QRCodeAnalyzer(private val listener: (Barcode) -> Unit): ImageAnalysis.Analyzer {

    companion object {
        private const val TAG = "QRCodeAnalyzer"
    }

    private val enabled = AtomicBoolean(true)

    fun resume() = enabled.set(true)

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {

        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)

            // Optional, faster if scanner knows exact formats to scan for
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            BarcodeScanning
                .getClient(options)
                .process(inputImage)
                .addOnSuccessListener { processBarcodes(it) }
                .addOnFailureListener { Log.e(TAG, "onFailed", it) }
                .addOnCompleteListener {
                    // Make sure to close imageproxy after onComplete when done with analyze
                    // mutliple calls to imageProxy.close() do not cause crash
                    imageProxy.close()
                }
        }
    }

    private fun processBarcodes(barcodes: List<Barcode>) {
        if (enabled.get()){
            if (barcodes.isNotEmpty()) {
                enabled.set(false)
                barcodes.forEach {
                    if (it.valueType == Barcode.TYPE_URL)
                        listener(it)
                }
            }
        }
    }
}