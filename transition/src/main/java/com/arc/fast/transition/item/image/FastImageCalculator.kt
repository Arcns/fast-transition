package com.arc.fast.transition.item.image

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import com.arc.fast.transition.item.FastBaseCalculator


/**
 * 共享元素动画计算器：图片
 */
class FastImageCalculator(
    var type: FastImageType,
    _first: FastImageValue,
    _last: FastImageValue,
    var isSetAlphaToChild: Boolean,
    var isEnter: Boolean,
) : FastBaseCalculator<FastImageValue, View>(
    viewClass = View::class, first = _first, last = _last
) {
    private var isInitChilds = false
    private var childs: List<View>? = null

    override val differ: FastImageValue by lazy { last }

    override fun getValue(progress: Float): FastImageValue =
        if (progress >= 0.5f) last.apply {
            // 显示last
            // 0 表示完全透明，255 表示完全不透明
            alpha = calculatorIntValue(0, 255, -255, 1 - progress * 2)
        }
        else first.apply {
            // 显示first
            // 0 表示完全透明，255 表示完全不透明
            alpha = calculatorIntValue(255, 0, -255, progress * 2)
        }

    override fun setView(view: View, progress: Float, value: FastImageValue) {
        if (isSetAlphaToChild) {
            if (!isInitChilds) {
                isInitChilds = true
                childs = initSetChildList(view)
            }
            if ((isEnter && value == first) || (!isEnter && value == last)) {
                childs?.forEach { it.alpha = 0f }
            } else {
                childs?.forEach { it.alpha = value.alpha!! / 255f }
            }
        }
        val drawable = BitmapDrawable(
            view.context.resources,
            value.image
        ).apply {
            if (value.alpha != null)
                alpha = value.alpha!!
        }
        when (type) {
            FastImageType.Background ->
                view.background = drawable
            FastImageType.ImageViewSrc ->
                (view as? ImageView)?.setImageDrawable(drawable)
        }
    }
}
