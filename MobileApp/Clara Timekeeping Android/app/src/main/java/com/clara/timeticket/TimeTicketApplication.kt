package com.clara.timeticket

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TimeTicketApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}