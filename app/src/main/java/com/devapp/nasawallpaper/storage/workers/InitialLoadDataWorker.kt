package com.devapp.nasawallpaper.storage.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.devapp.nasawallpaper.App

class InitialLoadDataWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        Log.d("WorkManager", "InitialLoadDataWorkers")
        val success = App.getAppComponent().appController.loadInitial()
        return if(success) Result.success() else Result.retry()
    }
}