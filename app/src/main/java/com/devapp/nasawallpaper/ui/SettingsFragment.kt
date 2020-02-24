package com.devapp.nasawallpaper.ui

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
import kotlinx.android.synthetic.main.settings_fragment.*

class SettingsFragment : NavigationFragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
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
                object : UtilPermission.PermissionCallback(Array(1){Permission.STORAGE_WRITE}){
                    override fun onSuccessGrantedAll() {
                        init()
                    }
                })
        if(hasPermission){
            init()
        }

        openSettings?.setOnClickListener { startService() }

        openInfo?.setOnClickListener { openInfo() }

        val pref = activity?.getSharedPreferences("WDPP", 0)
        animationSwitch?.isChecked = pref?.getBoolean("animation", true) ?: true
        animationSwitch?.setOnCheckedChangeListener { buttonView, isChecked -> pref?.edit()?.putBoolean("animation", isChecked)?.apply() }
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
