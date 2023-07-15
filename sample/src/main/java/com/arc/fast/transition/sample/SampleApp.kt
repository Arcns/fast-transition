package com.arc.fast.transition.sample

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import com.arc.fast.immersive.setAutoInitSystemBarHeight
import com.arc.fast.transition.FastTransitionUtils

class SampleApp : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        FastTransitionUtils.enableMultipleActivityTransition(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        setAutoInitSystemBarHeight()
//        DynamicColors.applyToActivitiesIfAvailable(this) { _, _ ->
//            LocalData.enableDynamicColors
//        }
    }

    companion object {
        lateinit var instance: SampleApp

        @JvmStatic
        val isDebug: Boolean
            get() = BuildConfig.DEBUG

        @JvmStatic
        val packageInfo: PackageInfo
            get() = instance.packageManager.getPackageInfo(instance.packageName, 0)
    }
}