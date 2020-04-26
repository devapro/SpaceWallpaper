package com.devapp.nasawallpaper.ui.screens.main

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devapp.nasawallpaper.logic.entity.EntityImage
import com.devapp.nasawallpaper.logic.livedata.images.ImagesDataSourceFactory
import com.devapp.nasawallpaper.logic.usecases.GetImageUseCase
import com.devapp.nasawallpaper.logic.usecases.SetRateUseCase
import com.devapp.nasawallpaper.logic.BaseViewModel
import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.ui.screens.main.customview.imageList.ImageList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(
    app: Application,
    private val getImageUseCase: GetImageUseCase,
    private val setRateUseCase: SetRateUseCase,
    dataRepository: DataRepository,
    private val nav: NavController
) : BaseViewModel(app) {
    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(20)
        .setPageSize(10)
        .setPrefetchDistance(5)
        .build()

    private val factory = ImagesDataSourceFactory(dataRepository)

    val pagedList : LiveData<PagedList<EntityImage>> = LivePagedListBuilder<Long, EntityImage>(factory, config)
        .setInitialLoadKey(null)
        .build()

    fun getImageListener() : ImageList.ActionListener {
        return object : ImageList.ActionListener {
            override suspend fun getImage(imageInfo: EntityImage): Drawable? {
                return getImageUseCase.setEntityImage(imageInfo).run()
            }

            override fun onImageClick(item: EntityImage) {
                val direction = MainFragmentDirections.actionMainFragmentToViewDetailsFragment(item.id)
                nav.navigate(direction)
            }

            override fun onClickUp(item: EntityImage) {
                GlobalScope.launch {
                    setRateUseCase.setId(item.id).setRate(1).run()
                }
            }

            override fun onClickDown(item: EntityImage) {
                GlobalScope.launch {
                    setRateUseCase.setId(item.id).setRate(-1).run()
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(
        private val application: Application,
        private val  getImageUseCase: GetImageUseCase,
        private val  setRateUseCase: SetRateUseCase,
        private val dataRepository: DataRepository,
        private val nav: NavController
    ): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel =
                MainViewModel(
                    application,
                    getImageUseCase,
                    setRateUseCase,
                    dataRepository,
                    nav
                )
            return viewModel as T
        }
    }
}
