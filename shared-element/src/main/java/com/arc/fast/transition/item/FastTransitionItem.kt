package com.arc.fast.transition.item

import android.app.Activity
import android.view.View
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

    // 视图动画准备（下一步将创建计算器）的回调，您可以在此处进行视图相关的初始化，例如根据视图准备目标页对应的共享元素数据
    open fun onViewAnimReady(isEnter: Boolean, view: View, pageCurrentScale: Float? = null) {}

    // 执行进入动画前的回调，您可以在此进行进入动画前的初始化工作
    open fun onEnterBefore(
        activity: Activity,
        transitionConfig: FastTransitionConfig
    ) {
    }

    // 执行离开动画前的回调，您可以在此进行离开动画前的初始化工作
    open fun onReturnBefore(view: View, pageCurrentScale: Float? = null) {}
}