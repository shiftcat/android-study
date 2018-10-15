package com.example.yhlee.optionmenu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

/*
    xml에 의한 옵션메뉴 생
 */
//    // 옵션메뉴를 사용하려면 반드시 작성
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        // xml에 의한 메뉴 생성
//         menuInflater.inflate(R.menu.option_menu, menu)
//
//        // true 를 반환해야 메뉴가 보임.
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when(item?.itemId) {
//            R.id.menu1 -> textView.text = "메뉴1 클릭"
//            R.id.menu21 -> textView.text = "메뉴2의 서브1 클릭"
//            R.id.menu22 -> textView.text = "메뉴2의 서브2 클릭"
//            R.id.menu3 -> textView.text = "메뉴3 클릭"
//        }
//        return super.onOptionsItemSelected(item)
//    }


    /*
        코드에 의한 옵션메뉴 생성
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "코드메뉴1")
        menu?.add(Menu.NONE, Menu.FIRST + 2, Menu.NONE, "코드메뉴2")

        var sub:Menu? = menu?.addSubMenu("메뉴3")
        sub?.add(Menu.NONE, Menu.FIRST+3, Menu.NONE, "코드메뉴31")
        sub?.add(Menu.NONE, Menu.FIRST+4, Menu.NONE, "코드메뉴32")

        // true 를 반환해야 메뉴가 보임.
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            Menu.FIRST+1 -> textView.text = "메뉴1 클릭"
            Menu.FIRST+2 -> textView.text = "메뉴2 클릭"
            Menu.FIRST+3 -> textView.text = "메뉴3의 서브1 클릭"
            Menu.FIRST+4 -> textView.text = "메뉴3의 서브2 클릭"
        }
        return super.onOptionsItemSelected(item)
    }
}
