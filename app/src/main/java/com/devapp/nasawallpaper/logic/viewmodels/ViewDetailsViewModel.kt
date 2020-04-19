package com.devapp.nasawallpaper.logic.viewmodels

import android.app.Application
import android.app.DownloadManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devapp.nasawallpaper.logic.controllers.DownloadImageController
import com.devapp.nasawallpaper.logic.entity.EntityImage
import com.devapp.nasawallpaper.logic.usecases.GetImageUseCase
import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.storage.database.ImageMapper
import com.devapp.nasawallpaper.utils.imageLoader.GlideDrawableLoader
import kotlinx.coroutines.*
import java.io.File

class ViewDetailsViewModel(
    private val app: Application,
    private val dataRepository: DataRepository,
    private val downloadController: DownloadImageController
) : BaseViewModel(app) {

    var imageId: Int? = null
    set(value) {
        getImageInfo(value)
        field = value
    }

    private var downloadImageJob: Job? = null
    private val mapper = ImageMapper()

    val imageInfo = MutableLiveData<EntityImage>()
    val imageDrawable = MutableLiveData<Drawable>()

    companion object {
        fun createFactory(
            application: Application,
            dataRepository: DataRepository,
            downloadController: DownloadImageController
        ) : ViewModelProvider.Factory {
            return ViewModelFactory(application, dataRepository, downloadController)
        }
    }

    private fun getImageInfo(imageId: Int?){
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                imageId?.let {
                    val imageDbEntity = dataRepository.getImageInfoById(imageId)
                    imageDbEntity?.let {
                        val entityImage = mapper.map(imageDbEntity)
                        imageInfo.postValue(entityImage)
                        imageDrawable.postValue(getImageDrawable(entityImage))
                    }
                }
            }
        }
    }

    private suspend fun getImageDrawable(item: EntityImage): Drawable?{
        val loader = GlideDrawableLoader(app.applicationContext)
        val useCase = GetImageUseCase(item, dataRepository, downloadController, loader)
        return useCase.run()
    }

    override fun onCleared() {
        super.onCleared()
        downloadImageJob?.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val application: Application, private val dataRepository: DataRepository, private val downloadController: DownloadImageController): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = ViewDetailsViewModel(application, dataRepository, downloadController)
            return viewModel as T
        }
    }
}
