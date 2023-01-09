package com.arc.fast.transition.item.textview

import android.os.Parcelable
import android.text.TextUtils
import android.widget.TextView
import com.arc.fast.transition.item.FastColorValue
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


/**
 * 共享元素动画参数：文本
 */
@Parcelize
data class FastTextViewValue(
    var textSize: Float,
    var textColor: Int,
    var maxLines: Int,
    var ellipsizeValue: String?,
    var lineSpacingExtra: Float,
    var paddingLeft: Float,
    var paddingRight: Float,
    var paddingTop: Float,
    var paddingBottom: Float,
    var isBold: Boolean,
    var alpha: Float
) : Parcelable {
    constructor(
        textView: TextView
    ) : this(
        textView.textSize / textView.resources.displayMetrics.scaledDensity,
        textView.textColors.defaultColor,
        textView.maxLines,
        textView.ellipsize?.name,
        textView.lineSpacingExtra,
        textView.paddingLeft.toFloat(),
        textView.paddingRight.toFloat(),
        textView.paddingTop.toFloat(),
        textView.paddingBottom.toFloat(),
        textView.typeface.isBold,
        textView.alpha
    )

    val ellipsize: TextUtils.TruncateAt?
        get() = ellipsizeValue?.let {
            TextUtils.TruncateAt.valueOf(
                it
            )
        }

    @IgnoredOnParcel
    var textShareColorValue: FastColorValue = FastColorValue(textColor)
}