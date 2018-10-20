package com.example.pendingintent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test2.*

class TestActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        var data1 = intent.getStringExtra("data1")
        var data2 = intent.getIntExtra("data2", 0)

        textView2.text = "데이터1: ${data1} -  데이터2: ${data2}"
    }
}
