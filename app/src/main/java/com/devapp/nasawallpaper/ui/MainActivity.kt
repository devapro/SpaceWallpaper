package com.devapp.nasawallpaper.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.devapp.nasawallpaper.R
import com.devapp.nasawallpaper.storage.serverapi.workers.InitialLoadDataWorkers
import com.devapp.nasawallpaper.storage.serverapi.workers.LoadTodayImage
import com.devapp.nasawallpaper.utils.Permission
import com.devapp.nasawallpaper.utils.UtilPermission
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val permissionUtils = UtilPermission()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hasPermission = permissionUtils
            .checkAndRequestPermissions(
                this,
                object : UtilPermission.PermissionCallback(Array(1){ Permission.STORAGE_WRITE}){
                    override fun onSuccessGrantedAll() {
                        scheduleWorkers()
                    }
                })
        if(hasPermission){
            scheduleWorkers()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @Permission permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun scheduleWorkers(){
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresDeviceIdle(true)
            .setRequiresStorageNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val initialWorkRequest = OneTimeWorkRequest
            .Builder(InitialLoadDataWorkers::class.java)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance().enqueue(initialWorkRequest)

        val todayImageWorkRequest = PeriodicWorkRequest
            .Builder(LoadTodayImage::class.java, 12L, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance().enqueue(todayImageWorkRequest)
    }
}