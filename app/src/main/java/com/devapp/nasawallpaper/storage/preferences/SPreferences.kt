package com.devapp.nasawallpaper.storage.preferences

import android.content.Context
import android.content.SharedPreferences

const val PREFERENCES_NAME = "WDPP"

const val PREF_ANIMATION = "animation"
const val PREF_RESTRICT_BATTERY = "batteryRestrict"
const val PREF_RESTRICT_IDLE = "idleRestrict"

class SPreferences(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, 0)

    fun setValue(value: Boolean, key: String){
        pref.edit().apply {
            putBoolean(key, value).apply()
        }
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean{
        return pref.getBoolean(key, default)
    }
}