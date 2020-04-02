package com.devapp.nasawallpaper.storage.database

import com.devapp.nasawallpaper.storage.database.models.DbEntityImage
import com.devapp.nasawallpaper.logic.entity.EntityImage
import kotlin.collections.ArrayList

class ImageMapper {
    fun map(image: DbEntityImage): EntityImage {
        return EntityImage(
            image.id ?: 0,
            image.name,
            image.showCount,
            image.description,
            image.collection,
            image.url,
            image.type,
            image.localPath,
            image.urlHd,
            image.createdAt,
            image.isDeleted,
            image.rate
        )
    }

    fun map(images: List<DbEntityImage>): List<EntityImage> {
        val result = ArrayList<EntityImage>()
        for (image in images) {
            result.add(map(image))
        }
        return result
    }
}