package com.devapp.nasawallpaper.storage.serverapi.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.devapp.nasawallpaper.App

class InitialLoadDataWorkers(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val success = App.getAppComponent().appController.loadInitial()
        return if(success) Result.success() else Result.retry()
    }
}