package com.devapp.nasawallpaper.logic.livedata.images

import com.devapp.nasawallpaper.storage.database.models.DbEntityImage
import com.devapp.nasawallpaper.logic.entity.EntityImage
import java.util.*
import kotlin.collections.ArrayList

class ImagesMapper {
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
            image.isDeleted
        )
    }

    fun map(images: List<DbEntityImage>): List<EntityImage> {
        val result = ArrayList<EntityImage>()
        for (image in images) {
            result.add(map(image))
        }
     //   result.reverse()
        return result
    }
}