package com.example.okhttpmultipart

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicInteger

private val LOG_TAG = "BOARD_DETAIL"

class DetailActivity : AppCompatActivity() {

    private val mBoardClient: BoardClient by lazy {
        BoardClient.getInstance()
    }


    private lateinit var mBoard: Board


    private val mDelDialogListener: DialogInterface.OnClickListener by lazy {
        DialogInterface.OnClickListener { dialog, which ->
            when(which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    waitDialog.show()
                    mBoardClient.deleteBoard(::boardDeleteCallback, mBoard.id)
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    // pass
                }
            }
        }
    }

    val waitDialog: Dialog by lazy {
        WaitDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var action = supportActionBar
        action?.setHomeButtonEnabled(true)
        action?.setDisplayHomeAsUpEnabled(true)

        mBoard = intent.getParcelableExtra<Board>(ExtraName.BOARD)
        mBoardClient.findByBoardId(::boardFindOneCallback, mBoard.id)
        waitDialog.show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.mnEdit -> {
                var intent = Intent(this, WriteActivity::class.java)

                intent.putExtra(ExtraName.BOARD, mBoard)
                startActivityForResult(intent, ActivityRequest.REQUEST_WRITE)
            }

            R.id.mnDelete -> {
                showDeleteDialog(this, mDelDialogListener)
            }
        }
        return super.onOptionsItemSelected(item)
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(Activity.RESULT_OK == resultCode) {
            when(requestCode) {
                ActivityRequest.REQUEST_WRITE -> {
                    imageContainer.removeAllViews()
                    mBoardClient.findByBoardId(::boardFindOneCallback, mBoard.id)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun boardDeleteCallback(code: Int, response: Any) {
        if(waitDialog.isShowing) {
            waitDialog.cancel()
        }
        when(code) {
            -1 -> {
                Log.e(LOG_TAG, "Board delete fail: ${response}")
                runOnUiThread {
                    val errString = Converter.toExceptionMessage(response, "게시물 삭제 실패")
                    Toast.makeText(this@DetailActivity, errString, Toast.LENGTH_SHORT).show()
                }
            }
            204 -> {
                runOnUiThread {
                    var intent = Intent()
                    intent.putExtra(ExtraName.DELETE_BOARD, mBoard)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
            else -> {
                Log.w(LOG_TAG, "Board delete fail: ${response}")
                runOnUiThread {
                    Toast.makeText(this@DetailActivity, Converter.jsonToMessages(response as JSONArray), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun boardFindOneCallback(code: Int, response: Any): Unit {
        when(code) {
            -1 -> {
                Log.e(LOG_TAG, "Board read fail : ${response}")
                runOnUiThread {
                    if(waitDialog.isShowing) {
                        waitDialog.cancel()
                    }
                    val errString = Converter.toExceptionMessage(response, "게시물 로드 실패")
                    Toast.makeText(this@DetailActivity, errString, Toast.LENGTH_SHORT).show()
                }
            }

            200 ->{
                val root = response as JSONObject
                val subject = root.getString("subject")
                val content = root.getString("content")
                mBoard.subject = subject
                mBoard.content = content

                runOnUiThread {
                    textViewContent.text = content
                }

                val fileOjbectArray = root.getJSONArray("files")
                val fileObjectList = Converter.toImageObjectList(fileOjbectArray)
                val imageLoadCount = AtomicInteger(fileObjectList.size)
                val imageFailCount = AtomicInteger(0)

                val layoutparams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                fileObjectList
                    .forEach {
                        val fileItemObject = it
                        val fileId = fileItemObject.getLong("id")
                        val fileName = fileItemObject.getString("name")

                        val uri = "${BoardApi.FILE}/${fileId}"
                        Log.d(LOG_TAG, "Image load name, ${fileName}, url: ${uri}")

                        // ImageView 동적 로드
                        val imageView = ImageView(this@DetailActivity)
                        imageView.layoutParams = layoutparams
                        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                        imageView.setPadding(0, 10, 0, 0)

                        runOnUiThread {
                            Glide.with(this@DetailActivity)
                                .load(uri)
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                        val cnt = imageLoadCount.decrementAndGet()
                                        imageFailCount.incrementAndGet()
                                        Log.d(LOG_TAG, "Board read / image load fail count: ${cnt}")
                                        if(cnt <= 0) {
                                            if(waitDialog.isShowing) {
                                                waitDialog.cancel()
                                            }
                                        }
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                        val cnt = imageLoadCount.decrementAndGet()
                                        Log.d(LOG_TAG, "Board read / image success count: ${cnt}")
                                        if(cnt <= 0) {
                                            if(waitDialog.isShowing) {
                                                waitDialog.cancel()
                                            }
                                        }
                                        return false
                                    }
                                }).into(imageView)
                            imageContainer.addView(imageView)

                            if(imageFailCount.get() > 0 ) {
                                Toast.makeText(this@DetailActivity, "로드되지 않은 이미지가 있습니다.", Toast.LENGTH_SHORT).show()
                            }
                        } // end runOnUiThread

                    }

                if(fileObjectList.isEmpty()) {
                    runOnUiThread {
                        if(waitDialog.isShowing) {
                            waitDialog.cancel()
                        }
                    }
                }
            }

            else -> {
                Log.w(LOG_TAG, "Board read fail : ${response}")
                runOnUiThread {
                    if(waitDialog.isShowing) {
                        waitDialog.cancel()
                    }
                    Toast.makeText(this@DetailActivity, Converter.jsonToMessages(response as JSONArray), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}
