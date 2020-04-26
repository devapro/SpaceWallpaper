package com.devapp.nasawallpaper.ui.screens.details

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devapp.nasawallpaper.logic.entity.EntityImage
import com.devapp.nasawallpaper.logic.usecases.GetImageUseCase
import com.devapp.nasawallpaper.logic.BaseViewModel
import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.storage.database.ImageMapper
import kotlinx.coroutines.*

class ViewDetailsViewModel(
    app: Application,
    private val dataRepository: DataRepository,
    private val getImageUseCase: GetImageUseCase
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
        return getImageUseCase.setEntityImage(item).run()
    }

    override fun onCleared() {
        super.onCleared()
        downloadImageJob?.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val application: Application, private val dataRepository: DataRepository, private val getImageUseCase: GetImageUseCase): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel =
                ViewDetailsViewModel(
                    application,
                    dataRepository,
                    getImageUseCase
                )
            return viewModel as T
        }
    }
}
