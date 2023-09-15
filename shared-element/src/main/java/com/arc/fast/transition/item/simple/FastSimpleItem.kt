package com.arc.fast.transition.item.simple

import android.view.View
import com.arc.fast.transition.item.FastTransitionItem
import kotlinx.parcelize.Parcelize

/**
 * 共享元素动画：渐变/缩放
 */
@Parcelize
data class FastSimpleItem(
    var type: FastSimpleType,
    var start: Float = 0f,
    var end: Float = 0f,
    override var isSetToChild: Boolean = true,
    var isReturnBeforeScale: Boolean = false
) : FastTransitionItem(isSetToChild) {

    override val enable: Boolean get() = start != end && start >= 0f && end >= 0f
    override fun getCalculator(
        isEnter: Boolean,
        pageCurrentScale: Float?
    ): FastSimpleCalculator {
        return if (isEnter) FastSimpleCalculator(type, start, end, isSetToChild)
        else FastSimpleCalculator(type, end, start, isSetToChild)
    }

    override fun onReturnBefore(view: View, pageCurrentScale: Float?) {
        if (isReturnBeforeScale && pageCurrentScale != null && pageCurrentScale != 1f) {
            view.apply {
                scaleX = 1f / pageCurrentScale
                scaleY = 1f / pageCurrentScale
            }
        }
    }
}

