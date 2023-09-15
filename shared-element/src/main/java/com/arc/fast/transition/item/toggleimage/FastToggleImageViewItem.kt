package com.arc.fast.transition.item.toggleimage

import android.view.View
import android.widget.ImageView
import com.arc.fast.transition.item.FastTransitionItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * 共享元素动画：可切换的图片
 */
@Parcelize
data class FastToggleImageViewItem(
    var start: FastToggleImageViewValue? = null
) : FastTransitionItem() {
    @IgnoredOnParcel
    private var end: FastToggleImageViewValue? = null
    override val enable: Boolean get() = start != end && start != null && end != null
    override fun getCalculator(
        isEnter: Boolean, pageCurrentScale: Float?
    ): FastToggleImageViewCalculator? {
        return if (isEnter) FastToggleImageViewCalculator(start!!, end!!)
        else FastToggleImageViewCalculator(end!!, start!!)
    }

    override fun onViewAnimReady(isEnter: Boolean, view: View, pageCurrentScale: Float?) {
        if (view is ImageView) {
            end = FastToggleImageViewValue(view)
        }
    }
}

