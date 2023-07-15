package com.arc.fast.transition.item.bgfade

import com.arc.fast.transition.item.FastTransitionItem
import kotlinx.parcelize.Parcelize

/**
 * 共享元素动画：背景渐变
 */
@Parcelize
data class FastBackgroundFadeItem(
    var backgroundRes: Int,
    var showToHide: Boolean = true
) : FastTransitionItem() {

    override val enable: Boolean get() = true
    override fun getCalculator(
        isEnter: Boolean,
        pageCurrentScale: Float?
    ): FastBackgroundFadeCalculator? {
        // 0 表示完全透明，255 表示完全不透明
        val start: Float
        val end: Float
        if (showToHide) {
            start = 255f
            end = 0f
        } else {
            start = 0f
            end = 255f
        }
        return if (isEnter) FastBackgroundFadeCalculator(backgroundRes, start, end)
        else FastBackgroundFadeCalculator(backgroundRes, end, start)
    }
}