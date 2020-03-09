package com.devapp.nasawallpaper.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devapp.nasawallpaper.logic.entity.EntityImage

class ImageList(contetxt: Context, attrs: AttributeSet?, defStyle: Int) : RecyclerView(contetxt, attrs, defStyle) {
    constructor(contetxt: Context, attrs: AttributeSet?) : this(contetxt, attrs, 0)
    constructor(contetxt: Context) : this(contetxt, null, 0)

    var actionListener : ActionListener? = null

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        itemAnimator = DefaultItemAnimator()
        setHasFixedSize(true)
        adapter = ImagesListAdapter(object : ImagesListAdapter.ActionListener {
            override suspend fun getImage(item: EntityImage): Drawable? {
                return actionListener?.getImage(item)
            }
        })
    }

    fun submitList(list: PagedList<EntityImage>){
        (adapter as ImagesListAdapter).submitList(list)
    }

    interface ActionListener{
        suspend fun getImage(item: EntityImage): Drawable?
    }
}