package com.devapp.nasawallpaper.logic.controllers

import android.util.Log
import androidx.lifecycle.LiveData
import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.storage.database.ImageMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class AppController (private val dataController: DataController, private val dataRepository: DataRepository, private val downloadImageController: DownloadImageController){

    suspend fun loadInitial(): Boolean{
        val success = dataController.loadInitial()
        if(success) {
            val imagesForDownload = dataRepository.getItemsForDownloading()
            if(imagesForDownload.isNotEmpty()){
                val images = ImageMapper().map(imagesForDownload)
                for (image in images){
                    downloadImageController.downloadImage(image)
                }
            }
        }
        return success
    }

    suspend fun loadPrevious(): Boolean{
        return dataController.loadPrevious()
    }

    suspend fun loadToday(): Boolean{
        return dataController.loadToday()
    }

    suspend fun downloadImages(): Boolean{
        Log.d("WORK", "downloadImage")
        val mapper = ImageMapper()
        return withContext (Dispatchers.IO){
            try {
                val forDownloads = dataRepository.getItemsForDownloading()
                mapper.map(forDownloads).forEach{
                    downloadImageController.downloadImage(it)
                }
                return@withContext true
            } catch (e: Exception){
                return@withContext false
            }
        }
    }

    fun getErrorInfo(): LiveData<String>{
        return dataController.getErrorInfo()
    }
}