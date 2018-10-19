package com.example.yhlee.appbarlayout2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 액션바 숨김
        supportActionBar?.hide()
        
        app_bar_image.setImageResource(R.drawable.code)
    }
}
