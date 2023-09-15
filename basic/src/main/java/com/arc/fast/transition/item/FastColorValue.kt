package com.arc.fast.transition.item

import android.graphics.Color
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 颜色参数
 */
@Parcelize
data class FastColorValue(
    val red: Int,
    val blue: Int,
    val green: Int,
    val alpha: Int
) : Parcelable {
    constructor(color: Int) : this(
        Color.red(color),
        Color.blue(color),
        Color.green(color),
        Color.alpha(color)
    )

    operator fun minus(target: FastColorValue): FastColorValue {
        return FastColorValue(
            red - target.red,
            blue - target.blue,
            green - target.green,
            alpha - target.alpha
        )
    }

    val color: Int get() = Color.argb(alpha, red, green, blue)
}