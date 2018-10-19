package com.example.yhlee.towlinelistviewex

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SimpleAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var idx = Array(16, { it })
    var data1 = Array(16, { "데이터 ${it}" })
    var data2 = Array(16, { "Data ${it}" })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var list = idx
                .map {
                    var map = HashMap<String, String>()
                    map.put("str1", data1[it])
                    map.put("str2", data2[it])
                    map
                }

        var key = arrayOf("str1", "str2")
        var ids = intArrayOf(android.R.id.text1, android.R.id.text2)
        var adapter = SimpleAdapter(this, list, android.R.layout.simple_list_item_2, key, ids)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            textView.text = data1[position]
        }
    }
}
