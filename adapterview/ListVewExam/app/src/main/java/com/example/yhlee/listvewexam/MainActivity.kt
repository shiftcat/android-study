package com.example.yhlee.listvewexam

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*


/*
    AdapterView
    - 뷰를 구성하가 위해 개발자가 코드를 통해 결정해 줘야 하는 항목이 있는 뷰를 통칭해서 AdapterView 라고 부른다.
    - 다향한 항목을 제공하는 뷰에서 사용한다.

    ListView
    - 여러 항목들을 제공하고 위 아래로 스크롤하여 항목을 보여주는 뷰이다.

    adapter: 리스트뷰를 구성하기 위한 어뎁터 객체를 설정한다.

 */
class MainActivity : AppCompatActivity() {

    var data: Array<String> = Array(16, {i -> "리스트${i}"})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            textView.text = data[position]
        }

//        listView.setOnItemClickListener(ListListener())
    }

//    inner class ListListener: AdapterView.OnItemClickListener {
//        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            textView.text = data[position]
//        }
//
//    }
}
