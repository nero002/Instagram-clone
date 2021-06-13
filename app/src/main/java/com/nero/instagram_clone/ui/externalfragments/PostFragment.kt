package com.nero.instagram_clone.ui.externalfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.nero.instagram_clone.R
import com.nero.instagram_clone.ui.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post.*
import java.io.File


@AndroidEntryPoint
class PostFragment() : Fragment(R.layout.fragment_post) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            val imageView = findViewById<ImageView>(R.id.added_image)
            Glide.with(context).load(File(MainScreen.tempPicPath)).into(imageView)
        }


    }

}