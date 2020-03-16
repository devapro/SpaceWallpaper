package com.devapp.nasawallpaper.ui.customview

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.devapp.nasawallpaper.R
import com.devapp.nasawallpaper.logic.entity.EntityImage
import kotlinx.coroutines.*

class ImageViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView) {
    companion object{
        val LAYOUT_ID = R.layout.view_item_image
    }
    private val container = itemView.findViewById<RelativeLayout>(R.id.container)
    private val image = itemView.findViewById<ImageView>(R.id.image)
    private val progress = itemView.findViewById<ContentLoadingProgressBar>(R.id.progress)

    private var job: Job? = null

    fun onBind(entityImage: EntityImage?, listener: ActionListener){
        progress.show()
        onBindMedia(entityImage, listener)
    }

    fun onBindMedia(entityImage: EntityImage?, listener: ActionListener){
        entityImage?.run {
            job = GlobalScope.launch {
                withContext(Dispatchers.IO){
                    val drawable = listener.getImage(entityImage)
                    image.post {
                        image.setImageDrawable(drawable)
                        progress.hide()
                    }
                }
            }
        }

        image.setOnClickListener {
            entityImage?.let { listener.onImageClick(entityImage) }
        }
    }

    fun onUnBind(){
        job?.cancel()
        progress.show()
    }

    interface ActionListener{
        suspend fun getImage(item: EntityImage): Drawable?
        fun onImageClick(item: EntityImage)
    }
}