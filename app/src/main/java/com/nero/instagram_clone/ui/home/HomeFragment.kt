package com.nero.instagram_clone.ui.home

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nero.instagram_clone.R
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {


    lateinit var swipeListener: SwipeListener
    lateinit var llContainer: ConstraintLayout
    public lateinit var navController: NavController

    class SwipeListener : View.OnTouchListener {

        lateinit var gestureDetector: GestureDetector

        constructor(navController: NavController, view: View) {
            val threshold = 100
            val velocity_threshold = 100

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

        llContainer = view.findViewById(R.id.llContainer)

        navController = findNavController()
        swipeListener = SwipeListener(navController, llContainer);

        view.findViewById<Button>(R.id.openCamera).setOnClickListener {

        }
    }

}