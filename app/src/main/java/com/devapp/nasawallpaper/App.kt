package com.devapp.nasawallpaper

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import com.devapp.nasawallpaper.storage.database.AppDataBase
import com.devapp.nasawallpaper.storage.files.AppStorage
import com.devapp.nasawallpaper.logic.controllers.AppController
import com.devapp.nasawallpaper.logic.controllers.DataController
import com.devapp.nasawallpaper.logic.controllers.DownloadImageController
import com.devapp.nasawallpaper.storage.database.DataRepository
import com.devapp.nasawallpaper.storage.database.RoomDataRepository
import com.devapp.nasawallpaper.storage.preferences.SPreferences
import com.devapp.nasawallpaper.storage.serverapi.RestServerApi
import com.devapp.nasawallpaper.utils.UtilSensors

class App : MultiDexApplication(){
    lateinit var dataBase: AppDataBase
    lateinit var dataRepository: DataRepository
    lateinit var appStorage: AppStorage
    lateinit var appController: AppController
    lateinit var downloadController: DownloadImageController
    lateinit var sPreferences: SPreferences

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
        appStorage =
            AppStorage(applicationContext)
        UtilSensors.init(applicationContext)
        dataRepository = RoomDataRepository(dataBase)

        val serverApi = RestServerApi()
        val dataController = DataController(dataRepository, serverApi)
        downloadController = DownloadImageController(applicationContext, dataRepository, appStorage)

        appController = AppController(dataController, dataRepository, downloadController)

        sPreferences = SPreferences(applicationContext)

        sAppComponent = this
    }
}