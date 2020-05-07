package com.devapp.nasawallpaper.storage.database.livedata

import androidx.lifecycle.LiveData
import androidx.room.InvalidationTracker
import com.devapp.nasawallpaper.storage.database.DataRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoadingStateLiveData(private val dataRepository: DataRepository): LiveData<Boolean>(), CoroutineScope {

    private var coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    private val tables = arrayOf("wallpaper")

    private val observer = object : InvalidationTracker.Observer(tables) {
        override fun onInvalidated(tables: MutableSet<String>) {
           invalidate()
        }
    }

    override fun onActive() {
        super.onActive()
        dataRepository.addWeakObserver(this.observer)
        invalidate()
    }

    override fun onInactive() {
        super.onInactive()
        coroutineJob.cancelChildren()
    }

    private fun invalidate(){
        CoroutineScope(coroutineContext).launch {
            withContext(Dispatchers.IO){
                val unLoadedItems = dataRepository.getItemsForDownloading()
                postValue(unLoadedItems.isNotEmpty())
            }
        }
    }
}