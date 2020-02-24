package com.devapp.nasawallpaper.ui.customview

import android.content.Context
import android.util.AttributeSet
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devapp.nasawallpaper.logic.entity.EntityImage

class ImageList(contetxt: Context, attrs: AttributeSet?, defStyle: Int) : RecyclerView(contetxt, attrs, defStyle) {
    constructor(contetxt: Context, attrs: AttributeSet?) : this(contetxt, attrs, 0)
    constructor(contetxt: Context) : this(contetxt, null, 0)

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        itemAnimator = DefaultItemAnimator()
        setHasFixedSize(true)
        adapter = ImagesListAdapter()
    }

    fun submitList(list: PagedList<EntityImage>){
        (adapter as ImagesListAdapter).submitList(list)
    }

    fun setActionListener(actionListener: ImagesListAdapter.ActionListener){
        (adapter as ImagesListAdapter).actionListener = actionListener
    }
}