package com.example.samples.util.camerax.barcode.legacy

import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.media.ImageReader
import android.os.Handler
import android.util.Log
import androidx.core.util.forEach
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.util.concurrent.atomic.AtomicBoolean

class QRCodeImageReader(
    val width: Int,
    val height: Int,
    val barcodeDetector: BarcodeDetector,
    val handler: Handler,
    val listener: (String) -> Unit
) {

    private val enabled = AtomicBoolean(true)

    fun resume() = enabled.set(true)

    var imageReader: ImageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)

    init {
        imageReader.setOnImageAvailableListener({ reader ->
            val cameraImage = reader.acquireNextImage()

            if (enabled.get()) {
                enabled.set(false)
                val buffer = cameraImage.planes.first().buffer
                val bytes = ByteArray(buffer.capacity())
                buffer.get(bytes)

                val bitmap =
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.count(), null)
                val frameToProcess = Frame.Builder().setBitmap(bitmap).build()
                val barcodeResults = barcodeDetector.detect(frameToProcess)

                if (barcodeResults.size() > 0) {
                    Log.d("TAG", "Barcode detected!")
                    barcodeResults.forEach { key, value ->
                        listener.invoke(value.rawValue)
                    }
                } else {
//                    Log.d("TAG", "No barcode found")
                    resume()
                }
            }

            cameraImage.close()
        }, handler)
    }


}