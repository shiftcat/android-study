package com.example.cameraapi1app

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    val LOG_TAG = "CameraAPI1App"

    val mPermissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    var mCamera: Camera? = null


    val dirPath: String by lazy {
        var tempPath = Environment.getExternalStorageDirectory().absolutePath
        "${tempPath}/Android/data/${packageName}"
    }


    val surfaceHolderCallback = SurfaceHolderCallback()


    val pictureCallback = PictureCallback()


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(LOG_TAG, "MainActivity onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        surfaceView.holder.addCallback(surfaceHolderCallback)

        button.setOnClickListener {

            mCamera!!.takePicture(null, null, pictureCallback)
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "MainActivity onStart")
    }


    override fun onStop() {
        super.onStop()
        if(mCamera != null) {
            mCamera?.release()
        }
        Log.d(LOG_TAG, "MainActivity onStop")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "MainActivity onDestroy")
    }


    inner class PictureCallback: Camera.PictureCallback {
        override fun onPictureTaken(data: ByteArray?, camera: Camera?) {
            var bitmap = BitmapFactory.decodeByteArray(data, 0, data?.size!!)
            var filePath = "${dirPath}/temp_${System.currentTimeMillis()}.jpg"
            Log.d(LOG_TAG, filePath)
            var file = File(filePath)
            var fos = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()

            camera?.startPreview()
        }
    }



    inner class SurfaceHolderCallback: SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            Log.d(LOG_TAG, "SurfaceHolder surfaceChanged")
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            Log.d(LOG_TAG, "SurfaceHolder surfaceDestroyed")
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            Log.d(LOG_TAG, "SurfaceHolder surfaceCreated")
            Log.d(LOG_TAG, "Version check ${Build.VERSION.SDK_INT}")
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(mPermissionList, 0)
            }
            else {
                initializeCamera()
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val deniedCount = grantResults.filter { it == PackageManager.PERMISSION_DENIED }.count()
        Log.d(LOG_TAG, "PERMISSION_DENIED ${deniedCount}")
        if(deniedCount > 0) {
            Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT)
            return
        }
        else {
            initializeCamera()
        }
    }


    private fun openCamera(): Camera {
        var camera = Camera.open()

        var degree = when(windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> 90
            Surface.ROTATION_90 -> 0
            Surface.ROTATION_180 -> 270
            Surface.ROTATION_270 -> 180
            else -> 0
        }
        camera.setDisplayOrientation(degree)

        return camera
    }


    private fun initializeCamera() {
        Log.d(LOG_TAG, "Camera initializeCamera start")
        var file = File(dirPath)
        if(!file.exists()) {
            file.mkdirs()
        }

        mCamera = openCamera()
        mCamera!!.setPreviewDisplay(surfaceView.holder)
        mCamera!!.startPreview()
        Log.d(LOG_TAG, "Camera initializeCamera end")

    }

}
