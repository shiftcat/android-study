package com.example.contactconsumerapp

import android.content.ContentValues
import android.content.DialogInterface
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    val mListner: DialogListener by lazy {
        DialogListener()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        buttonInput.setOnClickListener {
            showInputDialog(mListner)
        }


        button.setOnClickListener {
            // 콘텐츠 프로바이더를 통해 데이터 수신
            val uri = Uri.parse("content://com.example.contact.provider")
            var cursor = contentResolver.query(uri, null, null, null, null)

            textView.text = ""
            while(cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val phone = cursor.getString(cursor.getColumnIndex("phone"))
                val email = cursor.getString(cursor.getColumnIndex("email"))

                textView.append("${id}, ${name}, ${phone}, ${email}\n")
            }
        }

    }


    inner class DialogListener: DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            val alert = dialog as AlertDialog
            val editTextName: EditText = alert.findViewById<EditText>(R.id.editTextName)!!
            val editTextPhone: EditText = alert.findViewById<EditText>(R.id.editTextPhone)!!
            val editTextEmail: EditText = alert.findViewById<EditText>(R.id.editTextEmail)!!

            val uri = Uri.parse("content://com.example.contact.provider")

            val contentValues = ContentValues()
            contentValues.put("name", editTextName.text.toString())
            contentValues.put("phone", editTextPhone.text.toString())
            contentValues.put("email", editTextEmail.text.toString())

            // 콘텐츠 프로바이더를 통해 신규 데이터 전송
            contentResolver.insert(uri, contentValues)
        }
    }


    private fun showInputDialog(listener: DialogInterface.OnClickListener) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("연락처 입력")
        builder.setMessage("신규 연럭처를 입력하세요.")
        builder.setIcon(R.mipmap.ic_launcher)
        val view = layoutInflater.inflate(R.layout.new_contact_input, null)
        builder.setView(view)
        builder.setPositiveButton("확인", listener)
        builder.setNegativeButton("취소", null)
        builder.show()
    }
}
