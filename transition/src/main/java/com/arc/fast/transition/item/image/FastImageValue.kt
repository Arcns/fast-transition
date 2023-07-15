package com.arc.fast.transition.item.image

import android.graphics.Bitmap
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * 共享元素动画参数：图片
 */
@Parcelize
data class FastImageValue(
    var image: Bitmap?
) : Parcelable {

    @IgnoredOnParcel
    var alpha: Int? = null

    constructor(
        type: FastImageType, view: View
    ) : this(
        when (type) {
            FastImageType.Background -> view.let {
                if (it.measuredWidth > 0 && it.measuredHeight > 0)
                    it.background?.toBitmap(
                        it.measuredWidth,
                        it.measuredHeight
                    )
                else null
            }
            FastImageType.ImageViewSrc -> (view as? ImageView)?.drawable?.toBitmap()
            else -> null
        }
    )
}