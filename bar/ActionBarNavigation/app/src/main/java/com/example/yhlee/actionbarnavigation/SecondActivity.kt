package com.example.yhlee.actionbarnavigation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        var action = supportActionBar
        action?.setHomeButtonEnabled(true)
        action?.setDisplayHomeAsUpEnabled(true)
        // 기본 아이콘에서 사용자 지정 아이콘으로 변경
        action?.setHomeAsUpIndicator(android.R.drawable.ic_menu_directions)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            // 안드로이드에서 home 이라는 id로 설정 되어 있다.
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
