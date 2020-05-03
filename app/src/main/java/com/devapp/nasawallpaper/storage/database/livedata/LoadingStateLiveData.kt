package com.devapp.nasawallpaper.storage.database.livedata

import androidx.lifecycle.LiveData
import androidx.room.InvalidationTracker
import com.devapp.nasawallpaper.storage.database.DataRepository

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
        val unLoadedItems = dataRepository.getItemsForDownloading()
        value = unLoadedItems.isNotEmpty()
    }
}