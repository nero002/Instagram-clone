package com.nero.instagram_clone.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.nero.instagram_clone.R
import com.nero.instagram_clone.databinding.MainscreenBinding
import com.nero.instagram_clone.models.UserModel
import com.nero.instagram_clone.preferencehelper.PreferenceHelper
import com.nero.instagram_clone.utils.Constants
import com.nero.instagram_clone.utils.Constants.KEY_USER_GOOGLE_ID
import com.nero.instagram_clone.utils.Constants.KEY_USER_NAME
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainScreen : AppCompatActivity() {

    private lateinit var binding: MainscreenBinding
    private lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceHelper.getSharedPreferences(this)

        if (PreferenceHelper.readStringFromPreference(KEY_USER_NAME) == null
            || PreferenceHelper.readStringFromPreference(KEY_USER_NAME) == ""
        ) {
            FirebaseDatabase.getInstance().getReference("users").child(
                PreferenceHelper.readStringFromPreference(
                    KEY_USER_GOOGLE_ID
                )
            ).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("username").value == null || snapshot.child("username").value == "") {
                        startActivity(Intent(this@MainScreen, SetUserName::class.java))
                    } else {
                        PreferenceHelper.writeStringToPreference(
                            KEY_USER_NAME,
                            snapshot.child("username").value.toString()
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
        homeNavigationSetup()
    }

    private fun homeNavigationSetup() {
        binding = MainscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_mainscreen)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

}