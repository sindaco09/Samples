package com.indaco.auth.utils.google

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.result.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.indaco.auth.BuildConfig

class GoogleAccountUtil<T: ActivityResultCaller>(val activityResult: T, val context: Context, val listener: SignInCredential.() -> Unit) {

    private val googleSignInCredentialHandler =
        activityResult.registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult? ->
            if (result?.resultCode == Activity.RESULT_OK) {
                val credential = Identity.getSignInClient(context).getSignInCredentialFromIntent(result.data)
                handleSignInCredential(credential)
            }
        }

    fun signOut() {

    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>, function: (GoogleSignInAccount?) -> Unit) {
        try {
            val account = task.getResult(ApiException::class.java)
            function.invoke(account)
            logAccountInfo(account)
        } catch (e: ApiException) {
            Log.e("TAG","error getting result", e)
        }
    }

    private fun logAccountInfo(account: GoogleSignInAccount?) {
        Log.d("TAG","updateUI: account: " +
                "\n\tdisplayName: ${account?.displayName}" +
                "\n\tfamilyname: ${account?.familyName}" +
                "\n\tgivenName: ${account?.givenName}" +
                "\n\temail: ${account?.email}" +
                "\n\ttoken: ${account?.idToken}")
    }

    // Google One Tap Sign in/up
    fun oneTapGoogleSignIn() {
        Identity.getSignInClient(context).beginSignIn(getSignInRequest())
            .addOnSuccessListener {
                googleSignInCredentialHandler.launch(IntentSenderRequest.Builder(it.pendingIntent.intentSender).build())
            }.addOnFailureListener {
                Log.e("TAG","signInFailed",it)
            }
    }

    fun oneTapGoogleSignUp() {
        Log.d("TAG","onTapGoogleSignup")
        Identity.getSignInClient(context).beginSignIn(getSignUpRequest())
            .addOnSuccessListener {
                googleSignInCredentialHandler.launch(IntentSenderRequest.Builder(it.pendingIntent.intentSender).build())
            }.addOnFailureListener {
                Log.e("TAG","signInFailed",it)
            }
    }

    fun handleSignInCredential(credential: SignInCredential) {
        logAccountInfo(credential)
        val idToken = credential.googleIdToken
        when {
            idToken != null -> {
                // Got an ID token from Google. Use it to authenticate
                Log.d("TAG", "Got ID token.")
                listener.invoke(credential)
            }
            else -> {
                // Shouldn't happen.
                Log.d("TAG", "No ID token!")
            }
        }
    }

    companion object {
        fun getSignInRequest() = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.SERVER_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

        fun getSignUpRequest() = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.SERVER_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        fun logAccountInfo(credential: SignInCredential?) {
            Log.d("TAG","updateUI: account: " +
                    "\n\temail: ${credential?.id}" +
                    "\n\tgoogleIdToken: ${credential?.googleIdToken}" +
                    "\n\tdisplayName: ${credential?.displayName}" +
                    "\n\tfamilyname: ${credential?.familyName}" +
                    "\n\tgivenName: ${credential?.givenName}")
        }
    }

    // Sign in using new google identity (meant to eventually replace existing signin/up api)
    private fun googleIdentitySignIn() {
        val request = GetSignInIntentRequest.builder().setServerClientId(BuildConfig.SERVER_CLIENT_ID).build()
        Identity.getSignInClient(context)
            .getSignInIntent(request)
            .addOnSuccessListener {
                googleSignInCredentialHandler.launch(IntentSenderRequest.Builder(it.intentSender).build())
            }
    }
}