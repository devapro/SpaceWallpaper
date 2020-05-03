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
    private val dataRepository: DataRepository,
    private val downloadController: DownloadImageController,
    private val glideLoader: GlideDrawableLoader
) : BaseUseCase<Drawable?>() {

    private var entityImage: EntityImage? = null

    fun setEntityImage(entityImage: EntityImage): GetImageUseCase {
        this.entityImage = entityImage
        return this
    }

    override suspend fun run(): Drawable? {
        entityImage?.let {
            if(TextUtils.isEmpty(it.localPath)){
                downloadController.downloadImage(it)
                return null
            }
            val f = File(it.localPath)
            if (!f.exists()){
                downloadController.downloadImage(it)
                dataRepository.updateLocalPath(it.id, null)
                return null
            }
            return glideLoader.load(Uri.fromFile(f), 1000)
        }
        return null
    }
}