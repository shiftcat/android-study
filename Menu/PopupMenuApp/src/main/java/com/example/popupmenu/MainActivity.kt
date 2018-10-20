package com.example.popupmenu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import kotlinx.android.synthetic.main.activity_main.*


/*
개발자 원하는 곳에 팝업 메뉴를 생성
 */
class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { view ->
            var pop = PopupMenu(this, textView)

            menuInflater.inflate(R.menu.popup_menu, pop.menu)
            var listener = PopupListener()
            pop.setOnMenuItemClickListener(listener)
            pop.show()
        }
    }


    // 람다식으로 가능하다.
    inner class PopupListener: PopupMenu.OnMenuItemClickListener
    {
        override fun onMenuItemClick(item: MenuItem?): Boolean
        {
            when(item?.itemId) {
                R.id.item1 -> textView.text = "메뉴1 선택"
                R.id.item2 -> textView.text = "메뉴2 선택"
                R.id.item3 -> textView.text = "메뉴3 선택"
            }
            return false
        }
    }
}
