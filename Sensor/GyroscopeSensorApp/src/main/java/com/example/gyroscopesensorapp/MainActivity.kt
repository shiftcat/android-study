package com.example.gyroscopesensorapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    val manager: SensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    val listener: SensorListener by lazy {
        SensorListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener {
            var sensor = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            var chk = manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            if(chk == false) {
                textView.text = "가속도 센서 지원하지 않음."
            }
        }

        button2.setOnClickListener {
            manager.unregisterListener(listener)
        }
    }


    inner class SensorListener: SensorEventListener
    {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent?) {
            if(event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
                textView.text = "X: ${event?.values[0]} \nY: ${event?.values[1]} \nZ: ${event?.values[2]}"
            }

        }

    }
}
