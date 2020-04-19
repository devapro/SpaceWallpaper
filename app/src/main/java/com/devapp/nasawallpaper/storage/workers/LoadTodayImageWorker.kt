package com.devapp.nasawallpaper.storage.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.devapp.nasawallpaper.App

class LoadTodayImageWorker(appContext: Context, workerParameters: WorkerParameters): CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        Log.d("WorkManager", "LoadTodayImage")
        val success = App.getAppComponent().appController.loadToday()
        App.getAppComponent().appController.loadPrevious()
        return if(success) Result.success() else Result.retry()
    }
}