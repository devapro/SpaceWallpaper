package com.devapp.nasawallpaper.logic.controllers

import android.content.Context
import com.devapp.nasawallpaper.logic.AppStorage
import com.devapp.nasawallpaper.logic.entity.EntityImage
import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.utils.GlideImageLoader
import com.devapp.nasawallpaper.utils.copy
import com.devapp.nasawallpaper.utils.getFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

class DownloadImageController(private val context: Context, private val dataRepository: DataRepository, private val appStorage: AppStorage) {


    suspend fun downloadImage(
        item: EntityImage
    ) {
        val imageUrl = item.urlHd ?: item.url
        val fileName = getFileName(imageUrl)
        return withContext(Dispatchers.IO){
            var clearedUrl = imageUrl
            if (clearedUrl.isEmpty() || clearedUrl.indexOf(".svg") > 0) {
                return@withContext
            }
            if (clearedUrl.indexOf("//") == 0) {
                clearedUrl = "http:$clearedUrl"
            }

            val loader = GlideImageLoader(object : GlideImageLoader.ActionListener{
                override fun onStart() {

                }

                override fun onProgress(progress: Int) {

                }

                override fun onEnd(cacheFile: File?) {
                    cacheFile.let {
                        val cacheDir = appStorage.getCacheDirectory()
                        var imageDirectory = appStorage.getImagesDirectory()
                        val imageFile = File(imageDirectory ?: cacheDir, fileName)
                        try {
                            copy(cacheFile, imageFile)
                            imageFile.run {
                                dataRepository.updateLocalPath(item.id, absolutePath)
                            }
                        }
                        catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }, context)
            loader.loadToFile(clearedUrl)
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