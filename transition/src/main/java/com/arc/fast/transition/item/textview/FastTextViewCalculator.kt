package com.arc.fast.transition.item.textview

import android.graphics.Typeface
import android.util.Log
import android.widget.TextView
import com.arc.fast.transition.item.FastBaseCalculator


/**
 * 共享元素动画计算器：文本
 */
class FastTextViewCalculator(
    _first: FastTextViewValue, _last: FastTextViewValue, _pageCurrentScale: Float? = null
) : FastBaseCalculator<FastTextViewValue, TextView>(
    viewClass = TextView::class, first = _first, last = _last, pageCurrentScale = _pageCurrentScale
) {
    private var isApplyScale = false

    override val differ: FastTextViewValue by lazy {
        FastTextViewValue(
            last.textSize - first.textSize,
            last.textColor,
            last.maxLines,
            last.ellipsizeValue,
            last.lineSpacingExtra - first.lineSpacingExtra,
            last.paddingLeft - first.paddingLeft,
            last.paddingRight - first.paddingRight,
            last.paddingTop - first.paddingTop,
            last.paddingBottom - first.paddingBottom,
            last.isBold,
            last.alpha - first.alpha
        ).apply {
            textShareColorValue = last.textShareColorValue - first.textShareColorValue
        }
    }

    override fun getValue(progress: Float): FastTextViewValue = FastTextViewValue(
        calculatorFloatValue(first.textSize, last.textSize, differ.textSize, progress),
        calculatorColorValue(
            first.textShareColorValue,
            last.textShareColorValue,
            differ.textShareColorValue,
            progress
        ),
        if (progress >= 0.5f) last.maxLines else first.maxLines,
        if (progress >= 0.5f) last.ellipsizeValue else first.ellipsizeValue,
        calculatorFloatValue(
            first.lineSpacingExtra, last.lineSpacingExtra, differ.lineSpacingExtra, progress
        ),
        calculatorFloatValue(
            first.paddingLeft, last.paddingLeft, differ.paddingLeft, progress
        ),
        calculatorFloatValue(
            first.paddingRight, last.paddingRight, differ.paddingRight, progress
        ),
        calculatorFloatValue(
            first.paddingTop, last.paddingTop, differ.paddingTop, progress
        ),
        calculatorFloatValue(
            first.paddingBottom, last.paddingBottom, differ.paddingBottom, progress
        ),
        if (progress >= 0.5f) last.isBold else first.isBold,
        calculatorFloatValue(
            first.alpha, last.alpha, differ.alpha, progress
        ),
    )

    override fun setView(view: TextView, progress: Float, value: FastTextViewValue) {
        if (pageCurrentScale != null && pageCurrentScale != 1f && !isApplyScale) {
            isApplyScale = true
//            view.scaleX = 1f / pageCurrentScale
//            view.scaleY = 1f / pageCurrentScale
        }
        view.textSize = value.textSize
        view.setTextColor(value.textColor)
        view.maxLines = value.maxLines
        view.ellipsize = value.ellipsize
        view.setLineSpacing(
            value.lineSpacingExtra, 1f
        )
        Log.e("aaaaa", "aaaaa:" + value.lineSpacingExtra)
        view.setPadding(
            value.paddingLeft.toInt(),
            value.paddingTop.toInt(),
            value.paddingRight.toInt(),
            value.paddingBottom.toInt()
        )
        view.typeface =
            Typeface.defaultFromStyle(if (value.isBold) Typeface.BOLD else Typeface.NORMAL)
        view.alpha = value.alpha
    }
}