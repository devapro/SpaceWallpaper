package com.devapp.nasawallpaper.logic.controllers

import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.storage.serverapi.ImageMapper
import com.devapp.nasawallpaper.storage.serverapi.ServerApi
import com.devapp.nasawallpaper.storage.serverapi.entity.ImageServerApiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class DataController (private val dataRepository: DataRepository, private val api: ServerApi){

    private val mapper = ImageMapper()

    suspend fun loadPrevious(): Boolean{
        return withContext (Dispatchers.IO){
            for (i in 1..5){
                val lastItemTime = dataRepository.getLastItemTime()
                val serverItems = api.loadNewPart(lastItemTime)
                saveItems(serverItems)
            }
            return@withContext true
        }
    }

    suspend fun loadToday(): Boolean{
        return withContext (Dispatchers.IO){
            val serverItems = api.loadNewPart(Date().time)
            if(serverItems.isEmpty()){
                return@withContext false
            }
            saveItems(serverItems)
            return@withContext true
        }
    }

    suspend fun loadInitial(): Boolean{
        return withContext (Dispatchers.IO){
            val lastTimeUpdate = dataRepository.getLastItemTime()
            lastTimeUpdate?.let {
                return@withContext true
            }
            val initialItems = ArrayList<ImageServerApiModel>()
            val date = Date()
            date.date = date.date - 1
            for (i in 1..10){
                val serverItems = api.loadNewPart(date.time)
                if(serverItems.isEmpty()){
                    return@withContext false
                }
                initialItems.addAll(serverItems)
                date.date = date.date - 1
            }
            saveItems(initialItems)
            return@withContext true
        }
    }

    private fun saveItems(serverItems: List<ImageServerApiModel>){
        val dbItems = mapper.map(serverItems)

        for (item in dbItems){
            val localItem = dataRepository.getByFireId(item.fireId!!)
            if(localItem == null) {
                dataRepository.saveItem(item)
            }
        }
    }
}