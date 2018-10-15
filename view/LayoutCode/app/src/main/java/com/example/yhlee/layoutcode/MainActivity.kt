package com.example.yhlee.layoutcode

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val linear : LinearLayout = LinearLayout(this)

        val btn1 : Button = Button(this)
        btn1.text = "버튼1"

        val btn2 : Button = Button(this)
        btn2.text = "버튼2"

        linear.addView(btn1)
        linear.addView(btn2)

        setContentView(linear)
    }
}
