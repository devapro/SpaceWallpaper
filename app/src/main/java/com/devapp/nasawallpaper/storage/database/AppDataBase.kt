package com.devapp.nasawallpaper.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devapp.nasawallpaper.storage.database.models.DbEntityImage

@Database(entities = [DbEntityImage::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun dataDao(): AppDao
}