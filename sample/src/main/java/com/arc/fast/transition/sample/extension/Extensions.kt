package com.arc.fast.transition.sample.extension

import android.util.Log
import com.arc.fast.transition.sample.BuildConfig
import com.arc.fast.transition.sample.SampleApp


fun LOG(log: String) {
    if (SampleApp.isDebug) Log.e(BuildConfig.APPLICATION_ID, log)
}

