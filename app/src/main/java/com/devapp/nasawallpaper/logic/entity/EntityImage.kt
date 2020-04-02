package com.devapp.nasawallpaper.logic.entity

data class EntityImage(
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
    val isDeleted: Boolean,
    val rate: Int
) {

    enum class Changed {
        LOCAL_PATH_UPDATE,
        RATE_CHANGE,
        NONE
    }

    fun getChanged(item: EntityImage): Changed{
        if(!localPath.equals(item.localPath)){
            return Changed.LOCAL_PATH_UPDATE
        }
        if(rate != item.rate){
            return Changed.RATE_CHANGE
        }
        return Changed.NONE
    }
}