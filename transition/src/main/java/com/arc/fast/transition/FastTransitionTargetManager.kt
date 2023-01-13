package com.arc.fast.transition

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.arc.fast.transition.item.FastTransitionItem

class FastTransitionTargetManager(
    val activity: Activity,
    val configs: List<FastTransitionConfig>,
) {
    fun getTransitionConfig(key: String): FastTransitionConfig? =
        configs.firstOrNull { it.key == key }

    fun setTransitionView(key: String, view: View) {
        getTransitionConfig(key)?.transitionView = view
    }

    /**
     * 应用进入与离开动画配置
     */
    fun applyTransitionEnterAndReturnConfig(
        duration: Long = 150,//150
        postponeEnterTransition: Boolean = false,
        postponeEnterTransitionTimeout: Long = 500,
        pageCurrentScale: (() -> Float)? = null,
        onTransitionEnd: (() -> Unit)? = null
    ) {
        // 先暂停动画，等待准备好后再通知开始动画
        if (postponeEnterTransition) activity.postponeEnterTransition()
        // 初始化操作
        configs.forEach { transitionConfig ->
            transitionConfig.items.forEach {
                if (it is FastTransitionItem) it.onEnterBefore(
                    activity,
                    transitionConfig
                )
            }
        }
        // 设置动画
        activity.window.enterTransition = Fade()
        activity.window.returnTransition = Fade()
        activity.window.sharedElementEnterTransition =
            TransitionSet().apply {
                interpolator = AccelerateInterpolator()
                setDuration(duration)
            }.addTransition(ChangeBounds()).apply {
                configs.forEach {
                    addTransition(
                        it.getTransition(true, pageCurrentScale)?.setDuration(duration - 50)
                            ?: return@forEach
                    )
                }
            }
        activity.window.sharedElementReturnTransition =
            TransitionSet().apply {
                interpolator = AccelerateInterpolator()
                setDuration(duration)
            }.addTransition(ChangeBounds()).apply {
                configs.forEach {
                    addTransition(
                        it.getTransition(false, pageCurrentScale)?.setDuration(duration - 50)
                            ?: return@forEach
                    )
                }
            }
        // 动画暂停超时处理
        var postponeEnterTransitionTimeoutHandler: Handler? = null
        if (postponeEnterTransition && postponeEnterTransitionTimeout > 0) {
            postponeEnterTransitionTimeoutHandler = Handler(Looper.getMainLooper())
            postponeEnterTransitionTimeoutHandler.postDelayed({
                startTransitionEnter()
            }, postponeEnterTransitionTimeout)
        }
        // 监听动画结束
        activity.window.sharedElementEnterTransition.addListener(object :
            Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                postponeEnterTransitionTimeoutHandler?.removeCallbacksAndMessages(null)
                configs.forEach {
                    it.hasExecutedTransitionEnter = true
                }
            }

            override fun onTransitionEnd(transition: Transition) {
                activity.window.sharedElementEnterTransition.removeListener(this)
                onTransitionEnd?.invoke()
            }

            override fun onTransitionCancel(transition: Transition) {
            }

            override fun onTransitionPause(transition: Transition) {
            }

            override fun onTransitionResume(transition: Transition) {
            }

        })
    }

    /**
     * 开始进入动画
     */
    fun startTransitionEnter() {
        if (configs.firstOrNull()?.hasExecutedTransitionEnter == true) return
        activity.startPostponedEnterTransition()
    }

    /**
     * 可选配置：请在Activity.onStop中调用该方法(super之前)
     * 修复Q及以上系统，activity调用onStop后共享元素动画丢失的BUG
     */
    fun onStop() = FastTransitionUtils.onStop(activity)

    /**
     * 可选配置：请在Activity.finishAfterTransition中调用该方法(super之前)
     * 注意使用该方法时，你还需要在Application.attachBaseContext中调用FastTransitionUtils.unsealMultipleActivityTransition
     * 修复Q及以上系统，3个及以上连续的activity拥有共享元素动画时，共享元素动画丢失的BUG（使用反射）
     */
    fun finishAfterTransition(
        pageCurrentScale: Float? = null
    ) {
        if (pageCurrentScale != null && pageCurrentScale != 1f) {
            configs.forEach {
                val transitionView = it.transitionView ?: return@forEach
                it.items.forEach {
                    if (it is FastTransitionItem) it.onReturnBefore(
                        transitionView,
                        pageCurrentScale
                    )
                }
            }
        }
        FastTransitionUtils.finishAfterTransition(activity, configs)
    }


    companion object {

        /**
         * 获取Intent中保存的转场目的地管理器
         */
        fun getManager(activity: Activity): FastTransitionTargetManager? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                activity.intent.getParcelableArrayListExtra(
                    DEFAULT_FAST_TRANSITION_CONFIGS_INTENT_NAME,
                    FastTransitionConfig::class.java
                )
            } else {
                activity.intent.getParcelableArrayListExtra(
                    DEFAULT_FAST_TRANSITION_CONFIGS_INTENT_NAME
                )
            }?.let { FastTransitionTargetManager(activity, it) }

    }
}