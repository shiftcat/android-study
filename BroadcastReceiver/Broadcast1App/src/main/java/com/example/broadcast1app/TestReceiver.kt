package com.example.broadcast1app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class TestReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        var data1 = intent.getIntExtra("data1", 0)
        var data2 = intent.getDoubleExtra("data2", 0.0)
        var str = "data1: ${data1}, data2: ${data2}"
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }
}
