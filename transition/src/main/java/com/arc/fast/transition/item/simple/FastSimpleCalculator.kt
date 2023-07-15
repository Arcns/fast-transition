package com.arc.fast.transition.item.simple

import android.view.View
import com.arc.fast.transition.item.FastBaseCalculator


/**
 * 共享元素动画计算器：渐变/缩放
 */
class FastSimpleCalculator(
    var type: FastSimpleType,
    _first: Float,
    _last: Float,
    _isSetToChild: Boolean,
) : FastBaseCalculator<Float, View>(
    viewClass = View::class,
    first = _first,
    last = _last,
    isSetToChild = _isSetToChild
) {
    override val differ: Float by lazy { last - first }

    override fun getValue(progress: Float): Float =
        calculatorFloatValue(first, last, differ, progress)

    override fun setView(view: View, progress: Float, value: Float) {
        when (type) {
            FastSimpleType.Scale -> {
                view.scaleX = value
                view.scaleY = value
            }
            FastSimpleType.Alpha -> {
                view.alpha = value
            }
        }
    }

}