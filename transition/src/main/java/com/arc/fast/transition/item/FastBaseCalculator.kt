package com.arc.fast.transition.item

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

/**
 * 共享元素动画计算器：基类
 */
abstract class FastBaseCalculator<VALUE, VIEW : Any> {
    // 视图的类型
    protected val viewClass: KClass<VIEW>

    // 计算器起始数值
    protected val first: VALUE

    // 计算器结束数值
    protected val last: VALUE

    // 页面的当前缩放比例
    protected val pageCurrentScale: Float?

    // 计算器起始数值与结束数值之间的差值
    abstract val differ: VALUE

    // 是否优先把动画设置到子视图中，只有子视图为空时再设置到本视图
    private var isSetToChild: Boolean = false

    // 子视图列表
    private var setToChildList: List<VIEW>? = null

    // 是否已经初始化过子视图列表
    private var isInitSetToChildList = false

    constructor(
        viewClass: KClass<VIEW>,
        first: VALUE,
        last: VALUE,
        isSetToChild: Boolean = false,
        pageCurrentScale: Float? = null
    ) {
        this.viewClass = viewClass
        this.isSetToChild = isSetToChild
        this.pageCurrentScale = pageCurrentScale
        this.first = first
        this.last = if (pageCurrentScale == null || pageCurrentScale == 1f) last
        else getLastByPageCurrentScale(last, pageCurrentScale)
    }

    /**
     * 按页面的当前缩放比例返回计算器结束数值，如果您的动画需要根据页面缩放计算面积，则您可以重写该方法
     */
    open fun getLastByPageCurrentScale(last: VALUE, pageCurrentScale: Float): VALUE = last

    /**
     * 根据动画进度返回相应的值
     */
    abstract fun getValue(progress: Float): VALUE

    /**
     * 初始化子视图列表并返回
     */
    open fun initSetChildList(view: VIEW): List<VIEW>? {
        if (view is ViewGroup) {
            val childList = ArrayList<VIEW>()
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                if (!child.transitionName.isNullOrBlank()) continue
                val safeView = viewClass.safeCast(child)
                if (safeView != null) childList.add(safeView)
            }
            return childList
        }
        return null
    }

    /**
     * 设置子视图
     */
    abstract fun setView(view: VIEW, progress: Float, value: VALUE)

    /**
     * 设置子视图
     */
    fun setView(
        view: View?,
        progress: Float
    ) {
        if (view == null) return
        val safeView = viewClass.safeCast(view) ?: return
        if (isSetToChild && !isInitSetToChildList) {
            setToChildList = initSetChildList(safeView)
        }
        val value = getValue(progress)
        if (!setToChildList.isNullOrEmpty()) {
            setToChildList?.forEach {
                setView(it, progress, value)
            }
        } else {
            setView(safeView, progress, value)
        }
    }

    companion object {
        const val AnimatorStartValue = 0f
        const val AnimatorEndValue = 1f

        /**
         * 计算Float数值的便捷方法
         */
        @JvmStatic
        fun calculatorFloatValue(first: Float, last: Float, differ: Float, progress: Float): Float {
            if (first == last) return first
            val isPositiveFirst = first > last
            return (first + differ * progress).let {
                if (isPositiveFirst) {
                    if (it > first) first else if (it < last) last else it
                } else {
                    if (it > last) last else if (it < first) first else it
                }
            }
        }

        /**
         * 计算Int数值的便捷方法
         */
        @JvmStatic
        fun calculatorIntValue(first: Int, last: Int, differ: Int, progress: Float): Int {
            if (first == last) return first
            val isPositiveFirst = first > last
            return (first + differ * progress).let {
                if (isPositiveFirst) {
                    if (it > first) first else if (it < last) last else it.toInt()
                } else {
                    if (it > last) last else if (it < first) first else it.toInt()
                }
            }
        }

        /**
         * 计算Color数值的便捷方法
         */
        @JvmStatic
        fun calculatorColorValue(
            first: FastColorValue,
            last: FastColorValue,
            differ: FastColorValue,
            progress: Float
        ): Int {
            if (first == last) return first.color
            return Color.argb(
                calculatorIntValue(first.alpha, last.alpha, differ.alpha, progress),
                calculatorIntValue(first.red, last.red, differ.red, progress),
                calculatorIntValue(first.green, last.green, differ.green, progress),
                calculatorIntValue(first.blue, last.blue, differ.blue, progress),
            )
        }
    }
}