package com.devapp.nasawallpaper.logic.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devapp.nasawallpaper.App
import com.devapp.nasawallpaper.logic.entity.EntityImage
import com.devapp.nasawallpaper.logic.livedata.images.ImagesDataSourceFactory

class MainViewModel(application: Application) : BaseViewModel(application) {
    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(20)
        .setPageSize(10)
        .setPrefetchDistance(5)
        .build()

    private val factory =
        ImagesDataSourceFactory((application as App).dataRepository)

    val pagedList : LiveData<PagedList<EntityImage>> = LivePagedListBuilder<Long, EntityImage>(factory, config)
        .setInitialLoadKey(null)
        .build()

}
