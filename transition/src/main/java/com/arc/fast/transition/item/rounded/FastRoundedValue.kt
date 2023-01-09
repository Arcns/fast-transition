package com.arc.fast.transition.item.rounded

import android.os.Parcelable
import androidx.annotation.ColorInt
import com.arc.fast.transition.item.FastColorValue
import com.arc.fast.view.rounded.IRoundedView
import com.arc.fast.view.rounded.RoundedRadius
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


/**
 * 共享元素动画参数：圆角
 */
@Parcelize
data class FastRoundedValue(
    var roundedRadius: RoundedRadius,
    @ColorInt var roundedBackground: Int?
) : Parcelable {
    constructor(
        roundedView: IRoundedView
    ) : this(
        if (roundedView.enableRoundedRadius) RoundedRadius(roundedView) else RoundedRadius(),
        roundedView._config.backgroundColor
    )

    constructor(
        roundedRadiusTopLeft: Float = 0f,
        roundedRadiusTopRight: Float = 0f,
        roundedRadiusBottomLeft: Float = 0f,
        roundedRadiusBottomRight: Float = 0f,
        @ColorInt roundedBackground: Int? = null
    ) : this(
        RoundedRadius(
            roundedRadiusTopLeft,
            roundedRadiusTopRight,
            roundedRadiusBottomLeft,
            roundedRadiusBottomRight
        ),
        roundedBackground
    )

    constructor(
        roundedRadius: Float,
        @ColorInt roundedBackground: Int? = null
    ) : this(
        RoundedRadius(roundedRadius),
        roundedBackground
    )

    @IgnoredOnParcel
    var roundedBackgroundColorValue: FastColorValue? = roundedBackground?.let { FastColorValue(it) }
}

