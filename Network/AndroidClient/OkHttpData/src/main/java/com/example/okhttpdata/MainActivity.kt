package com.example.okhttpdata

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener {
            var th = GetRequest()
            th.start()
        }

        button2.setOnClickListener {
            var th = PostRequest()
            th.start()
        }
    }


    inner class GetRequest : Thread() {
        override fun run() {
            var request =
                Request.Builder()
                    .url("http://192.168.100.114:8080/service1?param1=Hi+Android")
                    .get()
                    .build()

            OkHttpClient()
                .newCall(request)
                .enqueue(MyCallBack())
        }
    }


    inner class PostRequest: Thread() {
        override fun run() {
            var json = JSONObject()
            json.put("param1", "Hi!")
            json.put("param2", "Hello")

            var jsonType = MediaType.parse("application/json; charset=utf-8")

            var requestBody = RequestBody.create(jsonType, json.toString())

            var request =
                Request.Builder()
                    .url("http://192.168.100.114:8080/service2")
                    .post(requestBody)
                    .build()

            OkHttpClient()
                .newCall(request)
                .enqueue(MyCallBack())

        }
    }


    inner class MyCallBack : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e(javaClass.name, e.message)
            runOnUiThread {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

        override fun onResponse(call: Call, response: Response) {
            var result = response.body()?.string()
            var json = JSONObject(result)
            var data1= json.getString("data1")
            var data2= json.getInt("data2")
            var data3 = json.getDouble("data3")

            runOnUiThread {
                textView.text = "data1: ${data1}, data2 = ${data2}, data3 = ${data3}"
            }
        }

    }

}
