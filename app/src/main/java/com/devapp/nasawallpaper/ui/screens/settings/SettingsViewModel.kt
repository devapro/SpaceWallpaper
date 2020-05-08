package com.devapp.nasawallpaper.ui.screens.settings

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devapp.nasawallpaper.logic.BaseViewModel
import com.devapp.nasawallpaper.storage.preferences.SPreferences

class SettingsViewModel(
    application: Application,
    private val sPreferences: SPreferences
) : BaseViewModel(application) {

    val settingsAnimation = MutableLiveData<Boolean>()
    val settingsRestrictBattery = MutableLiveData<Boolean>()
    val settingsRestrictIdle = MutableLiveData<Boolean>()

    init {
        settingsAnimation.postValue(sPreferences.getAnimation(true))
        settingsRestrictBattery.postValue(sPreferences.getRestrictBattery(true))
        settingsRestrictIdle.postValue(sPreferences.getRestrictIdle(true))
    }

    fun setAnimation(value: Boolean){
        sPreferences.setAnimation(value)
        settingsAnimation.value = value
    }

    fun setRestrictBattery(value: Boolean){
        sPreferences.setRestrictBattery(value)
        settingsRestrictBattery.value = value
    }

    fun setRestrictIdle(value: Boolean){
        sPreferences.setRestrictIdle(value)
        settingsRestrictIdle.value = value
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val application: Application, private val sPreferences: SPreferences): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel =
                SettingsViewModel(
                    application,
                    sPreferences
                )
            return viewModel as T
        }
    }
}
