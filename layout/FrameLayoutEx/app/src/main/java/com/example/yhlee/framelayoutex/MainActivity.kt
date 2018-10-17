package com.example.yhlee.framelayoutex

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        host.setup()

        var spec1 = host.newTabSpec("tab1")
        spec1.setIndicator(null, ResourcesCompat.getDrawable(resources, android.R.drawable.ic_menu_camera, null))
        spec1.setContent(R.id.tab_content1)
        host.addTab(spec1)

        var spec2 = host.newTabSpec("tab1")
        spec2.setIndicator(null, ResourcesCompat.getDrawable(resources, android.R.drawable.ic_menu_edit, null))
        spec2.setContent(R.id.tab_content2)
        host.addTab(spec2)

        var spec3 = host.newTabSpec("tab1")
        spec3.setIndicator(null, ResourcesCompat.getDrawable(resources, android.R.drawable.ic_menu_search, null))
        spec3.setContent(R.id.tab_content3)
        host.addTab(spec3)

    }
}
