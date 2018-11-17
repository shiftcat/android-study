package com.example.okhttpmultipart

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.board_item_content.view.*
import kotlinx.android.synthetic.main.board_item_load.view.*
import kotlinx.android.synthetic.main.board_item_with_image.view.*


private val LOG_TAG = "BOARD_MAIN"


enum class BoardItemType
{
    ITEM_LOAD, CONTENT_ONLY, CONTENT_WITH_IMAGE
}

abstract class BoardItemBase(val id: Long)
{
    abstract fun getItemType(): BoardItemType
}


class BoardItemLoad(id: Long): BoardItemBase(id)
{
    override fun getItemType(): BoardItemType {
        return BoardItemType.ITEM_LOAD
    }
}

class BoardItemContent(id: Long, val writer: String, val subject: String, val content: String): BoardItemBase(id)
{
    override fun getItemType(): BoardItemType {
        return BoardItemType.CONTENT_ONLY
    }
}


class BoardItemWithImage(id: Long, val writer: String, val subject: String, val content: String, val thumbId: Long = -1, val thumbOriginId: Long = -1): BoardItemBase(id) {
    override fun getItemType(): BoardItemType {
        return BoardItemType.CONTENT_WITH_IMAGE
    }
}




private class BoardDeleteDialogListener(val adapter: BoardAdapter, val position: Int, val waiteDialog: Dialog): DialogInterface.OnClickListener
{

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when(which) {
            DialogInterface.BUTTON_POSITIVE -> {
                waiteDialog.show()
                val boardItem = adapter.getItem(position)
                BoardClient.getInstance().deleteBoard(::boardDeleteCallback, boardItem.id)
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                // pass
            }
        }
    }

    private fun boardDeleteCallback(code: Int, response: Any) {
        if (code == 204) {
            adapter.boardList.removeAt(position)

            val deleteHandler = BoardDeleteHandler(adapter, waiteDialog)
            val msg = Message()
            msg.what = 204
            msg.obj = position
            deleteHandler.sendMessage(msg)
        }
    }

}


class BoardDeleteHandler(val adapter: BoardAdapter, val waiteDialog: Dialog): Handler(Looper.getMainLooper())
{
    override fun handleMessage(msg: Message?) {
        super.handleMessage(msg)
        if(waiteDialog.isShowing) {
            waiteDialog.cancel()
        }
        if(msg?.what == 204) {
            val position = msg.obj as Int
            adapter.notifyItemRemoved(position)
        }
    }
}



/*
 리사이클러 뷰의 컨텍스트 메뉴를 생성하기 위한 함수
  - 뷰 홀더에서 호출된다.
  - 각 메뉴에 대한 클릭 리스너 정의한다.
 */
private fun createContextMenu(context: Context, menu: ContextMenu?, v: View?, adapter: BoardAdapter, position: Int)
{
    //groupId, itemId, order, title
    val mnItemEdit = menu?.add(Menu.NONE, v!!.id, 0, "수정")
    val mnItemDelete = menu?.add(Menu.NONE, v!!.id, 0, "삭제")

    val waiteDialog: Dialog by lazy {
        WaitDialog(context)
    }

    mnItemEdit?.setOnMenuItemClickListener {
        val boardItem = adapter.getItem(position)
        val board = Board(boardItem)
        var intent = Intent(context, WriteActivity::class.java)

        intent.putExtra(ExtraName.BOARD, board)
        context.startActivity(intent)
        return@setOnMenuItemClickListener true

    }

    mnItemDelete?.setOnMenuItemClickListener {
        showDeleteDialog(context, BoardDeleteDialogListener(adapter, position, waiteDialog))
        return@setOnMenuItemClickListener true
    }
}


/*
 게시물에서 이미지가 포함된 게시물에 대한 뷰 홀더
 */
private class ViewWithImageHolder(val view: View, val adapter: BoardAdapter): RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener
{

    val imageView = view.imageView
    val titleView = view.textViewContent2

    init {
        view.setOnCreateContextMenuListener(this@ViewWithImageHolder)
        view.setOnClickListener {
            adapter.itemClickListener!!(adapterPosition)
        }
    }


    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        createContextMenu(view.context, menu, v, adapter, adapterPosition)
    }

}


/*
 게시물에서 이미지가 없는 게시물에 대한 뷰 홀더
 */
private class ViewContentHolder(val view: View, val adapter: BoardAdapter): RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener
{

