package com.arc.fast.transition.item.disposable

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import com.arc.fast.view.FastTextView
import kotlinx.parcelize.Parcelize


/**
 * 共享元素动画参数：文本
 */
@Parcelize
data class DisposableFastTextViewValue(
    var width: Int,
    var height: Int,
    var gravity: Int,
    var text: String,
    var textSize: Float,
    var textColor: Int,
    var paddingLeft: Int,
    var paddingTop: Int,
    var paddingRight: Int,
    var paddingBottom: Int,
    var marginLeft: Int,
    var marginTop: Int,
    var marginRight: Int,
    var marginBottom: Int,
    var isBold: Boolean,
    var leftImgWidth: Int,
    var leftImgHeight: Int,
    var topImgWidth: Int,
    var topImgHeight: Int,
    var rightImgWidth: Int,
    var rightImgHeight: Int,
    var bottomImgHeight: Int,
    var bottomImgWidth: Int,
    var drawablePadding: Int,
    var images: List<Bitmap?>,
    var alignParentLeft: Boolean,
    var alignParentTop: Boolean,
    var alignParentRight: Boolean,
    var alignParentBottom: Boolean,
) : Parcelable {
    constructor(
        textView: FastTextView
    ) : this(
        textView.layoutParams.width,
        textView.layoutParams.height,
        textView.gravity,
        textView.text.toString(),
        textView.textSize / textView.resources.displayMetrics.scaledDensity,
        textView.textColors.defaultColor,
        textView.paddingLeft,
        textView.paddingTop,
        textView.paddingRight,
        textView.paddingBottom,
        (textView.layoutParams as ViewGroup.MarginLayoutParams).leftMargin,
        (textView.layoutParams as ViewGroup.MarginLayoutParams).topMargin,
        (textView.layoutParams as ViewGroup.MarginLayoutParams).rightMargin,
        (textView.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin,
        textView.typeface.isBold,
        textView.leftImageWidth,
        textView.leftImageHeight,
        textView.topImageWidth,
        textView.topImageHeight,
        textView.rightImageWidth,
        textView.rightImageHeight,
        textView.bottomImageWidth,
        textView.bottomImageHeight,
        textView.compoundDrawablePadding,
        textView.compoundDrawables.let {
            listOf(
                it.getOrNull(0).toBitmapOrNull(textView.leftImageWidth, textView.leftImageHeight),
                it.getOrNull(1).toBitmapOrNull(textView.topImageWidth, textView.topImageHeight),
                it.getOrNull(2).toBitmapOrNull(textView.rightImageWidth, textView.rightImageHeight),
                it.getOrNull(3)
                    .toBitmapOrNull(textView.bottomImageWidth, textView.bottomImageHeight)
            )
        },
        (textView.layoutParams as? ConstraintLayout.LayoutParams)?.leftToLeft == ConstraintLayout.LayoutParams.PARENT_ID,
        (textView.layoutParams as? ConstraintLayout.LayoutParams)?.topToTop == ConstraintLayout.LayoutParams.PARENT_ID,
        (textView.layoutParams as? ConstraintLayout.LayoutParams)?.rightToRight == ConstraintLayout.LayoutParams.PARENT_ID,
        (textView.layoutParams as? ConstraintLayout.LayoutParams)?.bottomToBottom == ConstraintLayout.LayoutParams.PARENT_ID,
    )
}

private fun Drawable?.toBitmapOrNull(width: Int, height: Int): Bitmap? {
    if (this == null || width <= 0 || height <= 0) return null
    return toBitmap(width, height)
}