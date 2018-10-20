package com.example.customadapter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*


/*
    1. 리스트뷰를 사용할 때 특별한 기능을 추가해서 만들고 싶다면 Adapter 클래스를 직접 만들어 사용하면 된다.
    2. Adapter 클래스를 만들때는 BaseAdapter 클래스를 상송받아 작성한다.

    오버라아드 메서드
    - getCount: 리스트뷰 내의 전체 항목의 개수를 반환한다.
    - getView: 리스트뷰 항목 하나를 구성하여 반환한다.

 */
class MainActivity : AppCompatActivity() {

    var data = Array(16, { "데이터 ${it}" })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var adapter = ArrayAdapter<String>(this, R.layout.list_item_view, R.id.txtListItem, data)

        var adapter = ListAdapter()
        listView.adapter = adapter


    }


    inner class ListAdapter : BaseAdapter() {
        var listener = BtnListener()

        // 리스트뷰 하나의 항목에 대한 뷰
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var itemView: View? = null

            if (convertView == null) {
                itemView = layoutInflater.inflate(R.layout.list_item_view, null)
            } else {
                itemView = convertView
            }
            var text: TextView? = itemView?.findViewById<TextView>(R.id.txtListItem)
            text?.text = data[position]

            var button1: Button? = itemView?.findViewById<Button>(R.id.btnList1)
            var button2: Button? = itemView?.findViewById<Button>(R.id.btnList2)
            button1?.setOnClickListener(listener)
            button2?.setOnClickListener(listener)

            button1?.tag = position
            button2?.tag = position

            return itemView
        }

        override fun getItem(position: Int): Any? {
            return null
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return data.size
        }

    }


    inner class BtnListener : View.OnClickListener {
        override fun onClick(v: View?) {
            var pos = v?.getTag() as Int
            when (v?.id) {
                R.id.btnList1 -> {
                    textView.text = "첫 번째 버튼 클릭"
                    textView.append(" - ${pos} 항목")
                }

                R.id.btnList2 -> {
                    textView.text = "두 번째 버튼 클릭"
                    textView.append(" - ${pos} 항목")
                }
            }
        }

    }
}
