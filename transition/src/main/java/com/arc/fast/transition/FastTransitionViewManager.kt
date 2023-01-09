package com.arc.fast.transition

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import com.arc.fast.transition.item.FastBaseItem

/**
 * 共享元素动画视图管理器
 */
open class FastTransitionViewManager {

    // 视图列表
    val views: HashMap<String, SETransitionView> by lazy {
        HashMap()
    }

    // 设置需要共享元素的视图
    open fun addView(key: String, view: View?, config: FastTransitionConfig? = null) {
        if (view == null) views.remove(key)
        else views[key] = SETransitionView(key, view, config)
    }

    // 设置需要共享元素的视图
    open fun addView(key: String, view: View?, vararg items: FastBaseItem) {
        addView(
            key = key,
            view = view,
            config = FastTransitionConfig(
                key,
                null,
                *items,
            )
        )
    }

    // 返回共享元素视图，并约束指定类型
    inline fun <reified T> getView(key: String): T? = views[key]?.view as? T

    // 设置共享元素的动画配置列表
    open fun setConfig(
        key: String, vararg items: FastBaseItem
    ) {
        val view = views[key]
        if (view != null) {
            view.config = FastTransitionConfig(key = key, items = *items)
        }
    }

    /**
     * 生成Intent动画配置
     */
    open fun generateIntentOptions(
        activity: Activity, intent: Intent, id: String?
    ): Bundle? {
        val key = System.currentTimeMillis().toString()
        val sharedElements = ArrayList<Pair<View, String>>()
        val configs = views.values.mapNotNull {
            if (it.view?.isVisible != true) return@mapNotNull null
            val transitionName = it.key + "_${key}_$id"
            sharedElements.add(Pair(it.view, transitionName))
            it.view?.transitionName = transitionName
            if (it.config != null) {
                it.config?.transitionName = transitionName
            } else {
                it.config = FastTransitionConfig(it.key, transitionName)
            }
            it.config
        } as ArrayList
        intent.putParcelableArrayListExtra(
            DEFAULT_FAST_TRANSITION_CONFIGS_INTENT_NAME,
            configs
        )
        return ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity, *sharedElements.toTypedArray()
        ).toBundle()
    }

    open fun <T : Activity> startActivity(
        activity: Activity,
        targetActivityCLass: Class<T>,
        targetDataID: String?= null,
        applyIntent: ((intent: Intent) -> Unit)? = null
    ) {
        startActivity(
            activity = activity,
            targetIntent = Intent(activity, targetActivityCLass),
            targetDataID = targetDataID
        )
    }

    open fun startActivity(
        activity: Activity,
        targetIntent: Intent,
        targetDataID: String?
    ) {
        activity.startActivity(
            targetIntent,
            generateIntentOptions(activity, targetIntent, targetDataID)
        )
    }

    /**
     * 共享元素动画视图
     */
    data class SETransitionView(
        val key: String, var view: View? = null, var config: FastTransitionConfig? = null
    )
}