package com.devapp.nasawallpaper.storage.database

import androidx.room.InvalidationTracker
import com.devapp.nasawallpaper.storage.database.models.DbEntityImage

interface DataRepository {
    fun saveItems(items: List<DbEntityImage>)
    fun getLastUpdateTime(): Long?
    fun getLastItemTime(): Long?
    fun getImagesInitial(limit: Int): List<DbEntityImage>
    fun getImagesInitial(sinceDate: Long, limit: Int): List<DbEntityImage>
    fun getImagesBeforeDate(sinceDate: Long, limit: Int): List<DbEntityImage>
    fun getImagesAfterDate(sinceDate: Long, limit: Int): List<DbEntityImage>
    fun updateLocalPath(id: Int, path: String): Int
    fun addWeakObserver(observer: InvalidationTracker.Observer)
}