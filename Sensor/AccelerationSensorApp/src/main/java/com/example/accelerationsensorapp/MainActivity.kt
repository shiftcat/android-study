package com.example.accelerationsensorapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    lateinit var manager: SensorManager

    lateinit var listener: SensorListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        listener = SensorListener()

        button1.setOnClickListener {
            var sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            var chk = manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            if(chk == false) {
                textView.text = "센서 지원하지 않음."
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
            if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                textView.text = "X: ${event?.values[0]} \nY: ${event?.values[1]} \nZ: ${event?.values[2]}"
            }

        }

    }
}
