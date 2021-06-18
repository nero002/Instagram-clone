package com.nero.instagram_clone.ui.externalfragments

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.nero.instagram_clone.R
import com.nero.instagram_clone.models.PostModel
import com.nero.instagram_clone.preferencehelper.PreferenceHelper
import com.nero.instagram_clone.ui.MainScreen
import com.nero.instagram_clone.utils.Constants.KEY_USER_GOOGLE_ID
import com.nero.instagram_clone.utils.Constants.KEY_USER_NAME
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.*


@AndroidEntryPoint
class PostFragment : Fragment(R.layout.fragment_post) {

    private val imgUri: Uri = MainScreen.tempPicPath!!
    var uploadTask: UploadTask? = null
    var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    var storageReference: StorageReference? = null
    var db = FirebaseFirestore.getInstance()
    var documentReference: DocumentReference? = null
    lateinit var progressBar: ProgressBar


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            progressBar = findViewById<ProgressBar>(R.id.pbCP)
            PreferenceHelper.getSharedPreferences(context)
            val imageView = findViewById<ImageView>(R.id.added_image)
            Log.d("TAG", "onViewCreated: " + MainScreen.tempPicPath)
            Glide.with(context).load(MainScreen.tempPicPath).into(imageView)
            documentReference = db.collection("posts").document("posts")
            storageReference = firebaseStorage.getReference("post_images")

            findViewById<ImageView>(R.id.back_from_post).setOnClickListener {
                activity?.onBackPressed()
            }

            findViewById<ImageView>(R.id.post_now).setOnClickListener {
                uploadPost(view)
            }

        }

    }

    private fun uploadPost(view: View) {
        val caption = view.findViewById<EditText>(R.id.added_caption);
        val tags = view.findViewById<EditText>(R.id.added_tags);
        progressBar.visibility = View.VISIBLE

        val reference = storageReference!!.child(
            System.currentTimeMillis().toString() + "." + getFileExt(view, imgUri)
        )
        uploadTask = reference.putFile(imgUri)

        val uriTask = uploadTask!!.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            reference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val post: MutableMap<String, String> =
                    HashMap()
                post["caption"] = caption.text.toString()
                post["tags"] = tags.text.toString()
                post["url"] = downloadUri.toString()
                documentReference!!.set(post).addOnSuccessListener {
                    progressBar.setVisibility(View.INVISIBLE)
                    addToRealTimeDataBase(view, post)
                    //val intent = Intent(view.context, ShowProfile::class.java)
                    //                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(
                        view.context,
                        "Post Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(
                view.context,
                "Upload Failed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun addToRealTimeDataBase(view: View, post: MutableMap<String, String>) {

        val timeStamp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().toString()
        } else {
            Date().toString()
        }

        val type = getFileExt(view, imgUri)
        if (type == "jpg" || type == "jpeg" || type == "png") {

        } else if (type == "mp4" || type == "webp" || type == "mkv" || type == "3gp") {

        } else {
            Toast.makeText(context, "Unsupported Type", Toast.LENGTH_SHORT).show()
            return
        }

        val postModel: PostModel =
            PostModel(
                PreferenceHelper.readStringFromPreference(KEY_USER_GOOGLE_ID),
                PreferenceHelper.readStringFromPreference(KEY_USER_NAME),
                post["caption"], post["tags"], post["url"], timeStamp,
                true,
                type,
                0, 0, 0
            )

        FirebaseDatabase.getInstance().getReference("posts")
            .child(
                System.currentTimeMillis()
                    .toString() + (Math.random() * 123).toInt()
            ).setValue(postModel).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(view.context, "Post is Online", Toast.LENGTH_SHORT)
                        .show()
                    activity?.onBackPressed()
                    activity?.onBackPressed()
                }
            }
    }

    fun getFileExt(view: View, uri: Uri): String? {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(view.context.contentResolver.getType(uri))
    }

}