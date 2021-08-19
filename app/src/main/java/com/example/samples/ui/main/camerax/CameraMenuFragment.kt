package com.example.samples.ui.main.camerax

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.example.samples.R
import com.example.samples.databinding.FragmentCameraMenuBinding
import com.example.samples.ui.base.DataBindingFragment
import com.example.samples.ui.main.camerax.types.ImageToTextCameraFragment
import com.example.samples.ui.main.camerax.types.QRCameraLegacyFragment
import com.example.samples.ui.main.camerax.types.QRCameraXFragment

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
                R.id.action_qr_camera_legacy -> QRCameraLegacyFragment()
                R.id.action_image_to_text_reader -> ImageToTextCameraFragment()
                else -> null
            }?.let {
                childFragmentManager.commit { replace(binding.cameraFragmentContainer.id, it) }
            }
            true
        }

        binding.bottomNav.selectedItemId = R.id.action_qr_camerax
    }
}
