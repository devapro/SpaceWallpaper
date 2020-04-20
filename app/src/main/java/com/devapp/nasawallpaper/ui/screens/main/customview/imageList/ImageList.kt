package com.devapp.nasawallpaper.ui.screens.main.customview.imageList

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devapp.nasawallpaper.logic.entity.EntityImage

class ImageList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    var actionListener : ActionListener? = null
    set(value) {
        (adapter as ImagesListAdapter).actionListener = value
        field = value
    }

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        itemAnimator = DefaultItemAnimator()
        setHasFixedSize(true)
        adapter =
            ImagesListAdapter()
    }

    fun submitList(list: PagedList<EntityImage>){
        (adapter as ImagesListAdapter).submitList(list)
    }

    interface ActionListener{
        suspend fun getImage(item: EntityImage): Drawable?
        fun onImageClick(item: EntityImage)
        fun onClickUp(item: EntityImage)
        fun onClickDown(item: EntityImage)
    }
}