package com.devapp.nasawallpaper.storage.database

import android.annotation.SuppressLint
import androidx.room.InvalidationTracker
import com.devapp.nasawallpaper.storage.database.models.DbEntityImage

class RoomDataRepository(private val appDataBase: AppDataBase) : DataRepository{
    override fun updateLocalPath(id: Int, path: String): Int {
        return appDataBase.dataDao().updateLocalPath(id, path)
    }

    override fun saveItems(items: List<DbEntityImage>) {
        appDataBase.dataDao().insert(items)
    }

    override fun getLastUpdateTime(): Long? {
        return appDataBase.dataDao().getLastUpdateTime()
    }

    override fun getLastItemTime(): Long? {
        return appDataBase.dataDao().getLastItemTime()
    }

    override fun getImagesInitial(limit: Int): List<DbEntityImage> {
        return appDataBase.dataDao().getImagesInitial(limit)
    }

    override fun getImagesInitial(sinceDate: Long, limit: Int): List<DbEntityImage> {
        return appDataBase.dataDao().getImagesInitial(sinceDate, limit)
    }

    override fun getImagesBeforeDate(sinceDate: Long, limit: Int): List<DbEntityImage> {
        return appDataBase.dataDao().getImagesBeforeDate(sinceDate, limit)
    }

    override fun getImagesAfterDate(sinceDate: Long, limit: Int): List<DbEntityImage> {
        return appDataBase.dataDao().getImagesAfterDate(sinceDate, limit)
    }

    @SuppressLint("RestrictedApi")
    override fun addWeakObserver(observer: InvalidationTracker.Observer) {
        appDataBase.invalidationTracker.addWeakObserver(observer)
    }

}