package com.example.yhlee.customdialogex

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*

/*
    커스텀 다이얼로그
    - 기본 다이얼로그에 View를 설정하면 디이얼로그에 표시되는 모양을 자유롭게 구성할 수 있다.

 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("커스텀 다이얼로그")
            builder.setMessage("다이얼로그 본문 입니다.")
            builder.setIcon(R.mipmap.ic_launcher)

            var v1 = layoutInflater.inflate(R.layout.customdialog, null)
            builder.setView(v1)

            var listener = object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    var alert = dialog as AlertDialog
                    var edit1: EditText? = alert.findViewById<EditText>(R.id.editText1)
                    var edit2: EditText? = alert.findViewById<EditText>(R.id.editText2)

                    textView.text = "Text1: ${edit1?.text} | Text2: ${edit2?.text}"
                }

            }

            builder.setPositiveButton("확인", listener)
            builder.setNegativeButton("취소", listener)

            builder.show()
        }
    }
}
