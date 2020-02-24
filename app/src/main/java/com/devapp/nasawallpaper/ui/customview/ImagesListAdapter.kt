package com.devapp.nasawallpaper.ui.customview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.devapp.nasawallpaper.UtilFiles
import com.devapp.nasawallpaper.logic.entity.EntityImage

class ImagesListAdapter : PagedListAdapter<EntityImage, ImageViewHolder>(POST_COMPARATOR) {

    var actionListener: ActionListener? = null

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
        image?.localPath?.run {
            UtilFiles().getFromPathWithCompress(
                holder.image,
                this,
                1000,
                1000
            )
            holder.progress.hide()
        }
        image?.run {
            if(localPath == null){
                holder.progress.show()
                actionListener?.downloadImage(image)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id?.toLong() ?: 0L
    }

    interface ActionListener{
        fun downloadImage(item: EntityImage)
    }
}