package com.example.okhttpmultipart

import android.util.Log
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


private val LOG_TAG = "BOARD_CLIENT"


class BoardClient
{

    private constructor()


    companion object
    {

        private val coreSize = 4
        private val maxSize = 9
        private val keepAliveTime = 30L
        private val timeUnit = TimeUnit.SECONDS
        private val queue = SynchronousQueue<Runnable>()

        private lateinit var executor: ThreadPoolExecutor

        private lateinit var boardClient: BoardClient

        init {
            executor = ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, timeUnit, queue)
            boardClient = BoardClient()
        }


        private fun init() {
            if(executor.isShutdown) {
                executor = ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, timeUnit, queue)
            }
        }


        fun destroy() {
            executor.shutdown()
        }


        fun getInstance(): BoardClient {
            init()
            return boardClient
        }


        private val client: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectionPool(ConnectionPool(5, 60, TimeUnit.SECONDS))
                .build()
        }

    }




    fun call(callback: Callback, url: String) {
        Log.d(LOG_TAG, "Call : ${url}")
        executor.execute {
            val request =
                Request.Builder()
                    .url(url)
                    .get()
                    .build()
            client.newCall(request).enqueue(callback)
        }
    }


    private inner class BoardSearchCallback(val callbackFun: (code: Int, response:Any) -> Unit): Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            val json = JSONObject()
            json.put("exception", e.message)
            callbackFun(-1, json)
        }

        override fun onResponse(call: Call, response: Response) {
            val code = response.code()
            val body = response.body()?.string()
            Log.d(LOG_TAG, "Board search response ${code}: ${body}")
            if(code == 200) {
                val jsonArray = JSONArray(body)
                Log.d(LOG_TAG, "Board search result count : ${jsonArray.length()}")
                callbackFun(code, jsonArray)
            }
            else {
                val json = JSONObject(body)
                callbackFun(code, json.getJSONArray("messages"))
            }
        }
    }


    private inner class BoardOneCallback(val callbackFun: (code: Int, response:Any) -> Any?): Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            val json = JSONObject()
            json.put("exception", e.message)
            callbackFun(-1, json)
        }

        override fun onResponse(call: Call, response: Response) {
            val code = response.code()
            val body = response.body()?.string()
            Log.d(LOG_TAG, "Board one item response ${code}: ${body}")
            when(code) {
                200 -> {
                    callbackFun(code, JSONObject(body))
                }

                201 -> {
                    callbackFun(code, JSONObject(body))
                }

                204 -> {
                    val json = JSONObject()
                    json.put("message", "Success.")
                    callbackFun(code, json)
                }

                else -> {
                    val json = JSONObject(body)
                    callbackFun(code, json.getJSONArray("messages"))
                }
            }
        }
    }


    fun findBoardAll(callbackFun: (code: Int, response:Any) -> Unit, page: Int) {
        Log.d(LOG_TAG, "Board search page : ${page}")
        executor.execute {
            val request =
                Request.Builder()
                    .url("${BoardApi.BOARD_SEARCH}?offset=${page}&size=5")
                    .get()
                    .build()

            client.newCall(request).enqueue(BoardSearchCallback(callbackFun))
        }
    }


    fun findByBoardId(callbackFun: (code: Int, response:Any) -> Any?, id: Long) {
        Log.d(LOG_TAG, "Board find by id : ${id}")
        executor.execute {
            val request =
                Request.Builder()
                    .url("${BoardApi.BOARD}/${id}")
                    .get()
                    .build()
            client.newCall(request).enqueue(BoardOneCallback(callbackFun))
        }
    }






    private fun createRequestBody(board: Board, imageList: List<ImageData>): MultipartBody {
        val builder =
            okhttp3.MultipartBody.Builder()
                .setType(okhttp3.MultipartBody.FORM)
                .addFormDataPart("writer", board.writer)
                .addFormDataPart("subject", board.subject)
                .addFormDataPart("content", board.content)

        imageList.forEach {
            val stream = ByteArrayOutputStream()
            it.bitmap!!
            it.bitmap!!.compress(it.fileType.getCompressFormat(), 90, stream)

            builder.addFormDataPart(
                "files",
                it.path,
                RequestBody.create(okhttp3.MultipartBody.FORM, stream.toByteArray())
            )
            Log.d(LOG_TAG, "Multipart file: name ${it.path}, size: ${stream.size()}, file type: ${it.fileType}")

            stream.flush()
            stream.close()
        }

        Log.d(LOG_TAG, "createRequestBody complete")
        return builder.build()
    }


    private fun createPostRequest(board: Board, imageList: List<ImageData>): Request {
        val requestBody = createRequestBody(board, imageList)
        Log.d(LOG_TAG, "Create post request")
        return Request.Builder()
            .url("${BoardApi.BOARD}")
            .post(requestBody)
            .build()
    }


    private fun createPutRequest(board: Board, imageList: List<ImageData>): Request {
        val requestBody = createRequestBody(board, imageList)
        Log.d(LOG_TAG, "Create put request")
        return Request.Builder()
            .url("${BoardApi.BOARD}/${board.id}")
            .put(requestBody)
            .build()
    }


    fun postBoard(callbackFun: (code: Int, response:Any) -> Any?, board: Board, imageList: List<ImageData>) {
        executor.execute {
            val request = createPostRequest(board, imageList)
            client.newCall(request).enqueue(BoardOneCallback(callbackFun))
        }
    }


    fun putBoard(callbackFun: (code: Int, response:Any) -> Any?, board: Board, imageList: List<ImageData>) {
        executor.execute {
            val request = createPutRequest(board, imageList)
            client.newCall(request).enqueue(BoardOneCallback(callbackFun))
        }
    }


    fun deleteBoard(callbackFun: (code: Int, response:Any) -> Any?, id: Long) {
        Log.d(LOG_TAG, "Board delete request : ${id}")
        executor.execute {
            val request =
                Request.Builder()
                    .url("${BoardApi.BOARD}/${id}")
                    .delete()
                    .build()
            client.newCall(request).enqueue(BoardOneCallback(callbackFun))
        }
    }
}