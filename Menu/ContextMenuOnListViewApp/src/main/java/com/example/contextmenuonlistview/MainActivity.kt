package com.example.contextmenuonlistview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var data = arrayOf("리스트1", "리스트2", "리스트3", "리스트4", "리스트5", "리스트6")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        listView.adapter = adapter
        listView.setOnItemClickListener{adapterView, view, position, id ->
            textView.text = "${position} 번째 항목 선택했습니다."
        }

        registerForContextMenu(listView)

        registerForContextMenu(textView)

    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        // 사용자 선택하여 길게 누를 뷰의 객체
        when(v?.id) {
            R.id.textView -> {
                menu?.setHeaderTitle("텍스트뷰의 메뉴")
                menuInflater.inflate(R.menu.context_menu, menu)
            }
            R.id.listView -> {
                menu?.setHeaderTitle("리스트뷰의 메뉴")
                menuInflater.inflate(R.menu.listview_menu, menu)
                var info = menuInfo as AdapterView.AdapterContextMenuInfo
                if (info.position % 2 == 0) {
                    menu?.add(Menu.NONE, Menu.FIRST+100, Menu.NONE, "리스트뷰 메뉴3")
                }
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId) {
            R.id.textview_item1 -> textView.text = "텍스트뷰의 컨텍스트 메뉴1"
            R.id.textview_item2 -> textView.text = "텍스트뷰의 컨텍스트 메뉴2"

            R.id.listview_item1 -> {
                var info = item?.menuInfo as AdapterView.AdapterContextMenuInfo
                textView.text = "리스트뷰 ${info.position}번째의 "
                textView.append("컨텍스트 메뉴1")
            }
            R.id.listview_item2 -> {
                var info = item?.menuInfo as AdapterView.AdapterContextMenuInfo
                textView.text = "리스트뷰 ${info.position}번째의 "
                textView.append("컨텍스트 메뉴2")
            }
            Menu.FIRST+100 -> {
                var info = item?.menuInfo as AdapterView.AdapterContextMenuInfo
                textView.text = "리스트뷰 ${info.position}번째의 "
                textView.append("컨텍스트 메뉴3")
            }
        }
        return super.onContextItemSelected(item)
    }
}
