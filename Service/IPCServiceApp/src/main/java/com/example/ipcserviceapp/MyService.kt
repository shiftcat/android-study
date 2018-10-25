package com.example.ipcserviceapp

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import java.util.concurrent.Executors

class MyService : Service() {


    val binder: IBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning = true
        execute()
        return super.onStartCommand(intent, flags, startId)
    }


    private var value = 0

    private var listener: ValueChangeListener? = null

    private var isRunning = false

    private fun execute() {
        var executors = Executors.newSingleThreadExecutor()
        executors.execute {
            while(isRunning) {
                SystemClock.sleep(1000)
                Log.d("IPCService", "value: ${value}")
                listener?.onChange(value++)
            }
        }
        executors.shutdown()
    }


    fun getValue(): Int
    {
        return value
    }


    fun start()
    {
        isRunning = true
        execute()
    }


    fun stop()
    {
        isRunning = false
    }


    fun setListener(listener:ValueChangeListener) {
        this.listener = listener
    }

    /*
        람다함수를 이용할 때 함수 타입을 선언하고 그 타입에 맞는 람다 함수를 정의하여 대입해 이용할 수 있다.
        대입할 함수의 매개변수 개수와 타입, 그리고 반환값의 타입을 명시한다.
        (Int) -> Unit 은 Int 타입 매기변수 1개와 반환값이 없는 함수 타입을 의미한다.
     */
    fun setListener(listener: (Int) -> Unit) {
        this.listener = object: ValueChangeListener {
            override fun onChange(value: Int) {
                listener(value)
            }
        }
    }


    inner class LocalBinder: Binder()
    {
        fun getService(): MyService {
            return this@MyService
        }
    }
}
