package com.devapp.nasawallpaper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

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
