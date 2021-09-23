package com.example.samples.util.common

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionsUtil(
    val fragment: Fragment,
    val type: Type,
    permissionGranted: (Boolean) -> Unit
) {

    // Eventually, nice to know what type of permissions (camera, location, etc...)
    enum class Type {CAMERAX, LOCATION}

    companion object {
        val REQUIRED_CAMERA_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        val REQUIRED_LOCATION_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val permissionRequestLauncher = fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()){
        permissionGranted(it)
    }

    fun shouldOpenSettingsDialog(openSettings: (Intent?) -> Unit) {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle("Action Required")
            .setMessage(getMessage())
            .setNegativeButton("cancel") {_, _ -> openSettings(null)}
            .setPositiveButton("OK") { _, _ -> openSettings(openSettingsIntent) }
            .create()
            .show()
    }

    private fun getMessage() =
            when (type) {
                Type.CAMERAX -> "Camera Permissions required."
                Type.LOCATION -> "Location required."
            }

    private val openSettingsIntent: Intent = Intent()
        .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        .addCategory(Intent.CATEGORY_DEFAULT)
        .setData(Uri.parse("package:" + fragment.requireContext().packageName))
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)

    fun allPermissionsGranted() =
        when (type) {
            Type.CAMERAX -> REQUIRED_CAMERA_PERMISSIONS
            Type.LOCATION -> REQUIRED_LOCATION_PERMISSIONS
        }.all {
            val permission = ContextCompat.checkSelfPermission(fragment.requireContext(), it)
            val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(fragment.requireActivity(), it)
            Log.d("TAG","allPermissionsGranted: $permission, shouldShowRationale: $shouldShowRationale")
            ContextCompat.checkSelfPermission(fragment.requireContext(), it) == PackageManager.PERMISSION_GRANTED }

    fun allPermissionsGranted(onSuccess: () -> Unit) {
        if (allPermissionsGranted()) {
            onSuccess.invoke()
        } else {
            requestPermission()
        }
    }

    fun requestPermission() {
        val permissions: String = when (type) {
            Type.CAMERAX -> REQUIRED_CAMERA_PERMISSIONS.first()
            Type.LOCATION -> REQUIRED_LOCATION_PERMISSIONS.first()
        }
        permissionRequestLauncher.launch(permissions)
    }
}