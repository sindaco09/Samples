package com.indaco.samples.ui.main.camera

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.indaco.samples.R
import com.indaco.samples.databinding.FragmentCameraMenuBinding
import com.indaco.samples.ui.base.DataBindingFragment
import com.indaco.samples.ui.main.camera.types.ImageToTextCameraXFragment
import com.indaco.samples.ui.main.camera.types.QRCamera2Fragment
import com.indaco.samples.ui.main.camera.types.QRCameraXFragment

class CameraMenuFragment: DataBindingFragment<FragmentCameraMenuBinding>(R.layout.fragment_camera_menu) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun init() {
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.action_qr_camerax -> QRCameraXFragment()
                R.id.action_qr_camera_legacy -> QRCamera2Fragment()
                R.id.action_image_to_text_reader -> ImageToTextCameraXFragment()
                else -> null
            }?.let {
                childFragmentManager.commit { replace(binding.cameraFragmentContainer.id, it) }
            }
            true
        }

        binding.bottomNav.selectedItemId = R.id.action_qr_camerax
    }
}
