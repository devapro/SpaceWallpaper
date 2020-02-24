package com.devapp.nasawallpaper.logic.controllers

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AppController (private val dataController: DataController){
    fun checkUpdates(){
        GlobalScope.launch{
            dataController.load()
        }
    }
}