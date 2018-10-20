package com.example.tablayout

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*


/*
    TabLayout
    - Appbar Layout에 TabBarLayout과 ViewPager를 통해 탭을 구성할 수 있다.
 */
class MainActivity : AppCompatActivity() {


    var fragList: List<Fragment>? = null

    var tabTitleList = ArrayList<String>()

    var idxs = Array(10, { it })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        tabs.setTabTextColors(Color.WHITE, Color.RED)

        fragList = idxs.map {
            tabTitleList.add("tab ${it}")

            var sub = SubFragment()
            sub.str1 = "Sub ${it}"
            sub
        }.toMutableList()


        pager.adapter = PagerAdapter(supportFragmentManager)
        tabs.setupWithViewPager(pager)
    }


    inner class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return fragList?.get(position)!!
        }

        override fun getCount(): Int {
            return fragList?.size!!
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitleList.get(position)
        }
    }
}
