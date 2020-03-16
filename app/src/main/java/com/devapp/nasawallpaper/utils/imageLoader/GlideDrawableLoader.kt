package com.devapp.nasawallpaper.utils.imageLoader

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.Glide

class GlideDrawableLoader(private val context: Context) {
    fun load(uri: Uri, maxSize: Int): Drawable? {
        val fileFutureTarget = Glide.with(context)
            .asDrawable()
            .load(uri)
            .override(maxSize, maxSize)
            .fitCenter()
            .submit()
        return try {
            fileFutureTarget.get()
        } catch (ex: Exception) {
            loadAsGif(uri, maxSize)
        }
    }

    private fun loadAsGif(
        uri: Uri,
        maxSize: Int
    ): Drawable? {
        val fileFutureTarget = Glide.with(context)
            .asGif()
            .load(uri)
            .override(maxSize, maxSize)
            .centerCrop()
            .submit()
        return try {
            fileFutureTarget.get()
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}