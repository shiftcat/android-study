package com.example.yhlee.listdialog

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.SimpleAdapter
import kotlinx.android.synthetic.main.activity_main.*

/*
    리스트 다이얼로그
    - 다아얼로그에 리스트뷰를 표시할 수 있는 다이얼로그
    - 다이얼로그는 버튼을 총 3개까지 배치할 수 있는데 그 이상이 필요할 경우 리스트 다이얼로그를 사용하면 된다.

 */
class MainActivity : AppCompatActivity() {

    var data1 = Array(7, {"항목${it}"})
    var data2 = Array(7, {"국가${it}"})


    fun makeItems(): List<HashMap<String, Any>>
    {
        var idx = Array(8, {it})
        var flags = idx.filter { i -> i > 0 }.map { i ->  resources.getIdentifier("flag_${i}", "drawable", packageName)}

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

        return items
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener { view ->
            var listener = object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    textView.text = "${data1[which]} 선택"
                }
            }
            var builder = AlertDialog.Builder(this)
                .setTitle("리스트 다이얼로그")
                .setNegativeButton("취소", null)
                .setItems(data1, listener)

            builder.show()
        }

        var items = makeItems()

        var keys = arrayOf("flag", "data1", "data2")
        var ids = intArrayOf(R.id.imageView, R.id.textView2, R.id.textView3)

        val adapter = SimpleAdapter(this, items, R.layout.custom_dialog, keys, ids)


        button2.setOnClickListener {
            var listener = object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    textView.text = "${data2[which]} 선택"
                }

            }
            var builder = AlertDialog.Builder(this)
                .setTitle("커스텀 다이얼로그")
                .setAdapter(adapter, listener)
                .setNegativeButton("취소", null)

            builder.show()
        }
    }
}
