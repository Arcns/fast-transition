package com.arc.fast.transition.item.disposable

import android.app.Activity
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.arc.fast.transition.FastTransitionConfig
import com.arc.fast.transition.item.FastTransitionItem
import com.arc.fast.transition.item.simple.FastSimpleCalculator
import com.arc.fast.transition.item.simple.FastSimpleType
import com.arc.fast.view.FastTextView
import kotlinx.parcelize.Parcelize

/**
 * 共享元素动画：一次性的FastTextView
 * 该动画会在目标页面创建相同控件以完成消失动画
 */
@Parcelize
data class FastDisposableFastTextViewItem(
    var value: DisposableFastTextViewValue? = null,
) : FastTransitionItem() {
    override val enable: Boolean get() = value != null
    override fun getCalculator(
        isEnter: Boolean, pageCurrentScale: Float?
    ): FastSimpleCalculator {
        return if (isEnter) FastSimpleCalculator(FastSimpleType.Alpha, 1f, 0f, isSetToChild)
        else FastSimpleCalculator(FastSimpleType.Alpha, 0f, 1f, isSetToChild)
    }

    override fun onEnterBefore(
        activity: Activity,
        transitionConfig: FastTransitionConfig
    ) {
        val activityContentView = activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        if (transitionConfig.transitionView != null || activityContentView !is ConstraintLayout) return
        val value = value ?: return
        val view = FastTextView(activity, null).apply {
            gravity = value.gravity
            text = value.text
            textSize = value.textSize
            setTextColor(value.textColor)
            setPadding(value.paddingLeft, value.paddingTop, value.paddingRight, value.paddingBottom)
            typeface =
                Typeface.defaultFromStyle(if (value.isBold) Typeface.BOLD else Typeface.NORMAL)
            leftImageWidth = value.leftImgWidth
            leftImageHeight = value.leftImgHeight
            topImageWidth = value.topImgWidth
            topImageHeight = value.topImgHeight
            rightImageWidth = value.rightImgWidth
            rightImageHeight = value.rightImgHeight
            bottomImageWidth = value.bottomImgWidth
            bottomImageHeight = value.bottomImgHeight
            compoundDrawablePadding = value.drawablePadding
            if (value.images.getOrNull(0) != null) {
                setImage(
                    FastTextView.ImageDirection.Left,
                    BitmapDrawable(activity.resources, value.images[0])
                )
            }
            if (value.images.getOrNull(1) != null) {
                setImage(
                    FastTextView.ImageDirection.Top,
                    BitmapDrawable(activity.resources, value.images[1])
                )
            }
            if (value.images.getOrNull(2) != null) {
                setImage(
                    FastTextView.ImageDirection.Right,
                    BitmapDrawable(activity.resources, value.images[2])
                )
            }
            if (value.images.getOrNull(3) != null) {
                setImage(
                    FastTextView.ImageDirection.Bottom,
                    BitmapDrawable(activity.resources, value.images[3])
                )
            }
        }
        activityContentView.addView(
            view,
            ConstraintLayout.LayoutParams(
                value.width, value.height
            ).apply {
                setMargins(value.marginLeft, value.marginTop, value.marginRight, value.marginBottom)
                if (value.alignParentLeft) leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
                if (value.alignParentTop) topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                if (value.alignParentRight) rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
                if (value.alignParentBottom) bottomToBottom =
                    ConstraintLayout.LayoutParams.PARENT_ID
            }
        )
        transitionConfig.transitionView = view
    }
}

