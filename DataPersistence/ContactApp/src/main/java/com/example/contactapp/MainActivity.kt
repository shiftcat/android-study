package com.example.contactapp

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Toast
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*


fun createTextData(): List<ContactVO> {
    var contact1 = ContactVO(0, "홍길동", "010-1234-1111", "abc1@email.com")
    var contact2 = ContactVO(0, "김제동", "010-1234-1112", "abc2@email.com")
    var contact3 = ContactVO(0, "김한국", "010-1234-1113", "abc3@email.com")
    var contact4 = ContactVO(0, "이순신", "010-1234-1114", "abc4@email.com")
    var contact5 = ContactVO(0, "강감찬", "010-1234-1115", "abc5@email.com")
    return listOf(contact1, contact2, contact3, contact4, contact5)
}


class MainActivity : AppCompatActivity() {


    private val mRealm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    private val mContactDAO: ContactDAO by lazy {
        ContactDAO(mRealm)
    }

    private lateinit var mAdapterData: RealmResults<Contact>

    private lateinit var mAdapter: BaseAdapter


    private fun initTestData() {
        mContactDAO.deleteAll()
        createTextData().forEach {
            mContactDAO.insert(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapterData = mContactDAO.findAll()
        mAdapter = MyAdaptor(this, mAdapterData!!)

        mAdapterData.addChangeListener { _ ->
            mAdapter.notifyDataSetChanged()
            (mAdapter as MyAdaptor).resetCheckStats(false)
            var dataSize = mAdapterData.size
            if (dataSize == 0) {
                Snackbar.make(coordinator, "연락처 정보가 없습니다.", Snackbar.LENGTH_SHORT).show()
            }
        }

        listItems.adapter = mAdapter

        btnAddItem.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivityForResult(intent, 200)
        }

        listItems.setOnItemClickListener { parent, view, position, id ->
            goViewActiviy(position)
        }

        registerForContextMenu(listItems)
    }


    override fun onDestroy() {
        super.onDestroy()
        mRealm.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                200 -> {
                    var item = data?.getParcelableExtra<ContactVO>("contact")!!
                    // var contact = Contact(0, item.name, item.phone, item.email)
                    mContactDAO.insert(item)
                }
            }
        }
    }


    fun showDialLog() {
        var listener = object: DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        var myAdaptor = mAdapter as MyAdaptor
                        val checkedItemPos = myAdaptor.checkedItemPositions()
                        val res = checkedItemPos.map {
                            myAdaptor.getItem(it)
                        }
                        res?.forEach {
                            mContactDAO.delete(it!!.id)
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
                        .setMessage("선택된 연락처를 삭제 하시겠습니까?")
                        // builder.setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("확인", listener)
                        .setNegativeButton("취소", listener)

        builder.show()
    }



    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        // 사용자 선택하여 길게 누를 뷰의 객체
        when (v?.id) {
            R.id.listItems -> {
                menuInflater.inflate(R.menu.context_menu, menu)
            }
        }
        return super.onCreateContextMenu(menu, v, menuInfo)
    }



    override fun onContextItemSelected(item: MenuItem?): Boolean {
        var info = item!!.menuInfo as AdapterView.AdapterContextMenuInfo
        when (item?.itemId) {
            R.id.mnCall -> {
                var contact = mAdapterData[info.position]
                var uri = Uri.parse("tel:${contact?.phone}")
                var intent = Intent(Intent.ACTION_DIAL, uri)
                startActivity(intent)
            }
            R.id.mnMessage -> {
                var contact = mAdapterData[info.position]
                val uri = Uri.parse("smsto:${contact?.phone}")
                val it = Intent(Intent.ACTION_SENDTO, uri)
                it.putExtra("sms_body", "")
                startActivity(it)
            }
            R.id.mnEmail -> {
                var contact = mAdapterData[info.position]
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "plain/text"
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(contact?.email))
//                intent.putExtra(Intent.EXTRA_SUBJECT, "subject")
//                intent.putExtra(Intent.EXTRA_TEXT, "mail body")
                startActivity(Intent.createChooser(intent, ""))
            }
        }
        return super.onContextItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.del_option_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.btnDelete -> {
                var myAdaptor = mAdapter as MyAdaptor
                val checkedItemPos = myAdaptor.checkedItemPositions()
                if(checkedItemPos.size > 0) {
                    showDialLog()
                }
                else {
                    Toast.makeText(this, "선택된 연락처가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.btnRenew -> {
                initTestData()
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun goViewActiviy(position: Int) {
        var myAdaptor = mAdapter as MyAdaptor
        var item = myAdaptor.getItem(position)!!
        Log.d(javaClass.name, "Send contract: ${item.id}, ${item.name}, ${item.phone}, ${item.email}")
        var contactVO = ContactVO(item.id, item.name, item.phone, item.email)
        var intent = Intent(this, ViewActivity::class.java)
        intent.putExtra("contact", contactVO)
        startActivity(intent)
    }

}
