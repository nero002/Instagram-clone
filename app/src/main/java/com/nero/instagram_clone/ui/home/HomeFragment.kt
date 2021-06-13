package com.nero.instagram_clone.ui.home

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nero.instagram_clone.R
import com.nero.instagram_clone.adapters.HomePostAdapter
import com.nero.instagram_clone.models.PostModel
import com.nero.instagram_clone.models.UserModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var swipeListener: SwipeListener
    lateinit var llContainer: ConstraintLayout
    lateinit var navController: NavController
    lateinit var rvHomePosts: RecyclerView

    var postList: ArrayList<PostModel> = ArrayList()
    val postAdapter = HomePostAdapter(postList)

    class SwipeListener : View.OnTouchListener {

        var gestureDetector: GestureDetector

        constructor(navController: NavController, view: View) {
            val threshold = 300
            val velocity_threshold = 200

            val l: GestureDetector.SimpleOnGestureListener =
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDown(e: MotionEvent?): Boolean {
                        return true
                    }

                    override fun onFling(
                        e1: MotionEvent?,
                        e2: MotionEvent?,
                        velocityX: Float,
                        velocityY: Float
                    ): Boolean {

                        val xDiff = e2!!.x - e1!!.x
                        val yDiff = e2!!.y - e1!!.y

                        try {
                            if (abs(xDiff) > abs(yDiff)) {
                                if (abs(xDiff) > threshold && abs(velocityX) > velocity_threshold) {
                                    if (xDiff > 0) {
                                        navController.navigate(HomeFragmentDirections.actionNavigationHomeToNavigationCamera())
                                    } else {
                                        //left swipe
                                    }
                                    return true
                                } else {
                                    if (abs(yDiff) > threshold && abs(velocityY) > velocity_threshold) {
                                        if (yDiff > 0) {
                                            //down swipe
                                        } else {
                                            //up swipe
                                        }
                                        return true
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        return false;
                    }
                }
            gestureDetector = GestureDetector(l)
            view.setOnTouchListener(this)

        }


        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            return gestureDetector.onTouchEvent(event)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvHomePosts = view.findViewById(R.id.rvHomePosts)
        rvHomePosts.layoutManager = LinearLayoutManager(view.context)

        startFetchingPosts()


        llContainer = view.findViewById(R.id.llContainer)

        navController = findNavController()
        swipeListener = SwipeListener(navController, rvHomePosts)

        this.getView()?.setFocusableInTouchMode(true);
        this.getView()?.requestFocus();
        view.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                Log.d("TAG", "onKey: " + keyCode)
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton(
                            "Yes"
                        ) { dialog, id -> activity?.finish() }
                        .setNegativeButton("No", null)
                        .show()
                    return true
                }
                return false
            }
        })
    }

    private fun startFetchingPosts() {
        val dbRef = FirebaseDatabase.getInstance().getReference("posts")
        postList.clear()
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {

                    val postModel = PostModel(
                        it.child("userId").value.toString(),
                        it.child("upostId").value.toString(),
                        it.child("caption").value.toString(),
                        it.child("tags").value.toString(),
                        it.child("img_url").value.toString(),
                        it.child("dateTime").value.toString(),
                        it.child("isGlobal").value.toString().toBoolean(),
                        it.child("mediaType").value.toString(),
                        it.child("likes").value.toString().toLong(),
                        it.child("comments").value.toString().toLong(),
                        it.child("shares").value.toString().toLong()
                    )

                    postList.add(postModel)
                    Log.d("TAG", "onDataChange: $postModel")
                }
                rvHomePosts.adapter = postAdapter
                postAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: ${error.message}")
            }

        })
    }
}