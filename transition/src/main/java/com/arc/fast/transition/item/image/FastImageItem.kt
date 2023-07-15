package com.arc.fast.transition.item.image

import android.view.View
import com.arc.fast.transition.item.FastTransitionItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * 共享元素动画：图片
 */
@Parcelize
data class FastImageItem(
    var type: FastImageType,
    var start: FastImageValue? = null,
    var isSetAlphaToChild: Boolean = false
) : FastTransitionItem() {
    @IgnoredOnParcel
    private var end: FastImageValue? = null
    override val enable: Boolean get() = start != end && start != null && end != null
    override fun getCalculator(
        isEnter: Boolean, pageCurrentScale: Float?
    ): FastImageCalculator? {
        return if (isEnter) FastImageCalculator(type, start!!, end!!, isSetAlphaToChild, isEnter)
        else FastImageCalculator(type, end!!, start!!, isSetAlphaToChild, isEnter)
    }

    override fun onViewAnimReady(isEnter: Boolean, view: View, pageCurrentScale: Float?) {
        end = FastImageValue(type, view)
    }
}



