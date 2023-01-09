package com.arc.fast.transition.item.toggleimage

import android.widget.ImageView
import com.arc.fast.transition.item.FastBaseCalculator


/**
 * 共享元素动画计算器：可切换的图片
 */
class FastToggleImageViewCalculator(
    _first: FastToggleImageViewValue, _last: FastToggleImageViewValue
) : FastBaseCalculator<FastToggleImageViewValue, ImageView>(
    viewClass = ImageView::class, first = _first, last = _last
) {

    override val differ: FastToggleImageViewValue by lazy { last }

    override fun getValue(progress: Float): FastToggleImageViewValue =
        if (progress >= 0.5f) last else first

    override fun setView(view: ImageView, progress: Float, value: FastToggleImageViewValue) {
        view.setImageResource(value.getIcon(first.isSelect) ?: return)
    }
}