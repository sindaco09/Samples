package com.example.samples.util.camerax

import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.nio.ByteBuffer

open class QRCodeAnalyzer(private val listener: (Barcode) -> Unit): ImageAnalysis.Analyzer {

    companion object {
        private const val TAG = "QRCodeAnalyzer"
    }

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
                    Log.d(TAG,"onComplete")

                    // Make sure to close imageproxy after onComplete when done with analyze
                    imageProxy.close()
                }
        }
    }

    private fun processBarcodes(barcodes: List<Barcode>) {
        barcodes.forEach { barcode ->
            Log.d(TAG, "processBarcodes: ${barcode.valueType}, ${barcode.rawValue}")
            when (barcode.valueType) {
                Barcode.TYPE_URL -> {
                    listener(barcode)
                }
            }
        }
    }
}