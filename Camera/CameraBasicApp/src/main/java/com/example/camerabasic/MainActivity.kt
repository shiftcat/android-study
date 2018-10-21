package com.example.camerabasic

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_main.*


/*
    카메라 사용하기
    - 안드로이드 애플리케이션에서 카메라를 사용하는 방법은 두 가지가 있다.
      1. 카메라 기능이 구현되어 있는 Activity 사용
      2. SurfaceView를 이용한 카메라 기능 구현
 */
class MainActivity : AppCompatActivity() {

    val CAMERA_ACTIVITY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_ACTIVITY)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CAMERA_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK) {
                var bitmap = data?.getParcelableExtra<Bitmap>("data")
                // 축소된 썸네일 이미지 사지을 받아 옴.
                imageView.setImageBitmap(bitmap)
            }
        }
    }
}
