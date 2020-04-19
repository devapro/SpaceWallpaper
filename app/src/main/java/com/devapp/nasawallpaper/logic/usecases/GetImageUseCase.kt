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
        entityImage?.apply {
            if(TextUtils.isEmpty(this.localPath)){
                downloadController.downloadImage(this)
                return null
            }
            val f = File(this.localPath)
            if (!f.exists()){
                downloadController.downloadImage(this)
                dataRepository.updateLocalPath(this.id, null)
                return null
            }
            return glideLoader.load(Uri.fromFile(f), 1000)
        }
        return null
    }
}