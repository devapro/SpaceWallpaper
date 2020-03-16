package com.devapp.nasawallpaper.utils.imageLoader

import java.io.File

interface ImageLoader {
    interface ActionListener {
        fun onStart()
        fun onProgress(progress: Int)
        fun onEnd(cacheFile: File?)
    }

    fun loadToFile(url: String)
}