package com.example.samples.ui.main.camera.types

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import com.example.samples.R
import com.example.samples.databinding.FragmentQrCameraLegacyBinding
import com.example.samples.ui.base.DataBindingFragment
import com.example.samples.util.camera.barcode.legacy.QRCodeImageReader
import com.example.samples.util.common.PermissionsUtil
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.util.*

@AndroidEntryPoint
class QRCamera2Fragment: DataBindingFragment<FragmentQrCameraLegacyBinding>(R.layout.fragment_qr_camera_legacy) {

    companion object {
        private const val TAG = "QRCameraLegacyFragment"
    }

    private lateinit var qrCodeImageReader: QRCodeImageReader

    private val exeuctor = Dispatchers.IO.asExecutor()

    private var cameraBkgHandler: Handler? = null

    private lateinit var permissionsUtil: PermissionsUtil

    private var cameraDevice: CameraDevice? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPermissionUtil()

        binding.surfaceView.holder.addCallback(callback)
    }

    private val callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) = startCameraPreview()

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

        override fun surfaceDestroyed(holder: SurfaceHolder) {}
    }

    override fun onDestroyView() {
        super.onDestroyView()

        cameraDevice?.close()
    }

    private fun initPermissionUtil() {
        permissionsUtil = PermissionsUtil(this, PermissionsUtil.Type.CAMERAX) { granted ->
            if (granted) {
                startCameraPreview()
            } else {
                permissionsUtil.shouldOpenSettingsDialog { intent ->
                    intent?.let { i -> startActivity(i) }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startCameraPreview() {
        if (permissionsUtil.allPermissionsGranted()) {
            activity?.let { _activity ->
                cameraBkgHandler = Handler(Looper.getMainLooper())

                // Get Camera using system service
                val cm = _activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager

                // find cameraIds on phone and verify Back Facing Lens exists
                val backFacingCamera = cm.cameraIdList.find {
                    val characteristics = cm.getCameraCharacteristics(it)
                    val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)

                    return@find cameraDirection != null && cameraDirection == CameraCharacteristics.LENS_FACING_BACK
                }

                if (backFacingCamera != null) {
                    val cameraStateCallback = createStateCallback()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        cm.openCamera(backFacingCamera, exeuctor, cameraStateCallback)
                    } else {
                        cm.openCamera(backFacingCamera, cameraStateCallback, cameraBkgHandler)
                    }
                }
            }
        } else {
            permissionsUtil.requestPermission()
        }
    }

    private fun createStateCallback() = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {

            cameraDevice = camera

            val barcodeDetector = BarcodeDetector.Builder(requireContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build()

            if (!barcodeDetector.isOperational) {
                Log.e("TAG", "barcodeDetector inoperational")
                return
            }

            qrCodeImageReader = QRCodeImageReader(
                binding.surfaceView.width,
                binding.surfaceView.height,
                barcodeDetector,
                cameraBkgHandler!!
            ) {
                postProcessCode(it)
            }

            val captureStateCallback = createCaptureStateCallback(camera)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                camera.createCaptureSession(
                    SessionConfiguration(
                        SessionConfiguration.SESSION_REGULAR,
                        listOf(
                            OutputConfiguration(binding.surfaceView.holder.surface),
                            OutputConfiguration(qrCodeImageReader.imageReader.surface)
                        ),
                        exeuctor,
                        captureStateCallback
                    )
                )
            } else {
                camera.createCaptureSession(
                    listOf(
                        binding.surfaceView.holder.surface,
                        qrCodeImageReader.imageReader.surface
                    ),
                    captureStateCallback,
                    cameraBkgHandler
                )
            }
        }

        override fun onDisconnected(camera: CameraDevice) {}

        override fun onError(camera: CameraDevice, error: Int) {}

    }

    private fun createCaptureStateCallback(camera: CameraDevice) = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            val builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            builder.addTarget(binding.surfaceView.holder.surface)
            builder.addTarget(qrCodeImageReader.imageReader.surface)
            session.setRepeatingRequest(builder.build(), null, null)
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            Log.d(TAG, "onConfigureFailed")
        }
    }

    private fun postProcessCode(result: String?) {
        Toast.makeText(requireContext(),
            if (result == null) "null code received..." else "QR code is: $result",
            Toast.LENGTH_SHORT)
            .show()

        // Important to let QR Code Analyzer to resume scanning images for QR Codes
        qrCodeImageReader.resume()
    }
}