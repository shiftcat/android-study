package com.example.yhlee.activityex

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var cal: Calendar = GregorianCalendar(Locale.KOREA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "on create")

        buttonClick()

    }

    fun buttonClick() {
        btnNext.setOnClickListener {
            val intent: Intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "on start")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "on resume")
    }


    override fun onRestart() {
        super.onRestart()
        Log.d("MainActivity", "on restart")
    }


    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "on pause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "on stop")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "on destory")
    }
}