    val titleView = view.textViewContent1

    init {
        view.setOnCreateContextMenuListener(this@ViewContentHolder)
        view.setOnClickListener {
            adapter.itemClickListener!!(adapterPosition)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        createContextMenu(view.context, menu, v, adapter, adapterPosition)
    }
}


private class ViewItemLoadHolder(val view: View): RecyclerView.ViewHolder(view)
{
    val progressBar = view.progressBarItemLoad
}



class BoardAdapter(val context: Context, val boardList: MutableList<BoardItemBase>): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{


    private var isMoreLoading = true


    var itemClickListener: ((Int) -> Any?)? = null


    private var onLoadMoreListener: OnLoadMoreListener? = null


    interface OnLoadMoreListener {
        fun onLoadMore()
    }


    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        onLoadMoreListener = listener
    }

    /*
        이미지 컨텐츠 존재 유무에 따라 뷰 타입 결정.
     */
    override fun getItemViewType(position: Int): Int {
        val boardItem = boardList.get(position)
        return boardItem.getItemType().ordinal
    }


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val boardItemBase = boardList.get(position)
        val viewType = getItemViewType(position)

        when(viewType) {
            0-> {

            }
            1 -> {
                val boardItem = boardItemBase as BoardItemContent
                val itemViewHolder = viewHolder as ViewContentHolder
                itemViewHolder.titleView.text = boardItem.content
            }
            2 -> {
                val boardItem = boardItemBase as BoardItemWithImage
                val itemViewHolder = viewHolder as ViewWithImageHolder
                itemViewHolder.titleView.text = boardItem.content
                Glide.with(context).load("${BoardApi.BOARD_THUMNAIL}/${boardItem.thumbId}").into(itemViewHolder.imageView)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        when(type) {
            0 -> {
                return ViewContentHolder(layoutInflater.inflate(R.layout.board_item_load, parent, false), this)
            }

            1 -> {
                return ViewContentHolder(layoutInflater.inflate(R.layout.board_item_content, parent, false), this)
            }

            else -> {
                return ViewWithImageHolder(layoutInflater.inflate(R.layout.board_item_with_image, parent, false), this)
            }
        }
    }



    override fun getItemCount(): Int {
        return boardList.size
    }


    fun showLoading(): Int {
        Log.d(LOG_TAG, "isMoreLoading : ${isMoreLoading}")

        if (isMoreLoading && onLoadMoreListener != null) {
            isMoreLoading = false
            Handler().post {
                boardList.add(BoardItemLoad(-1))
                notifyItemInserted(boardList.size - 1)
                onLoadMoreListener!!.onLoadMore()
            }
        }
        return boardList.size -1
    }


    fun dismissLoading() {
        Log.d(LOG_TAG, "dismissLoading call")
        if (boardList.size > 0 && isMoreLoading == false) {
            if(getItem(boardList.size-1) is BoardItemLoad) {
                boardList.removeAt(boardList.size - 1)
                notifyItemRemoved(boardList.size)
            }
            isMoreLoading = true
        }
    }


    fun addItemMore(lst: List<BoardItemBase>) {
        val sizeInit = boardList.size
        boardList.addAll(lst)
        notifyItemRangeChanged(sizeInit, boardList.size)
    }



    fun getItem(position: Int): BoardItemBase {
        return boardList.get(position)
    }


    fun getItemPosition(board: Board): Int {
        boardList.forEachIndexed { index, boardItem ->
            if(boardItem.id == board.id) {
                return index
            }
        }
        return -1
    }


    fun setOnItemClickListener(listener: (position: Int) -> Any?) {
        this.itemClickListener = listener
    }


    fun updateList(newList: MutableList<BoardItemBase>) {
        val callback = ListDiffCallback(this.boardList, newList)
        val diffResult = DiffUtil.calculateDiff(callback)

        // this.boardList.clear()
        this.boardList.addAll(newList)

        Handler(Looper.getMainLooper()).post(
            object : Runnable {
                override fun run() {
                    diffResult.dispatchUpdatesTo(this@BoardAdapter)
                }
            }
        )
    }

}


private class ListDiffCallback(val oldList: MutableList<BoardItemBase>, val newList: MutableList<BoardItemBase>): DiffUtil.Callback() {
    override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
        return oldList.get(p0).id == newList.get(p1).id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
        val oldItem = oldList.get(p0)
        val newItem = newList.get(p1)
        return oldItem.equals(newItem)
    }
}