package com.example.yhlee.customlistviewex2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SimpleAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    var data1 = Array(13, {i -> "국가 ${i}"})
    var data2 = Array(13, {i -> "국가 ${i}번째 국기"})

    override fun onCreate(savedInstanceState: Bundle?) {

        var idx = Array(14, {i -> i})
        var flags = idx.filter { i -> i > 0 }.map { i ->  resources.getIdentifier("flag_${i}", "drawable", packageName)}


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var items =
                idx.filter { it < idx.size-1 }
                .map {
                    var map = HashMap<String, Any>()
                    map.put("flag", flags[it])
                    map.put("data1", data1[it])
                    map.put("data2", data2[it])
                    map
                }
                .toList()

        var keys = arrayOf("flag", "data1", "data2")
        var ids = intArrayOf(R.id.imageView, R.id.textView2, R.id.textView3)

        val adapter = SimpleAdapter(this, items, R.layout.list_item_view, keys, ids)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            textView.text = data1[position]
        }
    }
}
