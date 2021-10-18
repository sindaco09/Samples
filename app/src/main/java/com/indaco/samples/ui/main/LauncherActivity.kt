package com.indaco.samples.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG","LauncherActivity")

        checkDeepLink()
    }

    private fun checkDeepLink() {
        val uri = intent.data
        if (uri != null) {
            if (uri.host == "sindaco09.github.io") {
                when (uri.lastPathSegment) {
                    "auth" -> goToAuth()
                    "promo" -> goToPromo()
                    else -> goToHome()
                }
            }
        } else
            goToHome()
    }

    private fun goToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }



    private fun goToAuth() {
        Intent().setClassName(packageName, "com.indaco.auth.ui.screens.AuthActivity")
            .also { startActivity(it) }
    }

    private fun goToPromo() {
        Intent().setClassName(packageName, "com.indaco.promo.PromoActivity")
            .also { startActivity(it) }
    }
}