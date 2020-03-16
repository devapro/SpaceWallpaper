package com.devapp.nasawallpaper.logic.controllers

import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.storage.serverapi.ImageMapper
import com.devapp.nasawallpaper.storage.serverapi.ServerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataController (private val dataRepository: DataRepository, private val api: ServerApi){

    private val mapper = ImageMapper()

    suspend fun load(){
        withContext (Dispatchers.IO){
            val lastItemTime = dataRepository.getLastItemTime()
            val serverItems = api.loadNewPart(lastItemTime)
            val dbItems = mapper.map(serverItems)
            dataRepository.saveItems(dbItems)
        }
    }
}