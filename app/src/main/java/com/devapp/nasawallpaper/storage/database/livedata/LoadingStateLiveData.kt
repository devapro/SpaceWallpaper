package com.devapp.nasawallpaper.storage.database.livedata

import androidx.lifecycle.LiveData
import androidx.room.InvalidationTracker
import com.devapp.nasawallpaper.storage.database.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadingStateLiveData(private val dataRepository: DataRepository): LiveData<Boolean>() {

    private val tables = arrayOf("wallpaper")

    private val observer = object : InvalidationTracker.Observer(tables) {
        override fun onInvalidated(tables: MutableSet<String>) {
           invalidate()
        }
    }

    override fun onInactive() {
        super.onInactive()
        dataRepository.addWeakObserver(this.observer)
        invalidate()
    }

    private fun invalidate(){
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                val unLoadedItems = dataRepository.getItemsForDownloading()
                postValue(unLoadedItems.isNotEmpty())
            }
        }
    }
}