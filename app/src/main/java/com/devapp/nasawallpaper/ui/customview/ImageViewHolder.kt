package com.devapp.nasawallpaper.ui.customview

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.devapp.nasawallpaper.R

class ImageViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView) {
    companion object{
        val LAYOUT_ID = R.layout.view_item_image
    }
    val container = itemView.findViewById<RelativeLayout>(R.id.container)
    val image = itemView.findViewById<ImageView>(R.id.image)
    val progress = itemView.findViewById<ContentLoadingProgressBar>(R.id.progress)
}