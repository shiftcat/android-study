package com.example.yhlee.viewpagerex

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*


/*
    ViewPager
    - 좌우로 스와프 하며 View를 전환하는 AdapterView
    - 화면이 바뀌는 것이 아닌 화면만한 뷰들이 전환되는 개념이다.

    Adapter
    - PagerAdapter를 구형하여 사용한다.
    - getCount: ViewPager로 보여줄 뷰의 전체 개수
    - isViewFromObject: instantiateItem에서 만든 객체를 사용할 것인지의 여부를 결정한다.
    - instantiateItem: ViewPager로 보여줄 뷰 객체를 반환한다.
    - destroyItem: ViewPager에서 뷰가 사라질 때 제거하는 작업을 한다.

 */
class MainActivity : AppCompatActivity() {
    var viewList = ArrayList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewList.add(layoutInflater.inflate(R.layout.view1, null))
        viewList.add(layoutInflater.inflate(R.layout.view2, null))
        viewList.add(layoutInflater.inflate(R.layout.view3, null))

        pager.adapter = CustomAdapter()
        pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, p2: Int) {
                textView.text = "${position} 뷰 전환"
            }

            override fun onPageSelected(p0: Int) {
            }

        })
    }


    inner class CustomAdapter: PagerAdapter() {
        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun getCount(): Int {
            return viewList.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
//            return super.instantiateItem(container, position)
            pager.addView(viewList[position])
            return viewList[position]
        }


        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
//            super.destroyItem(container, position, obj)
            pager.removeView(obj as View)
        }
    }
}
