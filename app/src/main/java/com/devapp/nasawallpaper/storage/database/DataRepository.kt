package com.devapp.nasawallpaper.storage.database

import androidx.room.InvalidationTracker
import com.devapp.nasawallpaper.storage.database.models.DbEntityImage

interface DataRepository {
    fun saveItems(items: List<DbEntityImage>)
    fun saveItem(item: DbEntityImage)
    fun getLastUpdateTime(): Long?
    fun getLastItemTime(): Long?
    fun getImagesInitial(limit: Int): List<DbEntityImage>
    fun getImagesInitial(sinceDate: Long, limit: Int): List<DbEntityImage>
    fun getImagesBeforeDate(sinceDate: Long, limit: Int): List<DbEntityImage>
    fun getImagesAfterDate(sinceDate: Long, limit: Int): List<DbEntityImage>
    fun getAllItemsForWallpaper(limit: Int, minRate: Int): List<DbEntityImage>
    fun getItemsForDownloading(): List<DbEntityImage>
    fun updateLocalPath(id: Int, path: String?): Int
    fun getImageInfoById(id: Int): DbEntityImage?
    fun setRate(id: Int, rate: Int): Int
    fun updateViewCount(id: Int, count: Int)
    fun setDeleted(id: Int)
    fun addWeakObserver(observer: InvalidationTracker.Observer)
    fun getByFireId(fireId: String): DbEntityImage?
}