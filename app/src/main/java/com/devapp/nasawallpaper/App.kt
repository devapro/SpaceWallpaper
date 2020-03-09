package com.devapp.nasawallpaper

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import com.devapp.nasawallpaper.storage.database.AppDataBase
import com.devapp.nasawallpaper.logic.AppStorage
import com.devapp.nasawallpaper.logic.controllers.AppController
import com.devapp.nasawallpaper.logic.controllers.DataController
import com.devapp.nasawallpaper.logic.controllers.DownloadImageController
import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.storage.database.RoomDataRepository
import com.devapp.nasawallpaper.storage.serverapi.FireBaseServerApi
import com.devapp.nasawallpaper.utils.UtilSensors
import com.google.firebase.firestore.FirebaseFirestore

class App : MultiDexApplication(){
    lateinit var dataBase: AppDataBase
    lateinit var dataRepository: DataRepository
    lateinit var appStorage: AppStorage
    lateinit var appController: AppController
    lateinit var downloadController: DownloadImageController

    companion object{
        private var sAppComponent : App? = null

        @JvmStatic
        fun getAppComponent() : App {
            return sAppComponent!!
        }
    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        dataBase = Room.databaseBuilder(applicationContext,
            AppDataBase::class.java, "mWDevAppW")
            .build()
        appStorage = AppStorage(applicationContext)
        UtilSensors.init(applicationContext)
        dataRepository = RoomDataRepository(dataBase)
        val serverApi = FireBaseServerApi(FirebaseFirestore.getInstance())
        val dataController = DataController(dataRepository, serverApi)
        downloadController = DownloadImageController(applicationContext, dataRepository, appStorage)

        appController = AppController(dataController)

        sAppComponent = this
    }
}