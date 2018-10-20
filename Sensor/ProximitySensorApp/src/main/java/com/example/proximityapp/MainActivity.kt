package com.example.proximityapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


/*
    근접 센서

    삼섬 단말기의 경우
        - 원: 8.0
        - 근: 0.0
 */
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
            var sensor = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            var chk = manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            if (chk == false) {
                textView.text = "센서 지원하지 않음."
            }
        }

        button2.setOnClickListener {
            manager.unregisterListener(listener)
        }
    }

    inner class SensorListener : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_PROXIMITY) {
                textView.text = "근접: ${event?.values[0]}"
            }

        }

    }

}