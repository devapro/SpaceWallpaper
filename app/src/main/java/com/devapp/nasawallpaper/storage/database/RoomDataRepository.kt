package com.devapp.nasawallpaper.storage.database

import android.annotation.SuppressLint
import androidx.room.InvalidationTracker
import com.devapp.nasawallpaper.storage.database.models.DbEntityImage

class RoomDataRepository(private val appDataBase: AppDataBase) : DataRepository{

    override fun updateLocalPath(id: Int, path: String?): Int {
        return appDataBase.dataDao().updateLocalPath(id, path)
    }

    override fun getImageInfoById(id: Int): DbEntityImage? {
        return appDataBase.dataDao().getImageInfoById(id)
    }

    override fun setRate(id: Int, rate: Int): Int {
        return appDataBase.dataDao().setRate(id, rate)
    }

    override fun saveItems(items: List<DbEntityImage>) {
        appDataBase.dataDao().insert(items)
    }

    override fun saveItem(item: DbEntityImage) {
        appDataBase.dataDao().insert(item)
    }

    override fun getLastUpdateTime(): Long? {
        return appDataBase.dataDao().getLastUpdateTime()
    }

    override fun getLastItemTime(): Long? {
        return appDataBase.dataDao().getLastItemTime()
    }

    // for live data
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
    // end

    override fun getAllItemsForWallpaper(limit: Int, minRate: Int): List<DbEntityImage> {
        return appDataBase.dataDao().getAllItemsForWallpaper(limit, minRate)
    }

    override fun getItemsForDownloading(): List<DbEntityImage> {
        return appDataBase.dataDao().getItemsForDownloading()
    }

    override fun updateViewCount(id: Int, count: Int) {
         appDataBase.dataDao().updateViewCount(id, count)
    }

    override fun setDeleted(id: Int) {
         appDataBase.dataDao().setDeleted(id)
    }

    override fun getByFireId(fireId: String): DbEntityImage? {
        return appDataBase.dataDao().getWallpaperByFireId(fireId)
    }

    @SuppressLint("RestrictedApi")
    override fun addWeakObserver(observer: InvalidationTracker.Observer) {
        appDataBase.invalidationTracker.addWeakObserver(observer)
    }

}