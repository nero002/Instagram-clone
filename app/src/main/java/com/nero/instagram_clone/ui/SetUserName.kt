package com.nero.instagram_clone.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.nero.instagram_clone.R
import com.nero.instagram_clone.models.UserModel
import com.nero.instagram_clone.preferencehelper.PreferenceHelper
import com.nero.instagram_clone.utils.Constants
import com.nero.instagram_clone.utils.Constants.KEY_USER_NAME
import java.lang.String
import java.util.*

class SetUserName : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_user_name)
        PreferenceHelper.getSharedPreferences(this)

        findViewById<Button>(R.id.btnSetUserName).setOnClickListener {
            val username = findViewById<EditText>(R.id.etSetUserName).text.toString()
            Log.d("TAG", "onCreate: inside")

            if (username == "") {
                Toast.makeText(this, "Username shd not be null", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val s = FirebaseDatabase.getInstance().getReference("users").get()
                callIt(username, s)

            }
        }
    }

    private fun callIt(username: kotlin.String, s: Task<DataSnapshot>) {
        s.addOnSuccessListener {
            it.children.forEach { item ->
                if (item.child("username").value == username) {
                    Toast.makeText(this, "already exist", Toast.LENGTH_SHORT).show()
                    findViewById<EditText>(R.id.etSetUserName).error = "userid exist"
                    return@addOnSuccessListener
                }
            }
            setTheUserName(username)
        }
    }

    private fun setTheUserName(username: kotlin.String) {
        PreferenceHelper.writeStringToPreference(KEY_USER_NAME, username)
        val user = FirebaseDatabase.getInstance().getReference("users")
            .child(PreferenceHelper.readStringFromPreference(Constants.KEY_USER_GOOGLE_ID))

        user.child("username").setValue(username)

        startActivity(Intent(this, MainScreen::class.java))
        finish()
    }
}