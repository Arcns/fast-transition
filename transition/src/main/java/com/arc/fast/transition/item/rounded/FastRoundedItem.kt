package com.arc.fast.transition

import android.view.View
import com.arc.fast.transition.item.FastTransitionItem
import com.arc.fast.transition.item.rounded.FastRoundedCalculator
import com.arc.fast.transition.item.rounded.FastRoundedValue
import com.arc.fast.view.rounded.IRoundedView
import kotlinx.parcelize.Parcelize

/**
 * 共享元素动画：圆角
 */
@Parcelize
data class FastRoundedItem(
    var start: FastRoundedValue? = null,
    var end: FastRoundedValue? = null
) : FastTransitionItem() {
    override val enable: Boolean get() = start != end
    override fun getCalculator(
        isEnter: Boolean,
        pageCurrentScale: Float?
    ): FastRoundedCalculator? {
        if (!enable) return null
        return if (isEnter) FastRoundedCalculator(
            start ?: FastRoundedValue(),
            end ?: FastRoundedValue(),
            pageCurrentScale
        )
        else FastRoundedCalculator(
            end ?: FastRoundedValue(),
            start ?: FastRoundedValue(),
            pageCurrentScale
        )
    }

    override fun initByView(isEnter: Boolean, view: View, pageCurrentScale: Float?) {
        if (view is IRoundedView) {
            end = FastRoundedValue(view)
        }
    }
}
