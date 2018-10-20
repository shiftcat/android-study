package com.example.toolbar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

/*
    ToolBar
    - 안드로이드에서 ActionBar를 보다 자유롭게 사용할 수 있도록 ToolBar라는 뷰를 제공하고 있다.
    - ToolBar를 이용해 탭 등 다양한 기능에 이용할 수 있도록 제공하고 있으며 기본적인 부분은 ActionBar와 동일하다.

    ActionBar 비활성
    - res/values>styles.xml에 다음 추가
        <item name="windowActionBar">false</item>
        <item name="windowNoTitle">true</item>
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 액션바 대신 툴바 사용
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.item1 -> textView.text = "메뉴1 클릭"
            R.id.item2 -> textView.text = "메뉴2 클릭"
            R.id.item3 -> textView.text = "메뉴3 클릭"
        }
        return super.onOptionsItemSelected(item)
    }
}
