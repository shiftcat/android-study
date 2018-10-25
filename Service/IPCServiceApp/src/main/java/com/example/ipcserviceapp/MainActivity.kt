package com.example.ipcserviceapp

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


/*
    IPC
    - Activity에서 실행 중인 서비스의 데이터를 사용하고자 할 때 사용하는 개념
    - 현재 실행중인 서비스에 접속하고 서비스가 가지고 있는 메서드를 호출함으로써 값을 가져와 사용할 수 있다.

 */
class MainActivity : AppCompatActivity() {


    var ipcService: MyService? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("IPCService", "MainActivity onCreate()")
        val intent = Intent(this, MyService::class.java)
        if(!isServiceRunning("com.example.ipcserviceapp.MyService")) {
            startService(intent)
        }

        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)

        button1.setOnClickListener {
//            var value = ipcService?.getValue()
//            textView.text = "서비스 값: ${value}"
            ipcService?.start()
        }

        button2.setOnClickListener {
            ipcService?.stop()
        }
    }


    override fun onDestroy() {
        Log.d("IPCService", "MainActivity onDestroy()")
        super.onDestroy()
        ipcService?.stop()
        unbindService(mConnection)
    }


    fun isServiceRunning(name: String):Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        var res = manager.getRunningServices(Int.MAX_VALUE)
            .map {
                Log.d("IPCService", "ServiceClass: ${it.service.className}")
                it.service.className.equals(name)
            }
            .find { it }

        return res ?: false
    }


    private val mConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("IPCService", "ServiceConnection onServiceDisconnected()")
            ipcService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("IPCService", "ServiceConnection onServiceConnected()")
            val binder = service as MyService.LocalBinder
            ipcService = binder.getService()
            ipcService?.setListener{
                textView.text = "서비스 값: ${it}"
            }
        }

    }
}
