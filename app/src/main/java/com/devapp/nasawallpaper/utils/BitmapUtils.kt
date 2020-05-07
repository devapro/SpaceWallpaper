package com.devapp.nasawallpaper.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix

fun getResizedBitmap(
    bm: Bitmap,
    newWidth: Int,
    newHeight: Int,
    isNecessaryToKeepOrig: Boolean
): Bitmap {
    val width = bm.width
    val height = bm.height
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    // CREATE A MATRIX FOR THE MANIPULATION
    val matrix = Matrix()
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight)
    // "RECREATE" THE NEW BITMAP
    val resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
    if (!isNecessaryToKeepOrig) {
        bm.recycle()
    }
    return resizedBitmap
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}
