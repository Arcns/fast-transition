package com.arc.fast.transition

import android.animation.Animator
import android.animation.ObjectAnimator
import android.transition.Transition
import android.transition.TransitionValues
import android.util.Property
import android.view.View
import android.view.ViewGroup
import com.arc.fast.transition.item.FastBaseCalculator

/**
 * 共享元素动画
 */
internal class FastTransition(
    // 动画配置
    val config: FastTransitionConfig?,
    // 标识为进入动画或离开动画
    val isEnter: Boolean,
    // 当前页面的缩放比例，如果您所配置的动画中需要使用页面缩放比列，则您需要传入该方法
    val pageCurrentScale: (() -> Float)? = null
) :
    Transition() {

    override fun captureStartValues(transitionValues: TransitionValues?) {
    }

    override fun captureEndValues(transitionValues: TransitionValues?) {
    }

    override fun createAnimator(
        sceneRoot: ViewGroup?,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        return if (startValues?.view == null || endValues == null || config == null) null
        else createAnimator(startValues.view, config)
    }

    private fun createAnimator(
        view: View,
        transition: FastTransitionConfig
    ): Animator {
        // 创建自定义动画
        return ObjectAnimator.ofFloat(
            view,
            object :
                Property<View, Float>(Float::class.java, FastTransition::class.simpleName) {
                private var calculators: List<FastBaseCalculator<*, *>>? = null
                override fun get(view: View): Float = 0f
                override fun set(view: View, progress: Float) {
                    if (calculators == null) {
                        // 获取计算器列表
                        calculators =
                            transition.getCalculators(isEnter, view, pageCurrentScale?.invoke())
                    }
                    // 把计算好的更改设置到view中
                    calculators?.forEach {
                        it.setView(view, progress)
                    }
                }
            },
            // 固定的起始值与结束值
            FastBaseCalculator.AnimatorStartValue,
            FastBaseCalculator.AnimatorEndValue
        )
    }
}

