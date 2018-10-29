package com.example.contactapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {


    private val mRealm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    private val mContactDAO: ContactDAO by lazy {
        ContactDAO(mRealm)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        var action = supportActionBar
        action?.setHomeButtonEnabled(true)
        action?.setDisplayHomeAsUpEnabled(true)

        val contactId = intent.getLongExtra("contactId", -1)
        Log.d(javaClass.name, "Extra value contactId: ${contactId}")
        if(contactId > -1) {
            val contact =  mContactDAO.findOne(contactId)
            editName.setText(contact?.name, TextView.BufferType.EDITABLE)
            editPhone.setText(contact?.phone, TextView.BufferType.EDITABLE)
            editEmail.setText(contact?.email, TextView.BufferType.EDITABLE)
        }


        btnOk.setOnClickListener {
            var intent = Intent()
            var newContact = ContactVO()
            newContact.id = 0
            newContact.name = editName.text.toString()
            newContact.phone = editPhone.text.toString()
            newContact.email = editEmail.text.toString()
            intent.putExtra("contact", newContact)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
