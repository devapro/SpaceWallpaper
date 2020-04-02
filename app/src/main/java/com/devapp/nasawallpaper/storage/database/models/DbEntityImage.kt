package com.devapp.nasawallpaper.storage.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "wallpaper")
class DbEntityImage (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "showCount")
    val showCount: Int?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "collection")
    val collection: String?,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "type")
    val type: String?,
    @ColumnInfo(name = "localPath")
    val localPath: String?,
    @ColumnInfo(name = "urlHd")
    val urlHd: String?,
    @ColumnInfo(name = "fireId")
    val fireId: String?,
    @ColumnInfo(name = "createdAtServer")
    val createdAtServer: Long?,
    @ColumnInfo(name = "createdAt")
    val createdAt: Long,
    @ColumnInfo(name = "premium", defaultValue = "0")
    val premium: Boolean,
    @ColumnInfo(name = "isDeleted", defaultValue = "0")
    val isDeleted: Boolean,
    @ColumnInfo(name = "rate", defaultValue = "0")
    val rate: Int

) : Serializable