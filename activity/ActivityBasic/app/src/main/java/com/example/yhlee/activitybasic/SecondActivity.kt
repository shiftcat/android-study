package com.example.yhlee.activitybasic

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        finish()
        return true
    }
}
