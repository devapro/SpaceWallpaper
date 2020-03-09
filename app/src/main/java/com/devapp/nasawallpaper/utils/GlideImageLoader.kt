package com.devapp.nasawallpaper.utils

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import java.io.File

class GlideImageLoader(private val listener: ActionListener, private val context: Context) {
    interface ActionListener {
        fun onStart()
        fun onProgress(progress: Int)
        fun onEnd(cacheFile: File?)
    }

    fun loadToFile(url: String) {
        GlideAppModule.expect(url, object : GlideAppModule.UIonProgressListener {
           override fun onProgress(bytesRead: Long, expectedLength: Long) {
                val progress = (100 * bytesRead / expectedLength).toInt()
                listener.onProgress(progress)
            }

           override val granualityPercentage: Float
                get() = 1.0f
        })
        val fileFutureTarget = Glide.with(context)
            .asFile()
            .load(url)
            .priority(Priority.HIGH)
            .submit()
        try {
            val resource = fileFutureTarget.get()
            GlideAppModule.forget(url)
            listener.onEnd(resource)
        } catch (e: Exception) {
            e.printStackTrace()
            GlideAppModule.forget(url)
            listener.onEnd(null)
        }
    }
}