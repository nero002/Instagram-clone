package com.nero.instagram_clone.ui

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

import com.nero.instagram_clone.R
import com.nero.instagram_clone.databinding.MainscreenBinding
import com.nero.instagram_clone.preferencehelper.PreferenceHelper
import com.nero.instagram_clone.utils.Constants.KEY_USER_GOOGLE_ID
import com.nero.instagram_clone.utils.Constants.KEY_USER_NAME
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainScreen : AppCompatActivity() {

    companion object {
        var tempPicPath: String = ""
    }

    private lateinit var binding: MainscreenBinding
    private lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceHelper.getSharedPreferences(this)
        homeNavigationSetup()

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
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_reel,
                R.id.navigation_like,
                R.id.navigation_profile
            )
        )

        navView.setupWithNavController(navController)
    }


}