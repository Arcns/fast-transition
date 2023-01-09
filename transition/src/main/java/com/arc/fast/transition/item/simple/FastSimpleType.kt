package com.arc.fast.transition.item.simple

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * 共享元素动画简单类型：渐变/缩放
 */
@Parcelize
enum class FastSimpleType : Parcelable {
    Scale, Alpha
}