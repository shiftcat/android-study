package com.example.activitycontroller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

/*
    Controller
    - 웹, 모바일 등 애플리케이션 개발 시 눈에 보이는 화면들을 관리하는 요소를 Controller라고 부른다.
    - 만약 눈에 보이는 모든 화면을 Fragment로 만들어 사용할 경우 Fragment를 관리하는 Activity가 Controller의 역할을 한다.

    Activity의 역할
    - 각 Fragment를 교환하고 관리하는 역할을 한다.
    - Fragment 들이 사용할 데이터를 관리하는 역할을 한다.
 */
class MainActivity : AppCompatActivity() {

    var inputFragment = InputFragment()

    var resultFragment = ResultFragment()

    var value1: String? = null
    var value2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFragment("input")
    }


    fun setFragment(name: String) {
        var tran = supportFragmentManager.beginTransaction()
        when(name) {
            "input" -> tran.replace(R.id.container, inputFragment)
            "result" -> {
                tran.replace(R.id.container, resultFragment)
                tran.addToBackStack(null)
            }
        }

        tran.commit()
    }
}
