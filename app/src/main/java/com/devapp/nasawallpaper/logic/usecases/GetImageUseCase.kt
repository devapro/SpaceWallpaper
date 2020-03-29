package com.devapp.nasawallpaper.logic.usecases

import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import com.devapp.nasawallpaper.logic.controllers.DownloadImageController
import com.devapp.nasawallpaper.logic.entity.EntityImage
import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.utils.imageLoader.GlideDrawableLoader
import java.io.File

class GetImageUseCase(
    private val entityImage: EntityImage,
    private val dataRepository: DataRepository,
    private val downloadController: DownloadImageController,
    private val glideLoader: GlideDrawableLoader
) : BaseUseCase<Drawable?>() {
    override suspend fun run(): Drawable? {
        if(TextUtils.isEmpty(entityImage.localPath)){
            downloadController.downloadImage(entityImage)
            return null
        }
        val f = File(entityImage.localPath)
        if (!f.exists()){
            downloadController.downloadImage(entityImage)
            dataRepository.updateLocalPath(entityImage.id, null)
            return null
        }
        return glideLoader.load(Uri.fromFile(f), 1000)
    }
}