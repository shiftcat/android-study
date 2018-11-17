package com.example.okhttpmultipart

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.image_write_layout.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


private val LOG_TAG: String = "BOARD_WRITE"

data class ImageData(val path: String, var bitmap: Bitmap?, var url: String? = null, val degree:Float = 0f, val fileType: FileType = FileType.OTHER)


class ImageWriteAdapter(val context: Context, val imageList: MutableList<ImageData>): RecyclerView.Adapter<ImageWriteAdapter.ImageViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ImageViewHolder(layoutInflater.inflate(R.layout.image_write_layout, parent, false))
    }


    override fun getItemCount(): Int {
        return imageList.size
    }


    override fun onBindViewHolder(viewHolder: ImageViewHolder, position: Int) {
        Log.d(LOG_TAG, "ImageWriteAdapter onBindViewHolder position : ${position}")
        val imageData = imageList.get(position)
        if(imageData.bitmap != null) {
            Glide.with(context).load(imageData.bitmap)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        viewHolder.btnDelete.visibility = View.VISIBLE
                        viewHolder.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        viewHolder.btnDelete.visibility = View.VISIBLE
                        viewHolder.progressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(viewHolder.imageView )
        }
        else {
            Log.d(LOG_TAG, "ImageWriteAdapter onBindViewHolder url : ${imageData.url!!}")
            val imageRequestHandler = ImageRequestHandler(this, viewHolder, imageData)
            BoardClient.getInstance().call(ImageRequestCallback(imageRequestHandler), imageData.url!!)
        }
    }



    inner class ImageViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val imageView = view.imagePreview
        val btnDelete = view.btnDeletePreviewImage
        val progressBar = view.progressBar

        init {
            btnDelete.setOnClickListener {
                imageList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                //notifyDataSetChanged()
            }
        }
    }


    var imageLoadCompleteListener: ((String) -> Unit)? = null


    fun setImageLoadListener(argFun: (String) -> Unit) {
        imageLoadCompleteListener = argFun
    }

}


private class ImageRequestCallback(val imageRequestHandler: ImageRequestHandler): Callback
{
    override fun onFailure(call: Call, e: IOException) {
        e.printStackTrace()
        Log.e(LOG_TAG, "${javaClass.simpleName}.onFailure | Image load fail: ${imageRequestHandler.imageData.url} ")
        var msg = Message()
        msg.what = 0
        msg.obj = e.message ?: "Image load fail."
        imageRequestHandler.sendMessage(msg)
    }

    override fun onResponse(call: Call, response: Response) {
        val code = response.code()
        if(code == 200) {
            Log.d(LOG_TAG, "${javaClass.simpleName}.onResponse | Image load success: ${imageRequestHandler.imageData.url} ")
            val bodyBytes = response.body()?.bytes()
            val bmp = BitmapFactory.decodeByteArray(bodyBytes, 0, bodyBytes!!.size)
            Log.d(LOG_TAG, "${javaClass.simpleName}.onResponse | Image size: ${bmp.width} * ${bmp.height}, file size ${bodyBytes.size}")

            var msg = Message()
            msg.what = 1
            msg.obj = bmp
            imageRequestHandler.sendMessage(msg)
        }
        else {
            Log.w(LOG_TAG, "${javaClass.simpleName}.onResponse | Image load fail: ${imageRequestHandler.imageData.url} ")
            var msg = Message()
            msg.what = 0
            msg.obj = "Image load fail."
            imageRequestHandler.sendMessage(msg)
        }
    }
}


class ImageRequestHandler(val adapter: ImageWriteAdapter, val viewHolder: ImageWriteAdapter.ImageViewHolder, val imageData: ImageData): Handler(Looper.getMainLooper())
{
    override fun handleMessage(msg: Message?) {
        //super.handleMessage(msg)
        Log.d(LOG_TAG, "${javaClass.simpleName}.handleMessage image url: ${imageData.url}")
        viewHolder.btnDelete.visibility = View.VISIBLE
        viewHolder.progressBar.visibility = View.GONE
        when(msg?.what) {
            0 -> {
                Toast.makeText(adapter.context, msg?.obj.toString(), Toast.LENGTH_SHORT).show()
            }
            1 -> {
                val bitmap = msg.obj as Bitmap
                imageData.bitmap = bitmap
                viewHolder.imageView.setImageBitmap(bitmap)

                adapter.imageLoadCompleteListener!!(imageData.url!!)
            }
        }
    }
}
