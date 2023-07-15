package com.arc.fast.transition

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import me.weishu.reflection.Reflection

/**
 * 共享元素动画工具
 * 使用方法：
 * 1、请在onStop方法中调用FastTransitionUtils.onStop；
 * 2、请在Application.attachBaseContext中调用FastTransitionUtils.unsealMultipleActivityTransition；请在finishAfterTransition中调用FastTransitionUtils.finishAfterTransition
 * 工具简介：
 * 1、修复Q及以上系统，activity调用onStop后共享元素动画丢失的BUG
 * 2、修复Q及以上系统，3个及以上连续的activity拥有共享元素动画时，共享元素动画丢失的BUG（使用反射）
 * 注意使用反射必须与implementation 'com.github.tiann:FreeReflection:3.1.0'结合使用
 */
@SuppressLint("PrivateApi")
object FastTransitionUtils {
    private val CLAZZ: Class<*>? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) try {
            Class.forName("android.app.ActivityTransitionState")
        } catch (e: ClassNotFoundException) {
            Log.e(
                "SharedElementUtils",
                "load class android.app.ActivityTransitionState failed!",
                e
            )
            null
        } else null
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun setPendingExitSharedElements(activity: Activity, elements: ArrayList<String?>?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q || CLAZZ == null) return
        try {
            val mActivityTransitionStateField =
                Activity::class.java.getDeclaredField("mActivityTransitionState")
            mActivityTransitionStateField.isAccessible = true
            val mActivityTransitionStateObject = mActivityTransitionStateField[activity]
            val mPendingExitNamesField = CLAZZ!!.getDeclaredField("mPendingExitNames")
            mPendingExitNamesField.isAccessible = true
            mPendingExitNamesField[mActivityTransitionStateObject] = elements
        } catch (thr: Throwable) {
            Log.e("SharedElementUtils", "reflective set pending exit shared elements failed!", thr)
        }
    }

    /**
     * 修复Q及以上系统，3个及以上连续的activity拥有共享元素动画时，共享元素动画丢失的BUG（使用反射）
     * 请在Application.attachBaseContext中调用该方法(super之后)
     */
    fun enableMultipleActivityTransition(context: Context) {
        Reflection.unseal(context)
    }

    /**
     * 修复Q及以上系统，3个及以上连续的activity拥有共享元素动画时，共享元素动画丢失的BUG（使用反射）
     * 请在Actiivty.finishAfterTransition中调用该方法(super之前)
     */
    fun finishAfterTransition(activity: Activity, config: List<FastTransitionConfig>?) =
        finishAfterTransition(
            activity,
            config?.mapNotNull {
                if (it.transitionView == null) null else it.transitionName
            } as? ArrayList<String?>
        )


    /**
     * 修复Q及以上系统，3个及以上连续的activity拥有共享元素动画时，共享元素动画丢失的BUG（使用反射）
     * 请在Actiivty.finishAfterTransition中调用该方法(super之前)
     */
    fun finishAfterTransition(activity: Activity, elements: ArrayList<String?>?) =
        setPendingExitSharedElements(activity, elements)

    /**
     * 修复Q及以上系统，activity调用onStop后共享元素动画丢失的BUG
     * 请在Actiivty.onStop中调用该方法(super之前)
     */
    fun onStop(activity: Activity) {
        if (!activity.isFinishing && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Instrumentation().callActivityOnSaveInstanceState(activity, Bundle())
        }
    }

}