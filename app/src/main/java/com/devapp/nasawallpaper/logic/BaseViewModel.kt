package com.devapp.nasawallpaper.logic

import android.app.Application
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

open class BaseViewModel (application: Application) : AndroidViewModel(application){
    fun getStringRes(@StringRes resId: Int): String{
        return getApplication<Application>().resources.getString(resId)
    }
}