package com.example.samples.util.common

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionsUtil(val fragment: Fragment, permissionGranted: (Boolean) -> Unit) {

    // Eventually, nice to know what type of permissions (camera, location, etc...)
    enum class Type {CAMERAX}

    companion object {
        val REQUIRED_CAMERA_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private val permissionRequestLauncher = fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()){
        Log.d("TAG","permissionGranted: $it")
        permissionGranted(it)
    }

    fun shouldOpenSettingsDialog(openSettings: (Intent?) -> Unit) {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle("Action Required")
            .setMessage("Camera Permissions required.")
            .setNegativeButton("cancel") {_, _ -> openSettings(null)}
            .setPositiveButton("OK") { _, _ -> openSettings(openSettingsIntent) }
            .create()
            .show()
    }

    private val openSettingsIntent: Intent = Intent()
        .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        .addCategory(Intent.CATEGORY_DEFAULT)
        .setData(Uri.parse("package:" + fragment.requireContext().packageName))
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)

    fun allPermissionsGranted() = REQUIRED_CAMERA_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(fragment.requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        permissionRequestLauncher.launch(REQUIRED_CAMERA_PERMISSIONS.first())
    }
}