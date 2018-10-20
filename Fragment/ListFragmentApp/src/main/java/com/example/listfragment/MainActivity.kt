package com.example.listfragment

import android.support.v7.app.AppCompatActivity
import android.os.Bundle


/*
    List Fragment
    - Fragment 내에 ListView를 사용할 경우 보다 편리하게 구성할 수 있도록 제공되는 Fragment이다.
    - ListView의 id가 @android:id/list 로 설정되어 있을 경우 자동으로 ListView를 찾아 관리하게 된다.

 */
class MainActivity : AppCompatActivity() {

    var listFragment = MyListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var tran = supportFragmentManager.beginTransaction()

        tran.replace(R.id.container, listFragment)

        tran.commit()
    }
}
