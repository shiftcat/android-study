package com.example.yhlee.customactionbar

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


/*
    ActionBar는 개발자가 자유롭게 구성할 수 있다.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 기본으로 제공되는 기존의 액션바 요소들 비활성
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        // 사용자 정의 액션바 활성
        supportActionBar?.setDisplayShowCustomEnabled(true)

        var actionView = layoutInflater.inflate(R.layout.custom_actionbar, null)
        supportActionBar?.customView = actionView

        var actionText = actionView.findViewById<TextView>(R.id.textView)
        actionText.text = "커스텀 액션바"
        actionText.setTextColor(Color.BLUE)

        var actionButton = actionView.findViewById<Button>(R.id.button)
        actionButton.text = "액션버튼"
        actionButton.setOnClickListener {
            Toast.makeText(this, "액션 버튼을 클릭했습니다.", Toast.LENGTH_SHORT).show()
        }

        return true
    }
}
