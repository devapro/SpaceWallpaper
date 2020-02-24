package com.devapp.nasawallpaper.logic

import android.content.Context
import android.os.Environment
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

class AppStorage constructor(private val context: Context) {
    private var externalStorage: File? = null
    private var appDirectory: File? = null
    private var imagesDirectory: File? = null

    companion object{
        private const val APP_DIRECTORY_NAME = "Wallpapers"
        private const val IMAGES_DIRECTORY_NAME = "Images"
    }

    init{
        externalStorage = Environment.getExternalStorageDirectory()
    }


    fun getAppDirectory(): File? {
        if (!isExternalStorageWritable()) {
            return null
        }
        if (appDirectory == null) {
            appDirectory = File(externalStorage, APP_DIRECTORY_NAME)
        }
        return appDirectory
    }

    fun getImagesDirectory(): File? {
        val appDirectory = getAppDirectory() ?: return null
        if (imagesDirectory == null) {
            imagesDirectory = File(appDirectory, IMAGES_DIRECTORY_NAME)
        }
        if (imagesDirectory?.exists() == true) {
            return imagesDirectory
        }
        val success = imagesDirectory?.mkdirs() == true
        return if (success) {
            imagesDirectory
        } else null
    }

    fun getCacheDirectory(): File {
        return context.cacheDir
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED_READ_ONLY
    }

}