package com.example.yhlee.customlistviewex1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var data = Array(16, {i -> "데이터${i}"})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        var adapter = ArrayAdapter(this, R.layout.list_item_view, R.id.textView2, data)

        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            textView.text = data[position]
        }
    }
}
