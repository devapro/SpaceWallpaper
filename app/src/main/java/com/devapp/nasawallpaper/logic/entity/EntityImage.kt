package com.devapp.nasawallpaper.logic.entity

class EntityImage(
    val id: Int,
    val name: String?,
    val showCount: Int?,
    val description: String?,
    val collection: String?,
    val url: String,
    val type: String?,
    val localPath: String?,
    val urlHd: String?,
    val createdAt: Long,
    val isDeleted: Boolean
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EntityImage

        if (id != other.id) return false
        if (name != other.name) return false
        if (showCount != other.showCount) return false
        if (description != other.description) return false
        if (collection != other.collection) return false
        if (url != other.url) return false
        if (type != other.type) return false
        if (localPath != other.localPath) return false
        if (urlHd != other.urlHd) return false
        if (createdAt != other.createdAt) return false
        if (isDeleted != other.isDeleted) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (showCount ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (collection?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (localPath?.hashCode() ?: 0)
        result = 31 * result + (urlHd?.hashCode() ?: 0)
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + isDeleted.hashCode()
        return result
    }
}