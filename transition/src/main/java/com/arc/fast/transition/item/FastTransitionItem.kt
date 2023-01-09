package com.arc.fast.transition.item

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.arc.fast.transition.FastTransitionConfig

/**
 * 共享元素动画：自定义动画的基类
 */
abstract class FastTransitionItem(
    // 是否优先把动画设置到子视图中，只有子视图为空时再设置到本视图
    open var isSetToChild: Boolean = false
) : FastBaseItem() {
    // 动画是否可用
    abstract val enable: Boolean

    // 获取动画计算器
    abstract fun getCalculator(
        isEnter: Boolean,
        pageCurrentScale: Float?
    ): FastBaseCalculator<*, *>?

    // 视图已准备好，您可以在此处进行视图相关的初始化
    open fun initByView(isEnter: Boolean, view: View, pageCurrentScale: Float? = null) {}

    // 执行进入动画前的回调，您可以在此进行动画前的初始化工作
    open fun onEnterBefore(
        activity: Activity,
        transitionConfig: FastTransitionConfig
    ) {
    }

    // 执行离开动画前的回调，您可以在此进行动画前的初始化工作
    open fun onReturnBefore(view: View, pageCurrentScale: Float? = null) {}
}