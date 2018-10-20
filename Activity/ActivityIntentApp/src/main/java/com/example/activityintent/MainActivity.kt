package com.example.activityintent

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            var intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("data1", "Value1")
            intent.putExtra("data2", 100)
            // startActivity(intent)
            startActivityForResult(intent, 10)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            textResult.text = data?.getStringExtra("result")
        }
        else {
            textResult.text = "결과 없음."
        }
    }
}
