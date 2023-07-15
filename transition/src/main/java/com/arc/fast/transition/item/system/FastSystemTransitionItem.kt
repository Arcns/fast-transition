package com.arc.fast.transition.item.system

import android.transition.*
import com.arc.fast.transition.item.FastBaseItem
import kotlinx.parcelize.Parcelize

/**
 * 共享元素系统动画
 */
@Parcelize
class FastSystemTransitionItem(
    val type: FastSystemTransitionType
) : FastBaseItem() {

    override fun getItemTransition(
        isEnter: Boolean,
        pageCurrentScale: (() -> Float)?
    ): Transition {
        return when (type) {
            FastSystemTransitionType.Fade -> Fade()
            FastSystemTransitionType.Explode -> Explode()
            FastSystemTransitionType.ChangeBounds -> ChangeBounds()
            FastSystemTransitionType.ChangeClipBounds -> ChangeClipBounds()
            FastSystemTransitionType.ChangeTransform -> ChangeTransform()
            FastSystemTransitionType.ChangeImageTransform -> ChangeImageTransform()
        }
    }
}