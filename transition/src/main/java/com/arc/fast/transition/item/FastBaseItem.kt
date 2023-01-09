package com.arc.fast.transition.item

import android.os.Parcelable
import android.transition.Transition

/**
 * 共享元素动画：基类
 */
abstract class FastBaseItem : Parcelable {

    /**
     * 返回单独的动画
     */
    open fun getItemTransition(
        isEnter: Boolean,
        pageCurrentScale: (() -> Float)? = null
    ): Transition? = null
}
