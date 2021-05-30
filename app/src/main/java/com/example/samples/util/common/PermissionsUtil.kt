package com.example.samples.util.common

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class PermissionsUtil(val context: Context) {

    companion object {
        val REQUIRED_CAMERA_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    fun shouldOpenSettingsDialog(openSettings: (Intent?) -> Unit) {
        AlertDialog.Builder(context)
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
        .setData(Uri.parse("package:" + context.packageName))
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)

    fun allPermissionsGranted() = REQUIRED_CAMERA_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}