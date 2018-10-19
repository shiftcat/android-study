package com.example.yhlee.sendobject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {view ->
            var intent = Intent(this, InputActivity::class.java)
            startActivityForResult(intent, 10)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            var user = data?.getParcelableExtra<UserInfo>("userinfo")
            textView.text = "User name: ${user?.name}, email: ${user?.email}, age: ${user?.age}"
        }
    }
}
