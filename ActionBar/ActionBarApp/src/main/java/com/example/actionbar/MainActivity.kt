package com.example.actionbar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

/*
    주의)
    SearchView 는 다음과 같이 2개가 있다.
    - android.widget.SearchView
    - android.support.v7.widget.SearchView
 */

import android.support.v7.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*

/*
    ActionBar
    - 화면 상단에 배치된 바 부분을 의미한다.
    - OptionMenu 항목의 일부를 배치할 수 있다.

    Icon: ActionBar에 표시되는 아이콘을 설정한다.
    showAsAction: ActionBar 배치 여부를 결정한다.
    actionViewClass:  접었다 폈다 할 때 나타날 뷰를 설정한다.

    showAsAction
    - Never(기본): 메뉴를 ActionBar에 절대 표시하지 않는다.
    - Always: 항상 ActionBar에 표시한다.
    - ifRoom: 공간이 허락된다면 ActionBar에 표시한다.
    - withText: title 속성의 문자열과 함께 표시된다.
    - collapseActionView: 접었다 펼첬다 하면서 뷰를 표시할 수 있다.

 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var search_item:MenuItem? = menu?.findItem(R.id.item5)
        var search_view:SearchView = search_item?.actionView as SearchView
        search_view.queryHint = "검색어를 입력하세요."

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            // 검색어 입력할때마다 반응하는 리스너
            override fun onQueryTextChange(newText: String?): Boolean {
                textView2.text = newText
                return true
            }
            // 검색어 입력이 완료된 후 반응하는 리스너
            override fun onQueryTextSubmit(query: String?): Boolean {
                textView2.text = query
                // true를 반환하면 키보드가 사라지지 않는다.
                return false
            }
        })
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.item1 -> textView.text = "메뉴1 클릭"
            R.id.item2 -> textView.text = "메뉴2 클릭"
            R.id.item3 -> textView.text = "메뉴3 클릭"
            R.id.item4 -> textView.text = "메뉴4 클릭"
        }
        return super.onOptionsItemSelected(item)
    }
}
