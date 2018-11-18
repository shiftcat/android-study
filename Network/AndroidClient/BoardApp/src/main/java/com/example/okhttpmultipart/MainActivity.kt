package com.example.okhttpmultipart

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.io.File


private class WrapContentLinearLayoutManager(val context: Context?) : LinearLayoutManager(context)
{

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Log.e("probe", "meet a IOOBE in RecyclerView")
        }
    }
}


private val LOG_TAG = "BOARD_MAIN"


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, BoardAdapter.OnLoadMoreListener
{

    private val mBoardItemList = ArrayList<BoardItemBase>()

    private val mBoardAdapter by lazy {
        BoardAdapter(this, mBoardItemList)
    }

    private val mPermissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var mPage: Int = 0

    private val mBoardClient: BoardClient by lazy {
        BoardClient.getInstance()
    }

    private var mDataLoadLock = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefresh.setOnRefreshListener(this)

        listView.adapter = mBoardAdapter
        listView.layoutManager = WrapContentLinearLayoutManager(this)

        initForReload()
        mBoardClient.findBoardAll(::boardSearchCallback, mPage)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(mPermissionList, 0)
        }
        else {
            init()
        }

        mBoardAdapter.setOnItemClickListener { position ->
            val boardItem = mBoardItemList[position]
            val board = Board(boardItem)
            val detailIntent = Intent(this, DetailActivity::class.java)
            detailIntent.putExtra(ExtraName.BOARD, board)
            startActivityForResult(detailIntent, ActivityRequest.REQUEST_DETAIL)
        }

        mBoardAdapter.setOnLoadMoreListener(this)


        listView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!listView.canScrollVertically(1)) {
                    Log.d(LOG_TAG, "mDataLoadLock : ${mDataLoadLock}")
                    if(mDataLoadLock) {
                        mDataLoadLock = false
                        val pos = mBoardAdapter.showLoading()
                        listView.scrollToPosition(pos)
                    }
                }
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        BoardClient.destroy()
    }


    // BoardAdapter.OnLoadMoreListener 구현
    override fun onLoadMore() {
        Log.d(LOG_TAG, "onLoadMore call")
        mBoardClient.findBoardAll(::boardSearchCallback, mPage)
    }


    // SwipeRefreshLayout.OnRefreshListener 구현
    override fun onRefresh() {
        reload()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val deniedCount= grantResults.filter { it == PackageManager.PERMISSION_DENIED }.count()
        if(deniedCount > 0) {
            return
        }
        init()
    }


    private fun init() {
        val tempPath = Environment.getExternalStorageDirectory()
        var dirPath = "${tempPath}/Android/data/${packageName}"
        val file = File(dirPath)
        if(!file.exists()) {
            file.mkdirs()
        }
    }


    private fun initForReload() {
        val beforSize = mBoardItemList.size
        mBoardItemList.clear()
        mBoardAdapter.notifyItemRangeRemoved(0, beforSize)
        mPage = 0
    }


    private fun reload() {
        initForReload()
        mBoardClient.findBoardAll(::boardSearchCallback, mPage)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.mnWrite -> {
                val writeIntent = Intent(this, WriteActivity::class.java)
                startActivityForResult(writeIntent, ActivityRequest.REQUEST_WRITE)
            }

            R.id.mnReload -> {
                reload()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(Activity.RESULT_OK == resultCode) {
            when(requestCode) {
                ActivityRequest.REQUEST_WRITE -> {
                    val newBoard = data?.getParcelableExtra<Board>(ExtraName.NEW_BOARD)!!
                    var boardItem: BoardItemBase =
                            if(newBoard.thumb > -1) {
                                BoardItemWithImage(newBoard.id, newBoard.writer, newBoard.subject, newBoard.content, newBoard.thumb, newBoard.thumbOrigin)
                            }
                            else {
                                BoardItemContent(newBoard.id, newBoard.writer, newBoard.subject, newBoard.content)
                            }

                    mBoardItemList.add(0, boardItem)
                    mBoardAdapter.notifyItemInserted(0)

                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra(ExtraName.BOARD, newBoard)
                    startActivity(intent)
                }

                ActivityRequest.REQUEST_DETAIL -> {
                    val deletedBoard = data?.getParcelableExtra<Board>(ExtraName.DELETE_BOARD)
                    val deletedPosition = mBoardAdapter.getItemPosition(deletedBoard!!)
                    mBoardItemList.removeAt(deletedPosition)
                    mBoardAdapter.notifyItemRemoved(deletedPosition)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }



    private fun boardSearchCallback(code: Int, response: Any): Unit {
        runOnUiThread {
            mBoardAdapter.dismissLoading()
        }
        when(code) {
            -1 -> {
                runOnUiThread {
                    swipeRefresh.isRefreshing = false
                }
            }

            200 -> {
                val root = response as JSONArray
                val idxs = Array(root.length(), {it})
                val boardItems = ArrayList<BoardItemBase>()
                idxs.forEach {
                    val item = root.getJSONObject(it)
                    val boardItem = Converter.jsonToBoardItem(item)
                    boardItems.add(boardItem)
                }
                runOnUiThread {
                    if(boardItems.size > 0) {
                        mBoardAdapter.addItemMore(boardItems)
                        mPage++
                        mDataLoadLock = true
                    }
                    else {
                        Snackbar.make(mainLayout, "조회 결과가 없습니다.", Snackbar.LENGTH_SHORT).show()
                    }
                    swipeRefresh.isRefreshing = false
                }
            }

            else -> {
                runOnUiThread {
                    swipeRefresh.isRefreshing = false
                }
            }
        }
    }


}
