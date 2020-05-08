package com.devapp.nasawallpaper.storage.preferences

import android.content.Context
import android.content.SharedPreferences

const val PREFERENCES_NAME = "WDPP"

const val PREF_ANIMATION = "animation"
const val PREF_RESTRICT_BATTERY = "batteryRestrict"
const val PREF_RESTRICT_IDLE = "idleRestrict"

class SPreferences(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, 0)

    fun setAnimation(value: Boolean) = setValue(PREF_ANIMATION, value)
    fun setRestrictBattery(value: Boolean) = setValue(PREF_RESTRICT_BATTERY, value)
    fun setRestrictIdle(value: Boolean) = setValue(PREF_RESTRICT_IDLE, value)

    fun getAnimation(default: Boolean = false) = getBoolean(PREF_ANIMATION, default)
    fun getRestrictIdle(default: Boolean = false) = getBoolean(PREF_RESTRICT_IDLE, default)
    fun getRestrictBattery(default: Boolean = false) = getBoolean(PREF_RESTRICT_BATTERY, default)

    private fun setValue(key: String, value: Boolean){
        pref.edit().apply {
            putBoolean(key, value).apply()
        }
    }

    private fun getBoolean(key: String, default: Boolean = false): Boolean{
        return pref.getBoolean(key, default)
    }
}