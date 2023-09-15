package com.arc.fast.transition.item.image

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 共享元素动画简单类型
 */
@Parcelize
enum class FastImageType : Parcelable {
    ImageViewSrc, Background
}