package com.example.httpclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            var th = HttpThread()
            th.start()
        }
    }


    inner class HttpThread: Thread() {
        override fun run() {
            var site = "https://www.google.com"
            var url = URL(site)
            var conn = url.openConnection()

            var input = conn.getInputStream()
            var isr = InputStreamReader(input, "UTF-8")
            var br = BufferedReader(isr)

            var str: String? = null
            var buf = StringBuffer()

            do {
                str = br.readLine()
                if(str != null) {
                    buf.append(str)
                }
            } while (str != null)


            runOnUiThread {
                textView.text = buf.toString()
            }
        }
    }
}
