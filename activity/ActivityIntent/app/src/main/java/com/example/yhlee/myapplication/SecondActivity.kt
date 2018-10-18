package com.example.yhlee.myapplication

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_second.*


/*
    ActionBar Navigation
    - ActionBar의 좌측에 버튼을 활성화 하여 이전 하면으로 돌아가는 기능을 구현할 수 있다.
 */
class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        var action = supportActionBar
        action?.setHomeButtonEnabled(true)
        action?.setDisplayHomeAsUpEnabled(true)
        // 기본 아이콘에서 사용자 지정 아이콘으로 변경
        action?.setHomeAsUpIndicator(android.R.drawable.ic_media_previous)

        val data1 = intent.getStringExtra("data1")
        val data2 = intent.getIntExtra("data2", 0)
        textView2.text = "data1: ${data1} - data2: ${data2}"
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            // 안드로이드에서 home 이라는 id로 설정 되어 있다.
            android.R.id.home -> {
                intent.putExtra("result", "RESULT OK")
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
