package com.arc.fast.transition.item.image

import android.view.View
import com.arc.fast.transition.item.FastTransitionItem
import kotlinx.parcelize.Parcelize

/**
 * 共享元素动画：图片
 */
@Parcelize
data class FastImageItem(
    var type: FastImageType,
    var start: FastImageValue? = null,
    var end: FastImageValue? = null,
    var isSetAlphaToChild: Boolean = false
) : FastTransitionItem() {
    override val enable: Boolean get() = start != end && start != null && end != null
    override fun getCalculator(
        isEnter: Boolean, pageCurrentScale: Float?
    ): FastImageCalculator? {
        if (!enable) return null
        return if (isEnter) FastImageCalculator(type, start!!, end!!, isSetAlphaToChild, isEnter)
        else FastImageCalculator(type, end!!, start!!, isSetAlphaToChild, isEnter)
    }

    override fun initByView(isEnter: Boolean, view: View, pageCurrentScale: Float?) {
        end = FastImageValue(type, view)
    }
}



