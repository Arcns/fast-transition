package com.arc.fast.transition.item.rounded

import com.arc.fast.transition.item.FastBaseCalculator
import com.arc.fast.transition.FastAnimator
import com.arc.fast.view.rounded.IRoundedView
import com.arc.fast.view.rounded.RoundedRadius


/**
 * 共享元素动画计算器：圆角
 */
class FastRoundedCalculator(
    _first: FastRoundedValue,
    _last: FastRoundedValue,
    _pageCurrentScale: Float? = null
) : FastBaseCalculator<FastRoundedValue, IRoundedView>(
    viewClass = IRoundedView::class,
    first = _first,
    last = _last,
    pageCurrentScale = _pageCurrentScale
) {

    override fun getLastByPageCurrentScale(
        last: FastRoundedValue,
        pageCurrentScale: Float
    ): FastRoundedValue = FastRoundedValue(
        last.roundedRadius / pageCurrentScale,
        last.roundedBackground
    )

    override val differ: FastRoundedValue by lazy {
        FastRoundedValue(
            last.roundedRadius - first.roundedRadius,
            null
        ).apply {
            if (first.roundedBackgroundColorValue != null && last.roundedBackgroundColorValue != null) {
                roundedBackgroundColorValue =
                    last.roundedBackgroundColorValue!! - first.roundedBackgroundColorValue!!
            }
        }
    }

    override fun getValue(progress: Float): FastRoundedValue = FastRoundedValue(
        roundedRadius = RoundedRadius(
            FastAnimator.calculatorFloatValue(
                first.roundedRadius.roundedRadiusTopLeft,
                last.roundedRadius.roundedRadiusTopLeft,
                differ.roundedRadius.roundedRadiusTopLeft,
                progress
            ),
            FastAnimator.calculatorFloatValue(
                first.roundedRadius.roundedRadiusTopRight,
                last.roundedRadius.roundedRadiusTopRight,
                differ.roundedRadius.roundedRadiusTopRight,
                progress
            ),
            FastAnimator.calculatorFloatValue(
                first.roundedRadius.roundedRadiusBottomLeft,
                last.roundedRadius.roundedRadiusBottomLeft,
                differ.roundedRadius.roundedRadiusBottomLeft,
                progress
            ),
            FastAnimator.calculatorFloatValue(
                first.roundedRadius.roundedRadiusBottomRight,
                last.roundedRadius.roundedRadiusBottomRight,
                differ.roundedRadius.roundedRadiusBottomRight,
                progress
            )
        ),
        roundedBackground = null
    ).apply {
        if (first.roundedBackgroundColorValue != null && last.roundedBackgroundColorValue != null && differ.roundedBackgroundColorValue != null) {
            roundedBackground = FastAnimator.calculatorColorValue(
                first.roundedBackgroundColorValue!!,
                last.roundedBackgroundColorValue!!,
                differ.roundedBackgroundColorValue!!,
                progress
            )
        }
    }

    override fun setView(view: IRoundedView, progress: Float, value: FastRoundedValue) {
        view.setTemporarilyRoundedRadius(value.roundedRadius, value.roundedBackground)
    }

}