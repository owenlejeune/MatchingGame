package com.owenlejeune.matchinggame

import android.app.Application
import com.owenlejeune.matchinggame.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MatchingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MatchingApplication)
            modules(listOf(appModule))
        }
    }

}