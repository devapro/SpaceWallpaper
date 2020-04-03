package com.devapp.nasawallpaper.storage.serverapi.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.devapp.nasawallpaper.App

class LoadTodayImage(appContext: Context, workerParameters: WorkerParameters): CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        val success = App.getAppComponent().appController.loadToday()
        App.getAppComponent().appController.loadPrevious()
        return if(success) Result.success() else Result.retry()
    }
}