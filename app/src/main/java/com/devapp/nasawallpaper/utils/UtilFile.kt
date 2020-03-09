package com.devapp.nasawallpaper.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

@Throws(IOException::class)
fun copy(src: File?, dst: File?) {
    FileInputStream(src).use { `in` ->
        FileOutputStream(dst).use { out ->
            // Transfer bytes from in to out
            val buf = ByteArray(1024)
            var len: Int
            while (`in`.read(buf).also { len = it } > 0) {
                out.write(buf, 0, len)
            }
        }
    }
}

fun getFileName(url: String?): String? {
    if (url == null) {
        return null
    }
    val cut = url.lastIndexOf('/')
    return if (cut != -1) {
        url.substring(cut + 1)
    } else url
}

fun getFileNameShort(name: String?): String? {
    if (name == null) {
        return null
    }
    val cut = name.lastIndexOf('.')
    return if (cut != -1) {
        name.substring(0, cut)
    } else name
}