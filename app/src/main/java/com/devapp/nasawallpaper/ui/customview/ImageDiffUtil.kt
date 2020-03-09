package com.devapp.nasawallpaper.ui.customview

import androidx.recyclerview.widget.DiffUtil
import com.devapp.nasawallpaper.logic.entity.EntityImage

class ImageDiffUtil: DiffUtil.ItemCallback<EntityImage>() {

    override fun areItemsTheSame(oldItem: EntityImage, newItem: EntityImage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EntityImage, newItem: EntityImage): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: EntityImage, newItem: EntityImage): Any? {
        return oldItem.getChanged(newItem)
    }

}