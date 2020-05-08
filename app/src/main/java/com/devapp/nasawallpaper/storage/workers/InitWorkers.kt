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

    val downloadImageRequest = OneTimeWorkRequest
        .Builder(DownloadImageWorker::class.java)
        .setConstraints(constraintsForInitialLoader)
        .build()
    WorkManager.getInstance(app).enqueue(downloadImageRequest)

    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(app.sPreferences.getRestrictBattery())
        .setRequiresDeviceIdle(app.sPreferences.getRestrictIdle())
        .setRequiresStorageNotLow(true)
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val todayImageWorkRequest = PeriodicWorkRequest
        .Builder(LoadTodayImageWorker::class.java, 12L, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()
    WorkManager.getInstance(app).enqueue(todayImageWorkRequest)
}