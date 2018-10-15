package com.example.yhlee.activityex

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        Log.d("SecondActivity", "on create")

        btnBack.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d("SecondActivity", "on start")
    }

    override fun onResume() {
        super.onResume()
        Log.d("SecondActivity", "on resume")
    }


    override fun onRestart() {
        super.onRestart()
        Log.d("SecondActivity", "on restart")
    }


    override fun onPause() {
        super.onPause()
        Log.d("SecondActivity", "on pause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("SecondActivity", "on stop")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("SecondActivity", "on destory")
    }
}
