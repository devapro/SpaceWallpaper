package com.devapp.nasawallpaper.storage.serverapi

import android.util.Log
import com.devapp.nasawallpaper.storage.serverapi.entity.ImageServerApiModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CompletableDeferred
import kotlin.collections.ArrayList

class FireBaseServerApi(private val db: FirebaseFirestore) : ServerApi{

    private val TAG = FireBaseServerApi::class.java.simpleName.toString()

    override suspend fun loadNewPart(lastUpdate: Long?): List<ImageServerApiModel> {
        val result = CompletableDeferred<List<ImageServerApiModel>>()
        read((lastUpdate ?: 0), (if(lastUpdate != null) 50 else 100), object : ResultCallback{
            override fun onSuccess(items: List<ImageServerApiModel>) {
                result.complete(items)
            }

            override fun onError(exception: Exception) {
                result.completeExceptionally(exception)
            }
        })
        return result.await()
    }

    private fun read(sines: Long, limit: Long, callback: ResultCallback){
        val t = Timestamp(sines, 0)
        db.collection("wallpapers")
            .whereEqualTo("mediaType", "image")
            .limit(limit)
            .orderBy("createAt", Query.Direction.ASCENDING)
            .startAt(t)
            .get()
            .addOnSuccessListener { result ->
                val list = ArrayList<ImageServerApiModel>()
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data} - ${document.getTimestamp("createTime")?.seconds}")
                    list.add(
                        ImageServerApiModel(
                            document.id,
                            document.getString("url") ?: "",
                            document.getString("hdUrl"),
                            document.getString("name"),
                            document.getString("description"),
                            document.getString("mediaType") ?: "",
                            document.getBoolean("type") ?: false,
                            document.getString("date") ?: "",
                            document.getTimestamp("createAt")?.seconds ?: 0,
                            document.getString("collection") ?: ""
                        )
                    )
                }
                callback.onSuccess(list)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                callback.onError(exception)
            }
    }

    private interface ResultCallback{
        fun onSuccess(items: List<ImageServerApiModel>)
        fun onError(exception: Exception)
    }
}