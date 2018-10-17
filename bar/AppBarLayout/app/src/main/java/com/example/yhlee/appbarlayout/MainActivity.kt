package com.example.yhlee.appbarlayout

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_main.*


/*
    Appbar Layout
    - Appbar Layout은 툴바를 포함하고 있는 뷰를 만들어 다양하게 사용할 수 있도록 제공되는 레이아웃이다.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 액션바 숨김
        supportActionBar?.hide()

        toolbar.title = "Appbar Layout"
        // 툴바 객체에 색상적용 불가. 상위 객체인 CollapsingToolbarLayout에 적용해야 한다.
        // toolbar.setTitleTextColor(Color.WHITE)

        // CollapsingToolbarLayout에 접었다, 펼쳤다할 때 색상 지정
        toolbarLayout.setCollapsedTitleTextColor(Color.CYAN)
        toolbarLayout.setExpandedTitleColor(Color.WHITE)
        toolbarLayout.collapsedTitleGravity = Gravity.CENTER_HORIZONTAL
        toolbarLayout.expandedTitleGravity = Gravity.RIGHT + Gravity.TOP
    }
}
