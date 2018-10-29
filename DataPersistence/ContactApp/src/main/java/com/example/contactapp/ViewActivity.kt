package com.example.contactapp

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_view.*

class ViewActivity : AppCompatActivity() {


    private val mRealm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    private val mContactDAO: ContactDAO by lazy {
        ContactDAO(mRealm)
    }


    private val mItem: ContactVO by lazy {
        intent.getParcelableExtra<ContactVO>("contact")
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        var action = supportActionBar
        action?.setHomeButtonEnabled(true)
        action?.setDisplayHomeAsUpEnabled(true)

        app_bar_image.setImageResource(R.drawable.user)

        toolbar.title = mItem.name

        textName.text = mItem.name
        textPhone.text = mItem.phone
        textEmail.text = mItem.email

        btnEdit.setOnClickListener {
            Log.d(javaClass.name, "Edit contact id: ${mItem.id}")
            var intent = Intent(this, EditActivity::class.java)
            intent.putExtra("contactId", mItem.id)
            startActivityForResult(intent, 200)
        }

        btnCall.setOnClickListener {
            var uri = Uri.parse("tel:${mItem.phone}")
            var intent = Intent(Intent.ACTION_DIAL, uri)
            startActivity(intent)
        }

        btnMessage.setOnClickListener {
            val uri = Uri.parse("smsto:${mItem.phone}")
            val it = Intent(Intent.ACTION_SENDTO, uri)
            it.putExtra("sms_body", "")
            startActivity(it)
        }

        btnEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mItem.email))
//                intent.putExtra(Intent.EXTRA_SUBJECT, "subject")
//                intent.putExtra(Intent.EXTRA_TEXT, "mail body")
            startActivity(Intent.createChooser(intent, ""))
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                200 -> {
                    var item = data?.getParcelableExtra<ContactVO>("contact")!!
                    var contact = Contact(item.id, item.name, item.phone, item.email)
                    if(mContactDAO.update(contact)) {
                        textName.text = item.name
                        textPhone.text = item.phone
                        textEmail.text = item.email
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_option_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.mnDelete -> {
                showDialLog()
            }

            R.id.mnEdit -> {
                Log.d(javaClass.name, "Edit contact id: ${mItem.id}")
                var intent = Intent(this, EditActivity::class.java)
                intent.putExtra("contactId", mItem.id)
                startActivityForResult(intent, 200)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun showDialLog() {
        var listener = object: DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if(mContactDAO.delete(mItem.id)) {
                            finish()
                        }
                    }

                    DialogInterface.BUTTON_NEGATIVE -> {
                        // pass
                    }
                }
            }
        }

        var builder =
            AlertDialog.Builder(this)
                    .setTitle("연락처 삭제")
                    .setMessage("이 연락처를 삭제 하시겠습니까?")
                    // builder.setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("확인", listener)
                    .setNegativeButton("취소", listener)

        builder.show()
    }


}
