package com.devapp.nasawallpaper.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.devapp.nasawallpaper.R

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.about_app_title)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
