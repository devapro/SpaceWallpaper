package com.devapp.nasawallpaper.ui.customview

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.devapp.nasawallpaper.logic.entity.EntityImage

class ImagesListAdapter : PagedListAdapter<EntityImage, ImageViewHolder>(POST_COMPARATOR) {

    var actionListener: ImageList.ActionListener? = null

    companion object {
        val POST_COMPARATOR = ImageDiffUtil()
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ImageViewHolder(inflater.inflate(ImageViewHolder.LAYOUT_ID, parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = getItem(position)
        holder.onBind(image, listener)
    }

    override fun onBindViewHolder(
        holder: ImageViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val image = getItem(position)
        if(payloads.isNotEmpty()){
            if (payloads[0] is EntityImage.Changed){
                when(payloads[0]){
                    EntityImage.Changed.LOCAL_PATH_UPDATE -> {
                        holder.onBindMedia(image, listener)
                    }
                }
            }
        }
        else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id?.toLong() ?: 0L
    }

    override fun onViewRecycled(holder: ImageViewHolder) {
        super.onViewRecycled(holder)
        holder.onUnBind()
    }

    private val listener = object : ImageViewHolder.ActionListener{
        override suspend fun getImage(item: EntityImage): Drawable? {
            return actionListener?.getImage(item)
        }

        override fun onImageClick(item: EntityImage) {
            actionListener?.onImageClick(item)
        }
    }
}