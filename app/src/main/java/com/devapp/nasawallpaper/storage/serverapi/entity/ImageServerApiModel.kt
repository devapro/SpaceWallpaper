package com.devapp.nasawallpaper.storage.serverapi.entity

data class ImageServerApiModel (
    val id: String,
    val url: String,
    val urlHd: String?,
    val name: String?,
    val description: String?,
    val mediaType: String,
    val type: Boolean,
    val date: String,
    val createAt: Long,
    val collection: String
)