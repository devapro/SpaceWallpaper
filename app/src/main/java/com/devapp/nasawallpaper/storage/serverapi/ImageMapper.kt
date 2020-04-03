package com.devapp.nasawallpaper.storage.serverapi

import com.devapp.nasawallpaper.storage.database.models.DbEntityImage
import com.devapp.nasawallpaper.storage.serverapi.entity.ImageServerApiModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ImageMapper {
    fun map(image: ImageServerApiModel): DbEntityImage {
        val date = SimpleDateFormat("YYYY-MM-dd").parse(image.date)
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
            date.time,
            Date().time,
            false,
            false,
            0
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