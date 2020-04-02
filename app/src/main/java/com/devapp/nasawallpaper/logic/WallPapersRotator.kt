package com.devapp.nasawallpaper.logic

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.devapp.nasawallpaper.logic.controllers.DownloadImageController
import com.devapp.nasawallpaper.storage.database.ImageMapper
import com.devapp.nasawallpaper.storage.database.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class WallPapersRotator(private val dataRepository: DataRepository, private val downloadImageController: DownloadImageController) {
    var currentBitmap : Bitmap? = null
    private val mapper = ImageMapper()
    suspend fun getNextImage() : Boolean {
        return withContext(Dispatchers.IO){
            val lastItems = dataRepository.getAllItems(100, 0)
            if(lastItems.isNotEmpty()){
                val random = lastItems.sortedBy { it.showCount }
                val item = random[0]
                Log.d("WallPapersRotator", "id " + item.id + " url " + item.localPath)
                val blob = ByteArrayOutputStream()
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888

                if(item.localPath == null){
                    downloadImageController.downloadImage(mapper.map(item))
                }

                val bitmap = BitmapFactory.decodeFile(item.localPath, options)
                if(bitmap != null){
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, blob)
                    currentBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false)
                    dataRepository.updateViewCount(item.id,
                        (item.showCount?.plus(1) ?: 0)
                    )
                    return@withContext true
                }
                dataRepository.setDeleted(item.id)
            }
            return@withContext false
        }
    }
}