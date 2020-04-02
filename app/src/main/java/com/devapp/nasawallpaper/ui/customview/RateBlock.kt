package com.devapp.nasawallpaper.ui.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.devapp.nasawallpaper.R
import kotlinx.android.synthetic.main.view_rate_block.view.*

class RateBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle){

    var actionListener: ActionListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_rate_block, this, true)
        upBtn.setOnClickListener { actionListener?.onClickUp() }
        downBtn.setOnClickListener { actionListener?.onClickDown() }
    }

    fun setRate(rate: Int){
        when(rate){
            1 -> {
                upBtn.setBackgroundColor(ContextCompat.getColor(upBtn.context, R.color.colorAccent))
                downBtn.setBackgroundColor(Color.TRANSPARENT)
            }
            -1 -> {
                downBtn.setBackgroundColor(ContextCompat.getColor(upBtn.context, R.color.colorAccent))
                upBtn.setBackgroundColor(Color.TRANSPARENT)
            }
            else -> {
                downBtn.setBackgroundColor(Color.TRANSPARENT)
                upBtn.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    interface ActionListener{
        fun onClickUp()
        fun onClickDown()
    }
}