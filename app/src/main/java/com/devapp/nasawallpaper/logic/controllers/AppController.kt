package com.devapp.nasawallpaper.logic.controllers

import androidx.lifecycle.LiveData
import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.storage.database.ImageMapper

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

    fun getErrorInfo(): LiveData<String>{
        return dataController.getErrorInfo()
    }
}