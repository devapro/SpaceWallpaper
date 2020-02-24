package com.devapp.nasawallpaper.logic.livedata.images

import androidx.paging.DataSource
import com.devapp.nasawallpaper.storage.database.AppDataBase
import com.devapp.nasawallpaper.logic.entity.EntityImage
import com.devapp.nasawallpaper.storage.database.DataRepository

class ImagesDataSourceFactory (private val dataRepository: DataRepository) : DataSource.Factory<Long, EntityImage>() {

    override fun create(): DataSource<Long, EntityImage> {
        return ImagesDataSource(
            dataRepository
        )
    }

}