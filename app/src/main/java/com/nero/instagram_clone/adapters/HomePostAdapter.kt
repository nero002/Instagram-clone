package com.nero.instagram_clone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nero.instagram_clone.R
import com.nero.instagram_clone.models.PostModel
import com.nero.instagram_clone.viewholders.HomePostViewHolder

class HomePostAdapter(val postList: ArrayList<PostModel>) :
    RecyclerView.Adapter<HomePostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePostViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.each_post_item_layout, parent, false)
        return HomePostViewHolder(v)
    }

    override fun onBindViewHolder(holder: HomePostViewHolder, position: Int) {
        val eachPost = postList[position]
        holder.setData(eachPost)
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}