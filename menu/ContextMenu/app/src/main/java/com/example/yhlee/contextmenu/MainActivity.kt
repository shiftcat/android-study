package com.example.yhlee.contextmenu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


/*

    1. 화면에 배치된 뷰에 설정할 수 있는 메뉴
    2. 메뉴가 설정된 뷰를 길게 누르면 메뉴가 나타단다.
    3. 리스트에 보통 사용 됨.

    registerForContext
    1. 컨텍스트 메뉴를 등록하는 메서드
    2. 메서드의 매개 변수로 넣어준 뷰 객체에 메뉴가 설정된다.

    onCreateContextMenu
    1. 뷰를 길게 누르면 호출되는 메서드
    2. 여기에서 메뉴를 구성한다.

    onContextItemSelected
    1. 사용자가 메뉴를 선택했을 때 호출되는 메서드

 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerForContextMenu(textView)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        // 사용자 선택하여 길게 누를 뷰의 객체
        when(v?.id) {
            R.id.textView -> {
                    menu?.setHeaderTitle("텍스트뷰의 메뉴")
                    menuInflater.inflate(R.menu.textview_menu, menu)
                }
        }
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.textview_item1 -> textView.text = "텍스트뷰의 컨텍스트 메뉴1"
            R.id.textview_item2 -> textView.text = "텍스트뷰의 컨텍스트 메뉴2"
        }
        return super.onContextItemSelected(item)
    }
}
