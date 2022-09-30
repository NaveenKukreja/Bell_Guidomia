package com.bell

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/*
* Base class for generating Hilt application
*/
@HiltAndroidApp
class HiltApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}