package com.devapp.nasawallpaper.logic.viewmodels

import android.app.Application
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devapp.nasawallpaper.App
import com.devapp.nasawallpaper.logic.controllers.DownloadImageController
import com.devapp.nasawallpaper.logic.entity.EntityImage
import com.devapp.nasawallpaper.logic.livedata.images.ImagesDataSourceFactory
import com.devapp.nasawallpaper.ui.customview.ImageList
import com.devapp.nasawallpaper.utils.imageLoader.GlideDrawableLoader
import java.io.File

class MainViewModel(private val app: Application, private val downloadController: DownloadImageController) : BaseViewModel(app) {
    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(20)
        .setPageSize(10)
        .setPrefetchDistance(5)
        .build()

    private val factory =
        ImagesDataSourceFactory((app as App).dataRepository)

    val pagedList : LiveData<PagedList<EntityImage>> = LivePagedListBuilder<Long, EntityImage>(factory, config)
        .setInitialLoadKey(null)
        .build()

    companion object {
        fun createFactory(application: Application, downloadController: DownloadImageController) : ViewModelProvider.Factory {
            return ViewModelFactory(application, downloadController)
        }
    }

    fun getImageListener() : ImageList.ActionListener {
        return object : ImageList.ActionListener {
            override suspend fun getImage(item: EntityImage): Drawable? {
                if(TextUtils.isEmpty(item.localPath)){
                    downloadController.downloadImage(item)
                    return null
                }
                val f = File(item.localPath)
                if (!f.exists()){
                    downloadController.downloadImage(item)
                    return null
                }
                val loader =
                    GlideDrawableLoader(
                        app.applicationContext
                    )
                return loader.load(Uri.fromFile(f), 1000)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val application: Application, private val downloadController: DownloadImageController): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = MainViewModel(application, downloadController)
            return viewModel as T
        }
    }
}
