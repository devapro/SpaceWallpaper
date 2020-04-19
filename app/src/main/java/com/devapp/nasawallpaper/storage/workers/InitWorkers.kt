package com.devapp.nasawallpaper.storage.workers

import androidx.work.*
import com.devapp.nasawallpaper.App
import com.devapp.nasawallpaper.storage.preferences.PREF_RESTRICT_BATTERY
import com.devapp.nasawallpaper.storage.preferences.PREF_RESTRICT_IDLE
import java.util.concurrent.TimeUnit


fun scheduleWorkers(app: App){
    val constraintsForInitialLoader = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val initialWorkRequest = OneTimeWorkRequest
        .Builder(InitialLoadDataWorker::class.java)
        .setConstraints(constraintsForInitialLoader)
        .build()
    WorkManager.getInstance(app).enqueue(initialWorkRequest)

    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(app.sPreferences.getBoolean(
            PREF_RESTRICT_BATTERY
        ))
        .setRequiresDeviceIdle(app.sPreferences.getBoolean(PREF_RESTRICT_IDLE))
        .setRequiresStorageNotLow(true)
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val todayImageWorkRequest = PeriodicWorkRequest
        .Builder(LoadTodayImageWorker::class.java, 12L, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()
    WorkManager.getInstance(app).enqueue(todayImageWorkRequest)
}