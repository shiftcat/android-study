package com.example.activitybasic

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "on create")
        textView.append("onCreate \n")

        button.setOnClickListener {
            var intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        textView.append("onStart \n")
        Log.d("MainActivity", "on start")
    }

    override fun onResume() {
        super.onResume()
        textView.append("onResume \n")
        Log.d("MainActivity", "on resume")
    }


    override fun onRestart() {
        super.onRestart()
        textView.append("onRestart \n")
        Log.d("MainActivity", "on restart")
    }


    override fun onPause() {
        super.onPause()
        textView.append("onPause \n")
        Log.d("MainActivity", "on pause")
    }

    override fun onStop() {
        super.onStop()
        textView.append("onStop \n")
        Log.d("MainActivity", "on stop")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "on destory")
    }
}
