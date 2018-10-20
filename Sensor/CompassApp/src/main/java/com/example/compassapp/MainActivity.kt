package com.example.compassapp

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


    var accValue: FloatArray? = null

    var magValue: FloatArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener {
            var sensor1 = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            var sensor2 = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

            manager.registerListener(listener, sensor1, SensorManager.SENSOR_DELAY_NORMAL)
            manager.registerListener(listener, sensor2, SensorManager.SENSOR_DELAY_NORMAL)
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
            when(event?.sensor?.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    accValue = event?.values?.clone()
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    magValue = event?.values?.clone()
                }
            }

            if(accValue != null && magValue != null) {
                var R = FloatArray(9)
                var I = FloatArray(9)

                SensorManager.getRotationMatrix(R, I, accValue, magValue)

                var values = FloatArray(3)
                SensorManager.getOrientation(R, values)

                var azimuth = raidanToDegree(values[0])
                var pitch = raidanToDegree(values[1])
                var roll = raidanToDegree(values[2])

                textView.text = "azimuth: ${azimuth} \npitch: ${pitch} \nroll: ${roll}"
            }
        }

    }


    fun raidanToDegree(radian: Float): Float {
        return radian * 180 / Math.PI.toFloat()
    }
}
