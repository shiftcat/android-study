package com.example.yhlee.appbarlayout3

import android.content.DialogInterface
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.input_layout.*

class MainActivity : AppCompatActivity() {


    var data1 = Array<String>(16, {"데이터 ${it}"}).toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        app_bar_image.setImageResource(R.drawable.code)

        toolbar.title = "Appbar Layout"
        toolbarLayout.setCollapsedTitleTextColor(Color.BLACK)
        toolbarLayout.setExpandedTitleColor(Color.WHITE)

        // NestedScrollView 안에 ListView 가 스크롤될 수 있도록 한다.
        ViewCompat.setNestedScrollingEnabled(list1, true)

        list1.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data1)

        btnFloat.setOnClickListener {
            var v1 = layoutInflater.inflate(R.layout.input_layout, null)

            var builder =
                AlertDialog.Builder(this)
                    .setTitle("문자열 입력")
                    .setView(v1)
                    .setNegativeButton("취소", null)
                    .setPositiveButton("확인", DialogListener())
                    .show()
        }
    }


    inner class DialogListener: DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            var alert = dialog as AlertDialog
            var str1 = alert.editText.text.toString()

            data1.add(str1)

            var adapter = list1.adapter as ArrayAdapter<String>
            adapter.notifyDataSetChanged()
        }

    }
}
