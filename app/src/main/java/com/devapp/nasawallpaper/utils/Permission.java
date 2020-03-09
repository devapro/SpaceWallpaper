package com.devapp.nasawallpaper.utils;

import android.Manifest;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@StringDef({
        Permission.CONTACTS_READ,
        Permission.CONTACTS_WRITE,
        Permission.STORAGE_READ,
        Permission.STORAGE_WRITE,
        Permission.CAMERA,
        Permission.AUDIO_RECORD,
        Permission.LOCATION_FINE,
        Permission.LOCATION_COARSE,
        Permission.PHONE_STATE,
        Permission.FOREGROUND_SERVICE
})

public @interface Permission {
    String CONTACTS_READ = Manifest.permission.READ_CONTACTS;
    String CONTACTS_WRITE = Manifest.permission.WRITE_CONTACTS;
    String STORAGE_READ = Manifest.permission.READ_EXTERNAL_STORAGE;
    String STORAGE_WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    String CAMERA = Manifest.permission.CAMERA;
    String AUDIO_RECORD = Manifest.permission.RECORD_AUDIO;
    String LOCATION_FINE = Manifest.permission.ACCESS_FINE_LOCATION;
    String LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;
    String PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    String FOREGROUND_SERVICE = Manifest.permission.FOREGROUND_SERVICE;
}
