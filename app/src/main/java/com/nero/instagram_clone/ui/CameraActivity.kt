package com.nero.instagram_clone.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import com.nero.instagram_clone.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {

    private val CAMERA_REQUEST = 1888
    private var imageView: ImageView? = null
    private val MY_CAMERA_PERMISSION_CODE = 100
    var anim: AnimationDrawable? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val llContainer =findViewById<LinearLayout>(R.id.llContainer)
        anim = llContainer.background as AnimationDrawable
        anim?.setEnterFadeDuration(6000)
        anim?.setExitFadeDuration(2000)

        imageView = findViewById(R.id.imageView1)
        val photoButton: Button = findViewById(R.id.button1)
        photoButton.setOnClickListener(View.OnClickListener {

            if (PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PermissionChecker.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
            } else {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAG", "onRequestPermissionsResult: Granted")
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                Log.d("TAG", "onRequestPermissionsResult: Denied")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val photo = data?.extras!!["data"] as Bitmap?
            imageView!!.setImageBitmap(photo)
        }
    }

    override fun onResume() {
        super.onResume()
        if (anim != null && !anim!!.isRunning) anim!!.start()
    }
    override fun onPause() {
        super.onPause()
        if (anim != null && anim!!.isRunning) anim!!.stop()
    }
}