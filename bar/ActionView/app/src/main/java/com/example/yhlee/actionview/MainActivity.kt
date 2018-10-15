package com.example.yhlee.actionview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

/*
    ActionView
    - ActionBar에 뷰를 배치하고 이를 접었다 폈다 할 수 있는 개념이다.
    - 주고 검색 기능을 만들 때 사용한다.

 */
class MainActivity : AppCompatActivity() {


    var data1 = arrayOf("대한민국", "중국", "미국", "스페인", "영국", "인도네시아", "네팔", "일본", "캐나다", "몽고", "러시아", "호주", "아르헨티나", "브라질", "독일", "캐냐", "가나")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data1)
        listView.adapter = adapter
        listView.isTextFilterEnabled = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var item = menu?.findItem(R.id.item1)
        var listener = ActionListener()
        item?.setOnActionExpandListener(listener)

        var searchView = item?.actionView as SearchView
        searchView?.queryHint = "검색어를 입력하세요."

        var listener2 = ActionListener2()
        searchView?.setOnQueryTextListener(listener2)
        return true
    }


    inner class ActionListener: MenuItem.OnActionExpandListener {
        override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
            textView.text = "액션뷰 펼침"
            return true
        }

        override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
            textView.text = "액션뷰 접힘"
            listView.clearTextFilter()
            return true
        }

    }


    inner class ActionListener2: SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            textView2.text = query
            listView.setFilterText(query)
            if(query?.length == 0) {
                listView.clearTextFilter()
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            textView2.text = newText
            listView.setFilterText(newText)
            if(newText?.length == 0) {
                listView.clearTextFilter()
            }
            return true
        }

    }
}
