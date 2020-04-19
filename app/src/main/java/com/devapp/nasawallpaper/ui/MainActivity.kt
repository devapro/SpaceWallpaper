package com.devapp.nasawallpaper.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.devapp.nasawallpaper.App
import com.devapp.nasawallpaper.R
import com.devapp.nasawallpaper.storage.workers.scheduleWorkers
import com.devapp.nasawallpaper.utils.Permission
import com.devapp.nasawallpaper.utils.UtilPermission

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
                        scheduleWorkers(application as App)
                    }
                })
        if(hasPermission){
            scheduleWorkers(application as App)
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
}