package com.devapp.nasawallpaper.logic.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devapp.nasawallpaper.logic.entity.EntityImage
import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.storage.database.ImageMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewDetailsViewModel(private val app: Application, private val dataRepository: DataRepository) : BaseViewModel(app) {

    var imageId: Int? = null
    set(value) {
        getImageInfo(value)
        field = value
    }

    private val mapper = ImageMapper()
    val imageInfo = MutableLiveData<EntityImage>()

    companion object {
        fun createFactory(
            application: Application,
            dataRepository: DataRepository
        ) : ViewModelProvider.Factory {
            return ViewModelFactory(application, dataRepository)
        }
    }

    private fun getImageInfo(imageId: Int?){
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                imageId?.let {
                    val imageDbEntity = dataRepository.getImageInfoById(imageId)
                    imageDbEntity?.let {
                        imageInfo.postValue(mapper.map(imageDbEntity))
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val application: Application, private val dataRepository: DataRepository): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = ViewDetailsViewModel(application, dataRepository)
            return viewModel as T
        }
    }
}
