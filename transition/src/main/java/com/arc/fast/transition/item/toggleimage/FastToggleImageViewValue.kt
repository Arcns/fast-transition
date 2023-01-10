package com.arc.fast.transition.item.toggleimage

import android.os.Parcelable
import android.widget.ImageView
import androidx.annotation.DrawableRes
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
        imageView.fastToggleImageviewIsSelect ?: imageView.isSelected,
        imageView.fastToggleImageviewNormalIcon,
        imageView.fastToggleImageviewSelectIcon
    )

    fun getIcon(isSelect: Boolean?): Int? {
        if (selectIcon == null || normalIcon == null) return selectIcon ?: normalIcon
        return if (isSelect == null) null
        else if (isSelect) selectIcon
        else normalIcon
    }
}

var ImageView.fastToggleImageviewIsSelect: Boolean?
    set(value) = setTag(R.id.fast_toggle_imageview_is_select, value)
    get() = getTag(R.id.fast_toggle_imageview_is_select) as? Boolean

var ImageView.fastToggleImageviewNormalIcon: Int?
    set(value) = setTag(R.id.fast_toggle_imageview_normal_icon, value)
    get() = getTag(R.id.fast_toggle_imageview_normal_icon) as? Int

var ImageView.fastToggleImageviewSelectIcon: Int?
    set(value) = setTag(R.id.fast_toggle_imageview_select_icon, value)
    get() = getTag(R.id.fast_toggle_imageview_select_icon) as? Int