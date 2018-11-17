package com.example.androidclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.Exception
import java.net.Socket

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            var th = NetworkThread()
            th.start()
        }
    }


    inner class NetworkThread: Thread() {
        override fun run() {
            var socket: Socket? = null
            try {
                socket = Socket("192.168.100.114", 3333)

                var input = socket.getInputStream()
                var dis = DataInputStream(input)

                var output = socket.getOutputStream()
                var dos = DataOutputStream(output)

                var data1 = dis.readInt()
                var data2 = dis.readDouble()
                var data3 = dis.readUTF()

                dos.writeInt(400)
                dos.writeDouble(999.999)
                dos.writeUTF("클라이언트가 보낸 문자열")

                runOnUiThread {
                    textView.append("data1 : ${data1}\n")
                    textView.append("data2 : ${data2}\n")
                    textView.append("data3 : ${data3}\n")
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                if(socket != null) {
                    try {
                        socket.close()
                    }
                    catch (e: Exception) {}
                }
            }
        }
    }
}
