package com.nero.instagram_clone.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jarvanmo.exoplayerview.media.SimpleMediaSource
import com.jarvanmo.exoplayerview.ui.ExoVideoView
import com.nero.instagram_clone.R
import com.nero.instagram_clone.models.PostModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomePostViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    var user_img: CircleImageView
    var username: TextView
    var txt_likes: TextView
    var txt_caption: TextView
    var txt_tags: TextView
    var txt_timePosted: TextView
    var txt_commments: TextView
    var option: ImageView
    var postImage: ImageView
    var postVideo: ExoVideoView
    var img_heart: ImageView
    var img_heart_red: ImageView
    var img_comments: ImageView
    var img_send: ImageView
    var img_save: ImageView
    lateinit var mediaSource: SimpleMediaSource //uri also supported

    init {
        itemView.apply {
            user_img = findViewById(R.id.user_img)
            username = findViewById(R.id.username)
            txt_likes = findViewById(R.id.txt_likes)
            txt_caption = findViewById(R.id.txt_caption)
            txt_tags = findViewById(R.id.txt_tags)
            txt_timePosted = findViewById(R.id.txt_timePosted)
            txt_commments = findViewById(R.id.txt_commments)
            option = findViewById(R.id.option)
            postImage = findViewById(R.id.postImage)
            postVideo = findViewById(R.id.postVideo)
            img_heart = findViewById(R.id.img_heart)
            img_heart_red = findViewById(R.id.img_heart_red)
            img_comments = findViewById(R.id.img_comments)
            img_send = findViewById(R.id.img_send)
            img_save = findViewById(R.id.img_save)
        }
    }

    fun setData(eachPost: PostModel) {
        val type = eachPost.mediaType
        if (type == "jpg" || type == "jpeg" || type == "png") {
            postVideo.visibility = View.GONE
            Glide.with(itemView.context).load(eachPost.img_url).into(postImage)
        } else if (type == "mp4" || type == "webp" || type == "mkv" || type == "3gp") {
            postImage.visibility = View.GONE
            mediaSource = SimpleMediaSource(eachPost.img_url) //uri also supported
            postVideo.play(mediaSource)
            postVideo.pause()
            postVideo.setOnClickListener {
                if (postVideo.player.isPlaying) {
                    postVideo.pause()
                } else {
                    postVideo.resume()
                }

            }
        } else {
            postImage.visibility = View.GONE
            postVideo.visibility = View.GONE
        }

        txt_likes.text = eachPost.likes.toString()
        txt_commments.text = eachPost.comments.toString()
        txt_caption.text = eachPost.caption
        txt_tags.text = eachPost.tags
        txt_timePosted.text = eachPost.dateTime
        username.text = eachPost.uPostId


        //room database work is needed
        FirebaseDatabase.getInstance().getReference("users").child(eachPost.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.child("")
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


    }

}