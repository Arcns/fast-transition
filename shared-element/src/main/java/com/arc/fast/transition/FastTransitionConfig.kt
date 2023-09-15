package com.arc.fast.transition

import android.os.Parcelable
import android.transition.Transition
import android.transition.TransitionSet
import android.view.View
import com.arc.fast.transition.item.FastBaseCalculator
import com.arc.fast.transition.item.FastBaseItem
import com.arc.fast.transition.item.FastTransitionItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * 共享元素动画配置
 */
@Parcelize
class FastTransitionConfig(
    // 代表唯一性的关键key，便于查找
    var key: String,
    // transition name
    var transitionName: String? = null,
    // 动画列表
    vararg var items: FastBaseItem
) : Parcelable {

    @IgnoredOnParcel
    var hasExecutedTransitionEnter = false

    @IgnoredOnParcel
    var transitionView: View? = null
        set(value) {
            if (field != null) field?.transitionName = null
            field = value
            field?.transitionName = transitionName
        }

    /**
     * 返回计算器列表
     */
    fun getCalculators(
        isEnter: Boolean,
        view: View,
        pageCurrentScale: Float? = null
    ): List<FastBaseCalculator<*, *>> {
        return items.mapNotNull {
            (it as? FastTransitionItem)?.apply {
                onViewAnimReady(isEnter, view, pageCurrentScale)
            }?.let { item ->
                if (item.enable) item.getCalculator(isEnter, pageCurrentScale) else null
            }
        }
    }

    /**
     * 返回供系统使用的动画
     */
    fun getTransition(
        isEnter: Boolean,
        pageCurrentScale: (() -> Float)? = null
    ): Transition? {
        return if (items.isNotEmpty()) TransitionSet().apply {
            items.forEach {
                val transition = it.getItemTransition(isEnter, pageCurrentScale)
                if (transition != null) {
                    addTransition(transition)
                }
            }
            addTransition(FastTransition(this@FastTransitionConfig, isEnter, pageCurrentScale))
        }.addTarget(transitionName) else null
    }
}

const val DEFAULT_FAST_TRANSITION_CONFIGS_INTENT_NAME =
    "DEFAULT_FAST_TRANSITION_CONFIGS_INTENT_NAME"
