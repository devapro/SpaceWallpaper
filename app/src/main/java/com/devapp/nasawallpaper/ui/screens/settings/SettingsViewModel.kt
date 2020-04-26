package com.devapp.nasawallpaper.ui.screens.settings

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devapp.nasawallpaper.logic.BaseViewModel
import com.devapp.nasawallpaper.storage.preferences.PREF_ANIMATION
import com.devapp.nasawallpaper.storage.preferences.PREF_RESTRICT_BATTERY
import com.devapp.nasawallpaper.storage.preferences.PREF_RESTRICT_IDLE
import com.devapp.nasawallpaper.storage.preferences.SPreferences

class SettingsViewModel(
    application: Application,
    private val sPreferences: SPreferences
) : BaseViewModel(application) {

    val settingsAnimation = MutableLiveData<Boolean>()
    val settingsBatteryRestrict = MutableLiveData<Boolean>()
    val settingsIdleRestrict = MutableLiveData<Boolean>()

    init {
        settingsAnimation.postValue(sPreferences.getBoolean(PREF_ANIMATION, true))
        settingsBatteryRestrict.postValue(sPreferences.getBoolean(PREF_RESTRICT_BATTERY, true))
        settingsIdleRestrict.postValue(sPreferences.getBoolean(PREF_RESTRICT_IDLE, true))
    }

    fun setAnimation(value: Boolean){
        sPreferences.getBoolean(PREF_ANIMATION, value)
        settingsAnimation.value = value
    }

    fun setBatteryRestrict(value: Boolean){
        sPreferences.getBoolean(PREF_RESTRICT_BATTERY, value)
        settingsBatteryRestrict.value = value
    }

    fun setIdleRestrict(value: Boolean){
        sPreferences.getBoolean(PREF_RESTRICT_IDLE, value)
        settingsIdleRestrict.value = value
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
