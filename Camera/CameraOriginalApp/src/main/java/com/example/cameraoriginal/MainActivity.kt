package com.example.cameraoriginal

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


/*
    이미지 원본 가져오기
    - 안드로이드에서 촬영된 사지의 원본을 가져오려면 촬영된 사진을 파일로 저장한 다음 파일로부터 이미지 데이터를 가져오는 방식으로 개발을 해야 한다.

    권한
    - AndroidManifest.xml 에 카메라 사용권한과 외부 저장소 사용권한을 추가 한다.
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 */
class MainActivity : AppCompatActivity() {


    // 안드로이드 6.0 이상
    var permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    var dirPath: String? = null

    var contentUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionList, 0)
        }
        else {
            init()
        }

        button.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var fileName = "temp_${System.currentTimeMillis()}.jpg"
            var picPath = "${dirPath}/${fileName}"

            var file = File(picPath)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                contentUri = FileProvider.getUriForFile(this, "com.example.cameraoriginal.file_provider", file)
            }
            else {
                contentUri = Uri.fromFile(file)
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
            startActivityForResult(intent, 1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val deniedCount = grantResults.filter { it == PackageManager.PERMISSION_DENIED }.count()
        if(deniedCount > 0) {
            return
        }
        else {
            init()
        }
    }


    fun init() {
        var tempPath = Environment.getExternalStorageDirectory().absolutePath
        dirPath = "${tempPath}/Android/data/${packageName}"

        var file = File(dirPath)
        if(!file.exists()) {
            file.mkdirs()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var bitmap = BitmapFactory.decodeFile(contentUri?.path)
        imageView.setImageBitmap(bitmap)
    }

}
