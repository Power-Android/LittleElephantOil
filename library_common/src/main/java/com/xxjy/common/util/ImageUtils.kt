package com.xxjy.common.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.ThreadUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {
    @RequiresApi(Build.VERSION_CODES.FROYO)
    fun saveImage(context: Context, data: String) {
        try {
            val bitmap = webData2bitmap(data)
            if (bitmap != null) {
                save2Album(
                    context,
                    bitmap,
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                        Date()
                    ) + ".jpg"
                )
            } else {
                ThreadUtils.runOnUiThread {
                    Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            ThreadUtils.runOnUiThread { Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show() }
            e.printStackTrace()
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    fun webData2bitmap(data: String): Bitmap {
        val imageBytes = Base64.decode(data.split(",".toRegex()).toTypedArray()[1], Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    fun save2Album(context: Context, bitmap: Bitmap, fileName: String?) {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            fileName
        )
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            ThreadUtils.runOnUiThread {
                context.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(file)
                    )
                )
                Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            ThreadUtils.runOnUiThread { Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show() }
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (ignored: Exception) {
            }
        }
    }
}