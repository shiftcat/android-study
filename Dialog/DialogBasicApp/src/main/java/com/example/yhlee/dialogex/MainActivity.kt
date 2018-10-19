package com.example.yhlee.dialogex

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

/*
    다이얼로그
    - 개발자가 필요할 때 사용자에게 메시지를 전달하는 용도로 사용하며, 다이얼로그가 나타나 있을 때는 주변의 뷰를 누를 수 없다.
    - 메시지 전달이나 입력 등의 용도로 사용한다.

    기본 다이얼로그
    - 3개의 버튼 배치

 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("기본 다이얼로그")
            builder.setMessage("다이얼로그 본문 입니다.")
            builder.setIcon(R.mipmap.ic_launcher)

            var listener = object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when(which) {
                        DialogInterface.BUTTON_POSITIVE ->
                            textView.text = "기본 다이얼로그:  Positive"

                        DialogInterface.BUTTON_NEUTRAL ->
                            textView.text = "기본 디이얼로그: Neutral"

                        DialogInterface.BUTTON_NEGATIVE ->
                            textView.text = "기본 디이얼로그: Negative"
                    }
                }

            }

            builder.setPositiveButton("Positive", listener)
            builder.setNeutralButton("Neutral", listener)
            builder.setNegativeButton("Negative", listener)

            builder.show()
        }
    }
}
