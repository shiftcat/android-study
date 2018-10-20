package com.example.fragmentbasic

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


/*
    Fragment
    - 여러 화면을 가지고 있는 애플리케이션은 여러 Activity를 가지고 있는 애플리케이션을 의미한다.
    - Activity는 독립된 실행단위로 메모리를 많이 소모하는데 독립된 실행단위가 아닌 화면만 필요한 경우 Activity 보단 Fragment를 사용하는 것이 효율적이다.
    - Fragment는 Activity 내의 작은 화면의 조작으로 Activity의 하면을 여러 영역으로 나누어 관리 하고자 하는 목적으로 사용한다.
    - Fragment는 안드로이드 5.0에서 추가되었지만 하위 버전에서도 사용할 수 있도록 설계되어 있다.
    - add: Fragment를 지정된 레이아웃에 추가한다.
    - replace: 지정된 레이아웃에 설정되어 있는 Fragment를 제거하고 새로운 Fragment를 추가한다.


    AddToBackStack
    - 안드로이드에서 Back Button은 현재 액티비티를 종료한다.
    - Fragment는 Activity가 아니므로 Back Button으로 제거할 수 없다.
    - addToBackStack 메서드를 통해 Back Stack에 포함할 경우 Back Button으로 제거할 수 있다.
    - 이를 통해 마치 이전 화면으로 동아는 듯한 효과를 줄 수 있다.
 */
class MainActivity : AppCompatActivity() {

    var first = FirstFragment()
    var second = SecondFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener {
            var tran = supportFragmentManager.beginTransaction()
            //tran.add(R.id.container, first)
            tran.replace(R.id.container, first)
            tran.addToBackStack("first")
            tran.commit()
        }

        button2.setOnClickListener {
            var tran = supportFragmentManager.beginTransaction()
            // tran.add(R.id.container, second)
            tran.replace(R.id.container, second)
            tran.addToBackStack("second")
            tran.commit()
        }
    }
}
