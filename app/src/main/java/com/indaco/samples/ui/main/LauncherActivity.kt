package com.indaco.samples.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG","LauncherActivity")

        checkDeepLink()
    }

    private fun checkDeepLink() {
        val uri = intent.data
//        if (uri != null) {
//            if (uri.host == "sindaco09.github.io") {
//                when (uri.lastPathSegment) {
//                    "auth" -> goToAuth()
//                    else -> goToHome()
//                }
//            }
//        } else
//            goToHome()
        goToAuth()
    }

    private fun goToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun goToAuth() {
        Intent().setClassName(packageName, "com.indaco.auth.ui.screens.AuthActivity")
            .also { startActivity(it) }
    }
}