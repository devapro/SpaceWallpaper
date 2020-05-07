package com.devapp.nasawallpaper.ui.screens.main.customview.imageList

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.devapp.nasawallpaper.R
import com.devapp.nasawallpaper.logic.entity.EntityImage
import com.devapp.nasawallpaper.ui.screens.main.customview.RateBlock
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ImageViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView), CoroutineScope {
    companion object{
        val LAYOUT_ID = R.layout.item_image
    }

    private var coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob


    private val container = itemView.findViewById<RelativeLayout>(R.id.container)
    private val image = itemView.findViewById<ImageView>(R.id.image)
    private val progress = itemView.findViewById<ContentLoadingProgressBar>(R.id.progress)
    private val rate = itemView.findViewById<RateBlock>(R.id.rate)

    fun onBind(entityImage: EntityImage?, listener: ActionListener){
        onBindMedia(entityImage, listener)
        onBindRate(entityImage, listener)
    }

    fun onBindRate(entityImage: EntityImage?, listener: ActionListener){
        rate.setRate(entityImage?.rate ?: 0)
        rate.actionListener = object : RateBlock.ActionListener {
            override fun onClickUp() {
                entityImage?.let { listener.onClickUp(entityImage) }
            }

            override fun onClickDown() {
                entityImage?.let { listener.onClickDown(entityImage) }
            }
        }
    }

    fun onBindMedia(entityImage: EntityImage?, listener: ActionListener){
        progress.show()
        image.setImageDrawable(ContextCompat.getDrawable(image.context, R.drawable.image_placeholder))
        entityImage?.run {
            CoroutineScope(coroutineContext).launch {
                withContext(Dispatchers.IO){
                    val drawable = listener.getImage(entityImage)
                    drawable?.let {
                        image.post {
                            image.setImageDrawable(it)
                            progress.hide()
                        }
                    }
                }
            }
        }

        image.setOnClickListener {
            entityImage?.let { listener.onImageClick(entityImage) }
        }
    }

    fun onUnBind(){
        coroutineJob.cancelChildren()
        progress.show()
        rate.setRate(0)
        image.setImageDrawable(ContextCompat.getDrawable(image.context, R.drawable.image_placeholder))
    }

    interface ActionListener{
        suspend fun getImage(item: EntityImage): Drawable?
        fun onImageClick(item: EntityImage)
        fun onClickUp(item: EntityImage)
        fun onClickDown(item: EntityImage)
    }
}