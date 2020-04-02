package com.devapp.nasawallpaper.storage.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devapp.nasawallpaper.storage.database.models.DbEntityImage

@Dao
interface AppDao {
    @Query("SELECT createdAt FROM wallpaper ORDER BY createdAt DESC LIMIT 1")
    fun getLastUpdateTime(): Long?

    @Query("SELECT createdAtServer FROM wallpaper ORDER BY createdAt DESC LIMIT 1")
    fun getLastItemTime(): Long?

    @Query("SELECT * FROM wallpaper WHERE fireId = :fireId LIMIT 1")
    fun getWallpaperByFireId(fireId: String): DbEntityImage?

    @Query("SELECT * FROM wallpaper")
    fun getAllWallpapers(): List<DbEntityImage>
    /////
    @Query("SELECT * FROM wallpaper ORDER BY _id DESC LIMIT :limit")
    fun getImagesInitial(limit: Int): List<DbEntityImage>

    @Query("SELECT * FROM wallpaper WHERE _id < :sinceDate ORDER BY _id DESC LIMIT :limit")
    fun getImagesInitial(sinceDate: Long, limit: Int): List<DbEntityImage>

    @Query("SELECT * FROM wallpaper WHERE _id > :sinceDate ORDER BY _id ASC LIMIT :limit")
    fun getImagesBeforeDate(sinceDate: Long, limit: Int): List<DbEntityImage>

    @Query("SELECT * FROM wallpaper WHERE _id < :sinceDate ORDER BY _id DESC LIMIT :limit")
    fun getImagesAfterDate(sinceDate: Long, limit: Int): List<DbEntityImage>
    ////
    @Query("SELECT * FROM wallpaper WHERE localPath = '' OR localPath IS NULL")
    fun getNotDownloadWallpaper(): List<DbEntityImage>

    @Query("SELECT * FROM wallpaper WHERE localPath <> '' AND localPath IS NOT NULL AND isDeleted = 0 AND rate >= :minRate ORDER BY createdAt DESC LIMIT :limit")
    fun getNewestItems(limit: Int, minRate: Int): List<DbEntityImage>

    @Query("UPDATE wallpaper SET localPath = :path WHERE _id = :id")
    fun updateLocalPath(id: Int, path: String?): Int

    @Query("UPDATE wallpaper SET showCount = :newCount WHERE _id = :id")
    fun updateViewCount(id: Int, newCount: Int): Int

    @Query("UPDATE wallpaper SET isDeleted = 1 WHERE _id = :id")
    fun setDeleted(id: Int): Int

    @Query("UPDATE wallpaper SET rate = :rate WHERE _id = :id")
    fun setRate(id: Int, rate: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: DbEntityImage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<DbEntityImage>)

    @Query("SELECT * FROM wallpaper WHERE _id = :id LIMIT 1")
    fun getImageInfoById(id: Int): DbEntityImage?
}