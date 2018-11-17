package com.example.okhttpmultipart

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_write.*
import org.apache.commons.lang3.StringUtils
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.collections.HashSet


private val LOG_TAG: String = "BOARD_WRITE"

private class WrapContentLinearLayoutManager2(context: Context, orientation: Int, reverseLayout: Boolean) :
    LinearLayoutManager(context, orientation, reverseLayout)
{
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Log.e(LOG_TAG, "meet a IOOBE in RecyclerView")
        }
    }
}


class WriteActivity : AppCompatActivity() {


    // 카메라 임시 파일 저장 경로
    private val mDirPath: String by lazy {
        val tempPath = Environment.getExternalStorageDirectory()
        "${tempPath}/Android/data/${packageName}"
    }


    private lateinit var mContentUri: Uri

    private val mImageList = ArrayList<ImageData>()

    private val mImageWriteAdapter: ImageWriteAdapter by lazy {
        ImageWriteAdapter(this, mImageList)
    }


    /*
     기존에 존재하는 게시물 정보을 담을 Board 변수
     */
    private var mExistingBoard: Board? = null

    private val mBoardClient: BoardClient by lazy {
        BoardClient.getInstance()
    }


    val waitDialog: Dialog by lazy {
        WaitDialog(this)
    }


    /*
        기존 게시물의 이미지에 대한 URL 정보를 담을 SET
        게시물을 조회하여 이미지가 로드 되면 해당 URL을 제거 한다.
     */
    val mImageLoadSet: HashSet<String> by lazy {
        HashSet<String>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        var action = supportActionBar
        action?.setHomeButtonEnabled(true)
        action?.setDisplayHomeAsUpEnabled(true)


        listViewImageWrite.layoutManager = WrapContentLinearLayoutManager2(this, LinearLayoutManager.HORIZONTAL, false)
        listViewImageWrite.adapter = mImageWriteAdapter
        listViewImageWrite.isNestedScrollingEnabled = false
        listViewImageWrite.setHasFixedSize(true)

        // 이전 Activity에서 Board 정보가 넘어 왔다면...
        if (intent.hasExtra(ExtraName.BOARD)) {
            // 이전 Activity에서 기존 Board 객체 가져 옴.
            val board = intent.getParcelableExtra<Board>(ExtraName.BOARD)
            mExistingBoard = board
            editTextContent.setText(board.content, TextView.BufferType.EDITABLE)

            // 이미지 로드 set 초기화
            mImageLoadSet.clear()

            // 이미지 데이터 리스트 초기화
            val beforCnt = mImageList.size
            mImageList.clear()
            mImageWriteAdapter.notifyItemRangeRemoved(0, beforCnt)

            // wait 다이얼로그 show
            waitDialog.show()

            // 서버로 부터 board id 에 해당하는 게시물 조회
            mBoardClient.findByBoardId(::boardFindOneCallback, board.id)
            Log.d(LOG_TAG, "onCreate : ${mImageList.size}")
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.write_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.mnCamera -> {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val fileName = "temp_${System.currentTimeMillis()}.jpg"
                val picPath = "${mDirPath}/${fileName}"
                val file = File(picPath)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mContentUri = FileProvider.getUriForFile(this, "com.example.okhttpmultipart.file_provider", file)
                } else {
                    mContentUri = Uri.fromFile(file)
                }

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mContentUri)
                startActivityForResult(cameraIntent, 1)
            }

            R.id.mnGallery -> {
                var galleryIntent = Intent(Intent.ACTION_PICK)
                galleryIntent.type = MediaStore.Images.Media.CONTENT_TYPE
                startActivityForResult(galleryIntent, 2)
            }

