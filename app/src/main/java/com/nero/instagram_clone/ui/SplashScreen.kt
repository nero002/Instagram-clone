package com.nero.instagram_clone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nero.instagram_clone.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}