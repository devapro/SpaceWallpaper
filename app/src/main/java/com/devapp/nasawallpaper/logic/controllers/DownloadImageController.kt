package com.devapp.nasawallpaper.logic.controllers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.devapp.nasawallpaper.logic.AppStorage
import com.devapp.nasawallpaper.logic.entity.EntityImage
import com.devapp.nasawallpaper.storage.database.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

class DownloadImageController(private val dataRepository: DataRepository, private val appStorage: AppStorage) {
    private val client = OkHttpClient()

    suspend fun downloadImage(
        item: EntityImage
    ) {
        val imageUrl = item.urlHd ?: item.url
        val fileName = Date().time.toString() + ".png"
        return withContext(Dispatchers.IO){
            var clearedUrl = imageUrl
            if (clearedUrl.isEmpty() || clearedUrl.indexOf(".svg") > 0) {
                return@withContext
            }
            if (clearedUrl.indexOf("//") == 0) {
                clearedUrl = "http:$clearedUrl"
            }
            val request: Request = Request.Builder().url(clearedUrl).build()
            client.newCall(request).execute().use { response ->
                if(response.isSuccessful){
                    val bitmap = BitmapFactory.decodeStream(response.body?.byteStream())
                    if(bitmap != null) {
                        saveImage(bitmap, fileName)?.let {
                            dataRepository.updateLocalPath(item.id, it.absolutePath)
                        }
                    }
                }
            }
        }
    }

    private fun saveImage(image: Bitmap, fileName: String): File? {
        val cacheDir = appStorage.getCacheDirectory()
        var imageDirectory = appStorage.getImagesDirectory()
        if(imageDirectory == null) imageDirectory = cacheDir
        val imageFile = File(imageDirectory, fileName)
        return try {
            FileOutputStream(imageFile).use {
                image.compress(Bitmap.CompressFormat.PNG, 100, it)
                it.close()
                imageFile
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun checkFile(fileName: String): Boolean{
        val cacheDir = appStorage.getCacheDirectory()
        val imageFile = File(cacheDir, fileName)
        return imageFile.exists()
    }

    fun getFile(fileName: String): File {
        val cacheDir = appStorage.getCacheDirectory()
        return File(cacheDir, fileName)
    }
}