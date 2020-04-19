package com.devapp.nasawallpaper.logic.controllers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.storage.serverapi.ImageMapper
import com.devapp.nasawallpaper.storage.serverapi.ServerApi
import com.devapp.nasawallpaper.storage.serverapi.entity.ImageServerApiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class DataController (private val dataRepository: DataRepository, private val api: ServerApi){

    private val mapper = ImageMapper()
    private val errorInfo = MutableLiveData<String>(null)

    suspend fun loadPrevious(): Boolean{
        Log.d("WORK", "loadPrevious")
        return withContext (Dispatchers.IO){
            val lastItemTime = dataRepository.getLastItemTime() ?: Date().time
            val date = Date(lastItemTime)
            for (i in 1..5){
                date.date = date.date - 1
                try {
                    val serverItems = api.loadNewPart(date.time)
                    if(serverItems.isEmpty()){
                        return@withContext true
                    }
                    saveItems(serverItems)
                } catch (e: Exception){
                    return@withContext true
                }
            }
            return@withContext true
        }
    }

    suspend fun loadToday(): Boolean{
        Log.d("WORK", "loadToday")
        return withContext (Dispatchers.IO){
            try {
                val serverItems = api.loadNewPart(Date().time)
                saveItems(serverItems)
                return@withContext true
            } catch (e: Exception){
                errorInfo.postValue(e.message)
                return@withContext false
            }
        }
    }

    suspend fun loadInitial(): Boolean{
        Log.d("WORK", "loadInitial")
        return withContext (Dispatchers.IO){
            val lastTimeUpdate = dataRepository.getLastItemTime()
            lastTimeUpdate?.let {
                return@withContext true
            }
            val initialItems = ArrayList<ImageServerApiModel>()
            val date = Date()
            date.date = date.date - 1
            for (i in 1..10){
                try {
                    val serverItems = api.loadNewPart(date.time)
                    if(serverItems.isEmpty()){
                        return@withContext false
                    }
                    initialItems.addAll(serverItems)
                    date.date = date.date - 1
                } catch (e: Exception){
                    errorInfo.postValue(e.message)
                    return@withContext false
                }
            }
            saveItems(initialItems)
            return@withContext true
        }
    }

    fun getErrorInfo(): LiveData<String> {
        return errorInfo
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