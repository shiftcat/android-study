package com.example.yhlee.datepickerex

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            var cal = Calendar.getInstance()
            var year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            var day = cal.get(Calendar.DAY_OF_MONTH)

            var listener = object: DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    textView.text = "${year}년 ${month+1}월 ${dayOfMonth}일"
                }

            }

            var picker = DatePickerDialog(this, listener, year, month, day)
            picker.show()
        }

        button2.setOnClickListener {
            var cal = Calendar.getInstance()
            var hour = cal.get(Calendar.HOUR_OF_DAY)
            var minute = cal.get(Calendar.MINUTE)

            var listener = object: TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    textView.text = "${hourOfDay}시 ${minute}분"
                }
            }

            var timePicker = TimePickerDialog(this, listener, hour, minute, false)
            timePicker.show()
        }
    }
}
