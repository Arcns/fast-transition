package com.arc.fast.transition.item.bgfade

import android.view.View
import com.arc.fast.transition.item.FastBaseCalculator


/**
 * 共享元素动画计算器：背景渐变
 */
class FastBackgroundFadeCalculator(
    var backgroundRes: Int,
    _first: Float,
    _last: Float
) : FastBaseCalculator<Float, View>(
    viewClass = View::class,
    first = _first,
    last = _last
) {
    override val differ: Float by lazy { last - first }

    override fun getValue(progress: Float): Float =
        calculatorFloatValue(first, last, differ, progress)

    override fun setView(view: View, progress: Float, value: Float) {
        if (view.background == null) {
            view.setBackgroundResource(backgroundRes)
        }
        view.background = view.background?.apply {
            // 0 表示完全透明，255 表示完全不透明
            alpha = value.toInt()
        }
    }

}