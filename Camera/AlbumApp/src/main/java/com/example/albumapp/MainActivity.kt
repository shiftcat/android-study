package com.example.albumapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    var permissionList = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions((permissionList), 0)
        }

        button.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, 1)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            var c = contentResolver.query(data?.data, null, null, null, null)
            c.moveToNext()
            var index = c.getColumnIndex(MediaStore.Images.Media.DATA)
            var source = c.getString(index)
            showImage2(source)
        }
    }

    private fun showImage1(source: String) {
        var bitmap = BitmapFactory.decodeFile(source)
        bitmap = resizeBitmap(1024, bitmap)
        imageView.setImageBitmap(bitmap)

        var degree = getDegree(source)
        imageView.rotation = degree
    }


    private fun showImage2(source: String) {
        Glide.with(this).load(source).into(imageView)
    }

    private fun resizeBitmap(targetWidth:Int, source: Bitmap): Bitmap {
        var ratio = source.height.toDouble() / source.width.toDouble()
        var targetHeight = (targetWidth * ratio).toInt()
        var result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false)
        if(result != source) {
            source.recycle()
        }
        return result
    }


    private fun getDegree(imgPath: String): Float {
        var exif = ExifInterface(imgPath)
        var degree = 0
        // 이미지의 회전 각도 (사진 정보에 회전 각도 정보가 없다면 디폴트값으로 지정한 -1값을 리턴한다.)
        var ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
        if(ori > 0) {
            degree = when(ori) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        }
        return degree.toFloat()
    }


}
