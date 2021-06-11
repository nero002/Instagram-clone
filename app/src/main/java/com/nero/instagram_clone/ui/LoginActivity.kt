package com.nero.instagram_clone.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.nero.instagram_clone.R
import com.nero.instagram_clone.models.UserModel
import com.nero.instagram_clone.preferencehelper.PreferenceHelper
import com.nero.instagram_clone.utils.Constants.KEY_DISPLAY_NAME
import com.nero.instagram_clone.utils.Constants.KEY_LOGIN_WITH_OAUTH
import com.nero.instagram_clone.utils.Constants.KEY_USER_GOOGLE_GMAIL
import com.nero.instagram_clone.utils.Constants.KEY_USER_GOOGLE_ID
import com.nero.instagram_clone.utils.Constants.KEY_USER_LOGGED_IN
import com.nero.instagram_clone.utils.Constants.KEY_USER_NAME
import com.shobhitpuri.custombuttons.GoogleSignInButton
import java.util.*


class LoginActivity : AppCompatActivity() {

    lateinit var gso: GoogleSignInOptions
    lateinit var googleSignInClient: GoogleSignInClient
    val SIGN_IN_CODE = 10
    private var mAuth: FirebaseAuth? = null

    private var mEtEmail: EditText? = null
    private var mEtPassword: EditText? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        PreferenceHelper.getSharedPreferences(this)
        initializeViewsAndListeners()
        initializeSignin()
    }

    private fun initializeViewsAndListeners() {
        mEtEmail = findViewById(R.id.tvEmail)
        mEtPassword = findViewById(R.id.tvPassword)
        progressBar = findViewById(R.id.pb)
        progressBar?.visibility = View.INVISIBLE
    }

    private fun initializeSignin() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mAuth = FirebaseAuth.getInstance()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInButton = findViewById<GoogleSignInButton>(R.id.signInButton);
        signInButton.setOnClickListener {
            val intent: Intent = googleSignInClient.signInIntent
            startActivityForResult(intent, SIGN_IN_CODE)
        }

        findViewById<View>(R.id.btnSignUp).setOnClickListener {
            createUser()
        }
    }

    private fun createUser() {
        val email = mEtEmail!!.text.toString().trim { it <= ' ' }
        val password = mEtPassword!!.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            mEtEmail!!.error = "Email Required"
            mEtEmail!!.requestFocus()
            return
        }
        if (password.isEmpty()) {
            mEtPassword!!.error = "Password Required"
            mEtPassword!!.requestFocus()
            return
        }
        progressBar!!.visibility = View.VISIBLE
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUser()
                    updatePreference(mAuth!!)
                    startMainScreen()
                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        userLogin(email, password)
                    } else {
                        progressBar!!.visibility = View.INVISIBLE
                        Toast.makeText(
                            this@LoginActivity,
                            task.exception!!.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
    }

    private fun userLogin(email: String, password: String) {
        try {
            mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = FirebaseDatabase.getInstance().getReference("users")
                                .child(mAuth?.uid!!)
                            val token = Objects.requireNonNull(task.result)
                            user.child("token").setValue(token)
                        } else {
                            Log.d("TAG", "onComplete: " + task.exception!!.message)
                        }
                    }
                    updatePreference(mAuth!!)
                    startMainScreen()
                } else {
                    progressBar!!.visibility = View.INVISIBLE
                    Toast.makeText(this@LoginActivity, task.exception!!.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } catch (e: java.lang.Exception) {
            Log.d("TAG", "userLogin: Exception -> $e")
        }
    }

    private fun startMainScreen() {
        val intent = Intent(this, MainScreen::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_CODE) {
            var task: Task<GoogleSignInAccount>? = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try {
            if (task!!.isSuccessful) {
                val account = task.getResult(ApiException::class.java)
                PreferenceHelper.writeBooleanToPreference(KEY_LOGIN_WITH_OAUTH, true)
                updatePreference(account!!)
                Toast.makeText(this, "Welcome ${account.displayName}", Toast.LENGTH_SHORT)
                    .show()
                saveUser(account)

                startActivity(Intent(this, MainScreen::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Login Error " + task.exception?.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {

        }
    }


    private fun updatePreference(account: GoogleSignInAccount) {
        PreferenceHelper.writeBooleanToPreference(KEY_USER_LOGGED_IN, true)
        PreferenceHelper.writeStringToPreference(KEY_USER_GOOGLE_ID, account!!.id)
        PreferenceHelper.writeStringToPreference(
            KEY_DISPLAY_NAME,
            account.displayName
        )
        PreferenceHelper.writeStringToPreference(
            KEY_USER_GOOGLE_GMAIL,
            account.email
        )
    }

    private fun updatePreference(account: FirebaseAuth) {
        PreferenceHelper.writeBooleanToPreference(KEY_USER_LOGGED_IN, true)
        PreferenceHelper.writeBooleanToPreference(KEY_LOGIN_WITH_OAUTH, false)
        PreferenceHelper.writeStringToPreference(KEY_USER_GOOGLE_ID, account!!.uid)
        PreferenceHelper.writeStringToPreference(
            KEY_DISPLAY_NAME,
            account.currentUser?.displayName
        )
        PreferenceHelper.writeStringToPreference(
            KEY_USER_GOOGLE_GMAIL,
            account.currentUser?.email
        )
    }


    private fun saveUser() {
//        FirebaseMessaging.getInstance().subscribeToTopic("updates")
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = Objects.requireNonNull(task.result)
                Log.d("TAG", "onComplete: $token")
                saveToken(token)
            } else {
                Log.d("TAG", "onComplete: " + task.exception!!.message)
            }
        }
    }

    private fun saveUser(account: GoogleSignInAccount) {

        val database = FirebaseDatabase.getInstance()

        val dbUsers = database.getReference("users").child(account.id!!)

        dbUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //even if user exit in database the FCM token will be different for each device
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = FirebaseDatabase.getInstance().getReference("users")
                                .child(account.id!!)
                            val token = Objects.requireNonNull(task.result)
                            user.child("token").setValue(token)
                        } else {
                            Log.d("TAG", "onComplete: " + task.exception!!.message)
                        }
                    }
                    Toast.makeText(this@LoginActivity, "Welcomeback", Toast.LENGTH_SHORT).show()
                    return
                }
                if (!snapshot.exists()) {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener {
                        if (it.isSuccessful) {
                            val token: String = Objects.requireNonNull<String>(it.result)
                            val username: String = ""
                            val user =
                                UserModel(
                                    account.email!!,
                                    account.displayName,
                                    username,
                                    null,
                                    null,
                                    token
                                )

                            dbUsers.setValue(user)
                                .addOnCompleteListener { it_inside ->
                                    if (it_inside.isSuccessful) {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "token saved",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun saveToken(token: String?) {

        try {
            val dbUsers = FirebaseDatabase.getInstance().getReference("users")

            val email = mAuth!!.currentUser!!.email
            val username: String = ""
            val user =
                UserModel(
                    email!!,
                    mAuth!!.currentUser!!.displayName,
                    username,
                    null,
                    null,
                    token!!
                )

            dbUsers.child(mAuth!!.currentUser!!.uid)
                .setValue(user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "token saved", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: java.lang.Exception) {
            Log.d("TAG", "saveToken: $e")
        }
    }

}