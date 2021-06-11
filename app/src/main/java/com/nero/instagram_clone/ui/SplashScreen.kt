package com.nero.instagram_clone.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.nero.instagram_clone.R
import com.nero.instagram_clone.preferencehelper.PreferenceHelper
import com.nero.instagram_clone.utils.Constants
import com.nero.instagram_clone.utils.Constants.SPLASH_LENGTH
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PreferenceHelper.getSharedPreferences(this)
        mAuth = FirebaseAuth.getInstance()

        if (PreferenceHelper.readBooleanFromPreference(Constants.KEY_USER_LOGGED_IN)) {
            if (GoogleSignIn.getLastSignedInAccount(this) != null || mAuth.currentUser != null) {
                startMainActivity()
            } else {
                startLoginActivity()
            }
        } else {
            startLoginActivity()
        }
    }

    private fun startLoginActivity() {
        Handler().postDelayed(Runnable {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, SPLASH_LENGTH)

    }

    private fun startMainActivity() {
        Handler().postDelayed(Runnable {
            startActivity(Intent(this, MainScreen::class.java))
            finish()
        }, SPLASH_LENGTH)
    }

}