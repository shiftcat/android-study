package com.example.okhttpclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            var th = MyThread()
            th.start()
        }
    }

    inner class MyThread : Thread() {
        override fun run() {
            var request =
                Request.Builder()
                    .url("https://www.google.com")
                    .build()

            OkHttpClient()
                .newCall(request)
                .enqueue(MyCallBack())
        }
    }


    inner class MyCallBack : Callback {
        override fun onFailure(call: Call, e: IOException) {

        }

        override fun onResponse(call: Call, response: Response) {
            runOnUiThread {
                textView.text = response.body()?.string()
            }
        }

    }
}
