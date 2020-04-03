package com.devapp.nasawallpaper.storage.serverapi

import com.devapp.nasawallpaper.storage.serverapi.entity.ImageServerApiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RestServerApi : ServerApi{

    val okHttpClient: OkHttpClient = OkHttpClient()

    override suspend fun loadNewPart(lastUpdate: Long?): List<ImageServerApiModel> {
        return withContext(Dispatchers.IO){
            val date = Date()
            lastUpdate?.let { date.time = it }
            date.date = date.date - 1
            val formatter = SimpleDateFormat("YYYY-MM-dd")
            val url = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date=${formatter.format(date)}&hd=true"
            val request: Request = Request
                .Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .build()
            val response = okHttpClient.newCall(request).execute()
            if(response.isSuccessful){
                val json = response.body?.string()
                val jsonObject = JSONObject(json)
                val item = ImageServerApiModel(
                    jsonObject.getString("date"),
                    jsonObject.getString("url"),
                    jsonObject.getString("hdurl"),
                    jsonObject.getString("title"),
                    jsonObject.getString("explanation"),
                    jsonObject.getString("media_type"),
                    false,
                    jsonObject.getString("date"),
                    Date().time,
                    "apod"
                )
                return@withContext listOf(item)
            }
            return@withContext Collections.emptyList<ImageServerApiModel>()
        }
    }
}