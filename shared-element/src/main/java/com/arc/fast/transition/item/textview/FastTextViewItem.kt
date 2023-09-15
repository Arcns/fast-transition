package com.arc.fast.transition.item.textview

import android.view.View
import android.widget.TextView
import com.arc.fast.transition.item.FastTransitionItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * 共享元素动画：文本
 */
@Parcelize
data class FastTextViewItem(
    var start: FastTextViewValue? = null
) : FastTransitionItem() {
    @IgnoredOnParcel
    private var end: FastTextViewValue? = null
    override val enable: Boolean get() = start != end && start != null && end != null
    override fun getCalculator(
        isEnter: Boolean, pageCurrentScale: Float?
    ): FastTextViewCalculator? {
        return if (isEnter) FastTextViewCalculator(start!!, end!!, pageCurrentScale)
        else FastTextViewCalculator(end!!, start!!, pageCurrentScale)
    }

    override fun onViewAnimReady(isEnter: Boolean, view: View, pageCurrentScale: Float?) {
        if (view is TextView) {
            end = FastTextViewValue(view)
        }
    }

    override fun onReturnBefore(view: View, pageCurrentScale: Float?) {
        if (pageCurrentScale != null && pageCurrentScale != 1f) {
            view.apply {
                scaleX = 1f / pageCurrentScale
                scaleY = 1f / pageCurrentScale
                alpha = 0f //有缩放时，为了不让用户感知忽然变大，所以先变透明
            }
        }
    }
}

