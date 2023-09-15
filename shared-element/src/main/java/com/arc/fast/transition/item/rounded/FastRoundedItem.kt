package com.arc.fast.transition

import android.view.View
import com.arc.fast.transition.item.FastTransitionItem
import com.arc.fast.transition.item.rounded.FastRoundedCalculator
import com.arc.fast.transition.item.rounded.FastRoundedValue
import com.arc.fast.view.rounded.IRoundedView
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * 共享元素动画：圆角
 */
@Parcelize
data class FastRoundedItem(
    var start: FastRoundedValue? = null
) : FastTransitionItem() {
    @IgnoredOnParcel
    private var end: FastRoundedValue? = null
    override val enable: Boolean get() = start != end
    override fun getCalculator(
        isEnter: Boolean,
        pageCurrentScale: Float?
    ): FastRoundedCalculator? {
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

    override fun onViewAnimReady(isEnter: Boolean, view: View, pageCurrentScale: Float?) {
        if (view is IRoundedView) {
            end = FastRoundedValue(view)
        }
    }
}
