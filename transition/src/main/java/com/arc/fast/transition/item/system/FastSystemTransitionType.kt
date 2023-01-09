package com.arc.fast.transition.item.system

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 共享元素系统动画类型
 */
@Parcelize
enum class FastSystemTransitionType : Parcelable {
    Fade, Explode, ChangeBounds, ChangeClipBounds, ChangeTransform, ChangeImageTransform
}