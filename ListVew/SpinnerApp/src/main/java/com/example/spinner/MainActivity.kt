package com.example.spinner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*


/*
    Spinner
    - 사용자에게 항목을 주고 선택하게 할 수 있는 AdapterView
    - 작은 스마트폰 화면을 효율적으로 사용할 수 있다는 장점을 가지고 있다.

 */
class MainActivity : AppCompatActivity() {

    var data1 = Array(16, {"데이터1 ${it}"})
    var data2 = Array(16, {"데이터2 ${it}"})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, data1)
        var adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, data2)

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner1.adapter = adapter1
        spinner2.adapter = adapter2

        var listener = SpinnerListener()
        spinner1.onItemSelectedListener = listener

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                textView2.text = data2[position]
            }
        }

        button.setOnClickListener {
            textView2.text = data1[spinner1.selectedItemPosition] + "\n"
            textView2.append(data2[spinner2.selectedItemPosition])
        }
    }


    inner class SpinnerListener: AdapterView.OnItemSelectedListener {
        // 선택한 아이템이 없을 때
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            textView2.text = data1[position]
        }

    }
}
