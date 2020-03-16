package com.devapp.nasawallpaper.utils.imageLoader

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.devapp.nasawallpaper.utils.GlideAppModule

class GlideImageLoader(private val listener: ImageLoader.ActionListener, private val context: Context) : ImageLoader {

    override fun loadToFile(url: String) {
        GlideAppModule.expect(
            url,
            object :
                GlideAppModule.UIonProgressListener {
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