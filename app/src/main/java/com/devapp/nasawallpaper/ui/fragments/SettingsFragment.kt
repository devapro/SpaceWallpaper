package com.devapp.nasawallpaper.ui.fragments

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devapp.nasawallpaper.*
import com.devapp.nasawallpaper.logic.viewmodels.SettingsViewModel
import com.devapp.nasawallpaper.storage.preferences.PREF_ANIMATION
import com.devapp.nasawallpaper.storage.preferences.PREF_RESTRICT_BATTERY
import com.devapp.nasawallpaper.storage.preferences.PREF_RESTRICT_IDLE
import com.devapp.nasawallpaper.ui.InfoActivity
import com.devapp.nasawallpaper.ui.MainActivity
import com.devapp.nasawallpaper.utils.Permission
import com.devapp.nasawallpaper.utils.UtilPermission
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : NavigationFragment() {

    companion object {
        fun newInstance() =
            SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        setTitle(getString(R.string.settings))
        displayHome()

        val hasPermission = (activity as MainActivity)
            .permissionUtils
            .checkAndRequestPermissions(
                this,
                object : UtilPermission.PermissionCallback(Array(1){ Permission.STORAGE_WRITE}){
                    override fun onSuccessGrantedAll() {
                        init()
                    }
                })
        if(hasPermission){
            init()
        }

        openSettings?.setOnClickListener { startService() }

        openInfo?.setOnClickListener { openInfo() }

        switchAnimation?.isChecked = (activity?.application as App)
            .sPreferences.getBoolean(PREF_ANIMATION, true)
        switchAnimation?.setOnCheckedChangeListener { _, isChecked -> (activity?.application as App).sPreferences.setValue(isChecked, PREF_ANIMATION) }

        switchBatteryRestrict?.isChecked = (activity?.application as App)
            .sPreferences.getBoolean(PREF_RESTRICT_BATTERY, true)
        switchBatteryRestrict?.setOnCheckedChangeListener { _, isChecked -> (activity?.application as App).sPreferences.setValue(isChecked, PREF_RESTRICT_BATTERY) }

        switchIdleRestrict?.isChecked =(activity?.application as App)
            .sPreferences.getBoolean(PREF_RESTRICT_IDLE, true)
        switchIdleRestrict?.setOnCheckedChangeListener { _, isChecked -> (activity?.application as App).sPreferences.setValue(isChecked, PREF_RESTRICT_IDLE) }
    }

    private fun init(){
        info?.visibility = View.GONE
        info2?.visibility = View.VISIBLE
        openSettings?.visibility = View.VISIBLE
    }

    private fun openInfo(){
        startActivity(Intent(activity, InfoActivity::class.java))
    }

    private fun startService(){
        val intent = Intent(
            WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
        )
        intent.putExtra(
            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            ComponentName(activity, MyWallpaperService::class.java)
        )

        startActivity(intent)
    }
}
