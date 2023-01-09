package com.arc.fast.transition.item.toggleimage

import android.os.Parcelable
import android.widget.ImageView
import com.arc.fast.transition.R
import kotlinx.parcelize.Parcelize

/**
 * 共享元素动画参数：可切换的图片
 */
@Parcelize
data class FastToggleImageViewValue(
    var isSelect: Boolean?,
    var normalIcon: Int?,
    var selectIcon: Int?
) : Parcelable {
    constructor(
        imageView: ImageView
    ) : this(
        imageView.getTag(R.id.fast_toggle_imageview_is_select) as? Boolean,
        imageView.getTag(R.id.fast_toggle_imageview_normal_icon) as? Int,
        imageView.getTag(R.id.fast_toggle_imageview_select_icon) as? Int
    )

    fun getIcon(isSelect: Boolean?): Int? {
        return if (isSelect == null) null
        else if (isSelect) selectIcon
        else normalIcon
    }
}