            R.id.mnUpload -> {
                if(mImageLoadSet.size > 0) {
                    Toast.makeText(this, "이미지 로드가 완료 되지 않았습니다.", Toast.LENGTH_SHORT).show()
                    return false
                }
                waitDialog.show()
                val board = Board()
                board.writer = "android"
                board.subject = StringUtils.substring(editTextContent.text.toString(), 0, 100)
                board.content = editTextContent.text.toString()
                if (mExistingBoard != null) {
                    board.id = mExistingBoard!!.id
                    mBoardClient.putBoard(::boardWriteCallback, board, mImageList)
                }
                else {
                    mBoardClient.postBoard(::boardWriteCallback, board, mImageList)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                // 카메라
                1 -> {
                    var bitmap = BitmapFactory.decodeFile(mContentUri.path)

                    val degree = ImageUtils.getDegree(mContentUri.path)
                    bitmap = ImageUtils.resizeBitmap(bitmap, 1024, degree)

                    val imageData = ImageData(mContentUri.path, bitmap, null, degree, FileType.getFileType(mContentUri.path))
                    mImageList.add(imageData)
                    listViewImageWrite.adapter?.notifyDataSetChanged()
                }

                // 갤러리
                2 -> {
                    val cr = contentResolver.query(data?.data, null, null, null, null)
                    cr.moveToNext()

                    val index = cr.getColumnIndex(MediaStore.Images.Media.DATA)
                    val source = cr.getString(index)

                    val degree = ImageUtils.getDegree(source)
                    var bitmap = BitmapFactory.decodeFile(source)
                    bitmap = ImageUtils.resizeBitmap(bitmap, 1024, degree)

                    val imageData = ImageData(source, bitmap, null, degree, FileType.getFileType(source))
                    mImageList.add(imageData)
                    listViewImageWrite.adapter?.notifyDataSetChanged()
                }
            }
        }
    }




    private fun boardFindOneCallback(code: Int, response: Any): Unit {
        Log.d(LOG_TAG, "boardFindOneCallback : ${mImageList.size}")
        when(code) {
            -1 -> {
                runOnUiThread {
                    if(waitDialog.isShowing) {
                        waitDialog.cancel()
                    }
                    Toast.makeText(this@WriteActivity,
                        Converter.toExceptionMessage(response, "게시물 읽기 실패."), Toast.LENGTH_SHORT).show()
                }
            }

            200 -> {
                val root = response as JSONObject
                val fileOjbectArray = root.getJSONArray("files")
                val imageObjectList = Converter.toImageObjectList(fileOjbectArray)
                Log.d(LOG_TAG, "boardFindOneCallback recceive 200: ${mImageList.size}")

                val imageDataList = ArrayList<ImageData>()

                imageObjectList.forEach {
                    Log.d(LOG_TAG, "boardFindOneCallback forEach begin: ${mImageList.size}")
                    val imageObject = it
                    val fileId = imageObject.getLong("id")
                    val fileName = imageObject.getString("name")
                    val imageData = ImageData(fileName, null, "${BoardApi.FILE}/${fileId}", 0f, FileType.getFileType(fileName))
                    Log.d(LOG_TAG, "boardFindOneCallback ImageData url : ${imageData.url}")
                    mImageLoadSet.add(imageData.url!!)
                    imageDataList.add(imageData)
                }

                if(imageDataList.size > 0) {
                    runOnUiThread {
                        mImageWriteAdapter.setImageLoadListener {
                            // 이미지 로드가 완료되면 그 이미지의 URL이 인자로 들어오며 set에서 제거 한다.
                            mImageLoadSet.remove(it)
                            /*
                                리사이클러 뷰가 스크롤 되어야 이미지가 로드 된다.
                                사용자가 스크롤 하면 모든 이미지가 로드 될 것이다.
                                여기서는 여러 이미지 중 하나가 로드 되면 wait 다이얼로그를 종료 한다.
                             */
                            if(mImageLoadSet.size <= (imageDataList.size-1)) {
                                if(waitDialog.isShowing) {
                                    waitDialog.cancel()
                                }
                            }
                        }
                        mImageList.addAll(imageDataList)
                        mImageWriteAdapter.notifyItemRangeInserted(0, imageDataList.size)
                    }
                    Log.d(LOG_TAG, "boardFindOneCallback forEach end : ${mImageList.size}")
                }
                else {
                    runOnUiThread {
                        if(waitDialog.isShowing) {
                            waitDialog.cancel()
                        }
                    }
                }
            }

            else -> {
                runOnUiThread {
                    if(waitDialog.isShowing) {
                        waitDialog.cancel()
                    }
                    Toast.makeText(this@WriteActivity, Converter.jsonToMessages(response as JSONArray), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    // 신규 게시물 또는 게시물 변경에 대한 콜백 함수
    private fun boardWriteCallback(code: Int, response: Any): Unit {
        when(code) {
            -1 -> {
                runOnUiThread {
                    if(waitDialog.isShowing) {
                        waitDialog.cancel()
                    }
                    Toast.makeText(this@WriteActivity, (response as JSONObject).getString("exception"), Toast.LENGTH_SHORT).show()
                }
            }

            // 기존 게시물 변경
            200 -> {
                Log.d(LOG_TAG, "boardWriteCallback result ok: ${response.toString()}")
                runOnUiThread {
                    if(waitDialog.isShowing) {
                        waitDialog.cancel()
                    }
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }

            // 신규 게시물
            201 -> {
                Log.d(LOG_TAG, "boardWriteCallback result ok: ${response.toString()}")
                val board = Converter.jsonToBoard(response as JSONObject)
                runOnUiThread {
                    if(waitDialog.isShowing) {
                        waitDialog.cancel()
                    }
                    val intent = Intent()
                    intent.putExtra(ExtraName.NEW_BOARD, board)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }

            else -> {
                Log.d(LOG_TAG, "boardWriteCallback result fail: ${response.toString()}")
                runOnUiThread {
                    if(waitDialog.isShowing) {
                        waitDialog.cancel()
                    }
                    Toast.makeText(this@WriteActivity, Converter.jsonToMessages(response as JSONArray), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


}
