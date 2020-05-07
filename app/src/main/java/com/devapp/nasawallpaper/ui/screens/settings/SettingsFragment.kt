package com.devapp.nasawallpaper.ui.screens.settings

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.devapp.nasawallpaper.*
import com.devapp.nasawallpaper.ui.InfoActivity
import com.devapp.nasawallpaper.ui.MainActivity
import com.devapp.nasawallpaper.ui.NavigationFragment
import com.devapp.nasawallpaper.utils.Permission
import com.devapp.nasawallpaper.utils.UtilPermission
import com.devapp.nasawallpaper.utils.extensions.observeNonNull
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : NavigationFragment() {

    private val viewModel by viewModels<SettingsViewModel>(){
        val app = (requireActivity().application as App)
        SettingsViewModel.ViewModelFactory(app, app.sPreferences)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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

        viewModel.settingsAnimation.observeNonNull(viewLifecycleOwner){ switchAnimation.isChecked = it }
        viewModel.settingsBatteryRestrict.observeNonNull(viewLifecycleOwner){ switchBatteryRestrict.isChecked = it }
        viewModel.settingsIdleRestrict.observeNonNull(viewLifecycleOwner){ switchIdleRestrict.isChecked = it }

        switchAnimation?.setOnCheckedChangeListener { _, isChecked -> viewModel.setAnimation(isChecked) }
        switchBatteryRestrict?.setOnCheckedChangeListener { _, isChecked -> viewModel.setBatteryRestrict(isChecked) }
        switchIdleRestrict?.setOnCheckedChangeListener { _, isChecked -> viewModel.setIdleRestrict(isChecked) }

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
