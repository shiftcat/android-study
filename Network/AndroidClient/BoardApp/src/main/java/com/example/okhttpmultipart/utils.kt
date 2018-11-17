package com.example.okhttpmultipart

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileOutputStream
import java.util.ArrayList

class ImageUtils
{

    private val LOG_TAG = "IMAGE_UTIL"

    companion object
    {
        private val LOG_TAG = "IMAGE_UTIL"

        fun resizeBitmap(source: Bitmap, targetWidth: Int, degree: Float = 0f): Bitmap
        {
            Log.d(LOG_TAG, "Input bitmap size: ${source.width} * ${source.height}")
            Log.d(LOG_TAG, "Input vals = target width: ${targetWidth}, degree: ${degree}")

            var ratio = source.height.toDouble() / source.width.toDouble()
            var targetHeight = (targetWidth * ratio).toInt()
            Log.d(LOG_TAG, "Resize ratio: ${ratio} -> ${targetHeight}")

            val matrix = Matrix()
            matrix.setScale(targetWidth.toFloat()/source.width, targetHeight.toFloat()/source.height)
            matrix.postRotate(degree)
            Log.d(LOG_TAG, "Matrix: ${matrix}")

            var result = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
            Log.d(LOG_TAG, "Result bitmap info : ${result.width}, ${result.height}, ${matrix}")

            source.recycle()

            return result
        }



        fun getDegree(imgPath: String): Float
        {
            Log.d(LOG_TAG, "getDegree input path: ${imgPath}")
            var exif = ExifInterface(imgPath)
            var degree = 0
            // 이미지의 회전 각도 (사진 정보에 회전 각도 정보가 없다면 디폴트값으로 지정한 -1값을 리턴한다.)
            var ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
            Log.d(LOG_TAG, "getDegree ${imgPath} befor degree : ${ori}")
            if (ori > 0) {
                degree = when (ori) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270
                    else -> 0
                }
            }
            Log.d(LOG_TAG, "getDegree ${imgPath} after degree : ${degree}")
            return degree.toFloat()
        }
    }

}



class Converter
{
    companion object
    {
        fun jsonToBoard(json: JSONObject): Board {
            val newId = json.getLong("id")
            val writer = json.getString("writer")
            val subject = json.getString("subject")
            val content = json.getString("content")

            val board = Board()
            board.id = newId
            board.writer = writer
            board.subject = subject
            board.content = content

            if (!json.isNull("thumbnail")) {
                var thumbObj = json.getJSONObject("thumbnail")
                val thumb = thumbObj.getLong("id")
                val thumbOrigin = thumbObj.getLong("originId")

                board.thumb = thumb
                board.thumbOrigin = thumbOrigin

            } else {
                board.thumb = -1
                board.thumbOrigin = -1
            }
            return board
        }


        fun jsonToBoardItem(json: JSONObject): BoardItemBase
        {
            val id = json.getLong("id")
            val writer = json.getString("writer")
            val subject = json.getString("subject")
            val content = json.getString("content")

            if (!json.isNull("thumbnail")) {
                var thumbObj = json.getJSONObject("thumbnail")
                val thumb = thumbObj.getLong("id")
                val thumbOrigin = thumbObj.getLong("originId")
                return BoardItemWithImage(id, writer, subject, content, thumb, thumbOrigin)
            } else {
                return BoardItemContent(id, writer, subject, content)
            }
        }


        fun toImageObjectList(jsonArray: JSONArray): List<JSONObject>
        {
            val idxs = Array(jsonArray.length(), { it })
            val fileList = ArrayList<JSONObject>()
            idxs
                .forEach {
                    val fileItemObject = jsonArray.getJSONObject(it)
                    val fileType = fileItemObject.getString("type")
                    if (fileType.equals("IMG")) {
                        fileList.add(fileItemObject)
                    }
                }
            return fileList
        }


        fun jsonToMessages(jsonArray: JSONArray): String
        {
            val idxs = Array(jsonArray.length(), { it })
            val messages = idxs.map { jsonArray[it] as String }
            return messages.joinToString("\n")
        }


        fun toExceptionMessage(err: Any?, default: String): String
        {
            if(err != null) {
                if(err is JSONObject) {
                    val errJson = err as JSONObject
                    if(errJson.has("exception")) {
                        val errString = errJson.getString("exception")
                        return errString
                    }
                }
            }
            return default
        }

    }
}


enum class FileType
{
    JPEG {
        override fun getExtensions(): List<String> {
            return arrayListOf("jpg", "jpeg")
        }

        override fun getCompressFormat(): Bitmap.CompressFormat {
            return Bitmap.CompressFormat.JPEG
        }
    }
    ,

    PNG {
        override fun getExtensions(): List<String> {
            return arrayListOf("png")
        }

        override fun getCompressFormat(): Bitmap.CompressFormat {
            return Bitmap.CompressFormat.PNG
        }
    }
    ,

    OTHER {
        override fun getExtensions(): List<String> {
            return arrayListOf("")
        }

        override fun getCompressFormat(): Bitmap.CompressFormat {
            return Bitmap.CompressFormat.PNG
        }
    }
    ;


    abstract fun getExtensions(): List<String>


    abstract fun getCompressFormat(): Bitmap.CompressFormat


    companion object {

        private fun getExtension(fileName: String): String
        {
            var res = ""
            val pos = fileName.lastIndexOf('.')
            if(pos > -1) {
                if(fileName.length > (pos+1)) {
                    res = fileName.substring(pos+1)
                }
            }
            return res
        }


        private fun checkExtensions(extenstions: List<String>, ext: String): Boolean
        {
            val res = extenstions.filter {
                it.toUpperCase().equals(ext.toUpperCase())
            }
            return res.isNotEmpty()
        }


        fun getFileType(fileName: String): FileType {
            val ext = getExtension(fileName)
            var res = values().firstOrNull {
                                    checkExtensions(it.getExtensions(), ext)
                                }
            if(res == null) {
                return PNG
            }
            else {
                return res
            }
        }
    }

}