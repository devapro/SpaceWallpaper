package com.devapp.nasawallpaper.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.*

class HttpImageLoader {
    private val client = OkHttpClient()
//    fun loadToFile(url: String): File? {
//        val request: Request = Request.Builder().url(url).build()
//        val fileName = Date().time.toString() + ".png"
//        client.newCall(request).execute().use { response ->
//            if(response.isSuccessful){
//                val bitmap = BitmapFactory.decodeStream(response.body?.byteStream())
//                if(bitmap != null) {
//                    saveImage(bitmap, fileName)?.let {
//                        dataRepository.updateLocalPath(item.id, it.absolutePath)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun saveImage(image: Bitmap, fileName: String): File? {
//        val cacheDir = appStorage.getCacheDirectory()
//        var imageDirectory = appStorage.getImagesDirectory()
//        if(imageDirectory == null) imageDirectory = cacheDir
//        val imageFile = File(imageDirectory, fileName)
//        return try {
//            FileOutputStream(imageFile).use {
//                image.compress(Bitmap.CompressFormat.PNG, 100, it)
//                it.close()
//                imageFile
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
}