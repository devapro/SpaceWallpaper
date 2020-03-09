package com.devapp.nasawallpaper.utils

import android.graphics.BitmapFactory
import android.widget.ImageView
import java.io.File

class UtilFiles {
    fun getFromPathWithCompress(
        imageView: ImageView,
        path: String,
        maxWidth: Int,
        maxHeight: Int
    ) {
        val imgFile = File(path)
        if (imgFile.exists()) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imgFile.absolutePath, options)
            val options2 = BitmapFactory.Options()
            options2.inSampleSize =
                calculateInSampleSize(
                    options,
                    maxWidth,
                    maxHeight
                )
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath, options2)
            imageView.setImageBitmap(myBitmap)
        }
    }
}