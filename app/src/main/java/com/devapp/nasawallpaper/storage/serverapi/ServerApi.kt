package com.devapp.nasawallpaper.storage.serverapi

import com.devapp.nasawallpaper.storage.serverapi.entity.ImageServerApiModel

interface ServerApi{
    suspend fun loadNewPart(lastUpdate: Long?) : List<ImageServerApiModel>
}