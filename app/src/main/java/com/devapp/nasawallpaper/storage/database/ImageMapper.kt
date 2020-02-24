package com.devapp.nasawallpaper.storage.database

import com.devapp.nasawallpaper.storage.database.models.DbEntityImage
import com.devapp.nasawallpaper.storage.serverapi.entity.ImageServerApiModel
import java.util.*
import kotlin.collections.ArrayList

class ImageMapper {
    fun map(image: ImageServerApiModel): DbEntityImage {
        return DbEntityImage(
            0,
            image.name,
            0,
            image.description,
            image.collection,
            image.url,
            image.mediaType,
            null,
            image.urlHd,
            image.id,
            image.createAt,
            Date().time,
            false,
            false
        )
    }

    fun map(images: List<ImageServerApiModel>): List<DbEntityImage> {
        val result = ArrayList<DbEntityImage>()
        for (image in images) {
            result.add(map(image))
        }
        return result
    }
}