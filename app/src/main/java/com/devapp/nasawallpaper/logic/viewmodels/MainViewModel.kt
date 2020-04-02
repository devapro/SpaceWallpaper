package com.devapp.nasawallpaper.logic.viewmodels

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devapp.nasawallpaper.logic.controllers.DownloadImageController
import com.devapp.nasawallpaper.logic.entity.EntityImage
import com.devapp.nasawallpaper.logic.livedata.images.ImagesDataSourceFactory
import com.devapp.nasawallpaper.logic.usecases.GetImageUseCase
import com.devapp.nasawallpaper.logic.usecases.SetRateUseCase
import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.ui.customview.imageList.ImageList
import com.devapp.nasawallpaper.ui.fragments.MainFragmentDirections
import com.devapp.nasawallpaper.utils.imageLoader.GlideDrawableLoader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(
    private val app: Application,
    private val downloadController: DownloadImageController,
    private val dataRepository: DataRepository,
    private val nav: NavController
) : BaseViewModel(app) {
    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(20)
        .setPageSize(10)
        .setPrefetchDistance(5)
        .build()

    private val factory =
        ImagesDataSourceFactory(dataRepository)

    val pagedList : LiveData<PagedList<EntityImage>> = LivePagedListBuilder<Long, EntityImage>(factory, config)
        .setInitialLoadKey(null)
        .build()

    companion object {
        fun createFactory(
            application: Application,
            downloadController: DownloadImageController,
            dataRepository: DataRepository,
            nav: NavController
        ) : ViewModelProvider.Factory {
            return ViewModelFactory(application, downloadController, dataRepository, nav)
        }
    }

    fun getImageListener() : ImageList.ActionListener {
        return object : ImageList.ActionListener {
            override suspend fun getImage(imageInfo: EntityImage): Drawable? {
                val loader =
                    GlideDrawableLoader(
                        app.applicationContext
                    )
                val useCase = GetImageUseCase(imageInfo, dataRepository, downloadController, loader)
                return useCase.run()
            }

            override fun onImageClick(item: EntityImage) {
                val direction = MainFragmentDirections.actionMainFragmentToViewDetailsFragment(item.id)
                nav.navigate(direction)
            }

            override fun onClickUp(item: EntityImage) {
                GlobalScope.launch {
                    SetRateUseCase(dataRepository, item.id, 1).run()
                }
            }

            override fun onClickDown(item: EntityImage) {
                GlobalScope.launch {
                    SetRateUseCase(dataRepository, item.id, -1).run()
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(
        private val application: Application,
        private val downloadController: DownloadImageController,
        private val dataRepository: DataRepository,
        private val nav: NavController
    ): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = MainViewModel(application, downloadController, dataRepository, nav)
            return viewModel as T
        }
    }
}
