package com.indaco.camera.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.indaco.camera.R
import com.indaco.camera.databinding.FragmentCameraMenuBinding
import com.indaco.camera.ui.screens.types.ImageToTextCameraXFragment
import com.indaco.camera.ui.screens.types.QRCamera2Fragment
import com.indaco.camera.ui.screens.types.QRCameraXFragment
import com.indaco.samples.util.viewBinding

class CameraMenuFragment: Fragment(R.layout.fragment_camera_menu) {

    private val binding by viewBinding(FragmentCameraMenuBinding::bind)

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
