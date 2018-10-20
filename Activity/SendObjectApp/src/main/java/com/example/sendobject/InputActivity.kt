package com.example.sendobject

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_input.*
import java.lang.Integer.parseInt

class InputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        btnSend.setOnClickListener {
            var name = editName.text.toString()
            var email = editEmail.text.toString()
            var age = parseInt( editAge.text.toString() )

            var userInfo = UserInfo(name, email, age)

            var intent2 = Intent()
            intent2.putExtra("userinfo", userInfo)

            setResult(Activity.RESULT_OK, intent2)
            finish()
        }
    }
}
