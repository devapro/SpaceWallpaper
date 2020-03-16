package com.devapp.nasawallpaper.logic.livedata.images

import androidx.paging.ItemKeyedDataSource
import androidx.room.InvalidationTracker
import com.devapp.nasawallpaper.storage.database.models.DbEntityImage
import com.devapp.nasawallpaper.logic.entity.EntityImage
import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.storage.database.ImageMapper

class ImagesDataSource (private val dataRepository: DataRepository) : ItemKeyedDataSource<Long, EntityImage>(){

    private val mapper = ImageMapper()

    private val tables = arrayOf("wallpaper")

    private val observer = object : InvalidationTracker.Observer(tables) {
        override fun onInvalidated(tables: MutableSet<String>) {
            this@ImagesDataSource.invalidate()
        }
    }

    init {
        dataRepository.addWeakObserver(this.observer)
    }

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<EntityImage>
    ) {
        val sinceDate = params.requestedInitialKey ?: 0L
        if(sinceDate == 0L){
            val result = dataRepository.getImagesInitial(params.requestedLoadSize)
            callback.onResult(mapper.map(result))
        } else {
            val resultInitial = dataRepository.getImagesInitial(sinceDate, params.requestedLoadSize)
            val resultBefore = dataRepository.getImagesBeforeDate(sinceDate, params.requestedLoadSize)
            val result = ArrayList<DbEntityImage>()
            result.addAll(resultBefore.reversed())
            result.addAll(resultInitial)
            callback.onResult(mapper.map(result))
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<EntityImage>) {
        val sinceDate = params.key ?: 0
        val result = dataRepository.getImagesAfterDate(sinceDate, params.requestedLoadSize)
        callback.onResult(mapper.map(result))
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<EntityImage>) {
        val sinceDate = params.key ?: 0
        val result = dataRepository.getImagesBeforeDate(sinceDate, params.requestedLoadSize)
        callback.onResult(mapper.map(result.reversed()))
    }

    override fun getKey(item: EntityImage): Long {
        return item.id.toLong()
    }

}