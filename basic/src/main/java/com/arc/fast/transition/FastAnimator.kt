package com.arc.fast.transition

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import androidx.lifecycle.LifecycleOwner
import com.arc.fast.transition.extensions.bindLifecycle
import com.arc.fast.transition.item.FastColorValue

/**
 * 快捷动画工具
 */
object FastAnimator {

    /**
     * 创建FloatValueAnimator
     */
    @JvmStatic
    @JvmOverloads
    fun createFloatValueAnimator(
        start: Float = 0f,
        end: Float = 1f,
        startDelay: Long = 0,
        duration: Long = 300,
        lifecycleOwner: LifecycleOwner? = null,
        listener: DefaultAnimatorListener? = null,
        onUpdate: (Float) -> Unit
    ): ValueAnimator = createFloatValueAnimator(
        floatArrayOf(start, end), startDelay, duration, lifecycleOwner, listener, onUpdate
    )

    /**
     * 创建FloatValueAnimator
     */
    @JvmStatic
    @JvmOverloads
    fun createFloatValueAnimator(
        values: FloatArray,
        startDelay: Long = 0,
        duration: Long = 300,
        lifecycleOwner: LifecycleOwner? = null,
        listener: DefaultAnimatorListener? = null,
        onUpdate: (Float) -> Unit
    ): ValueAnimator {
        return ValueAnimator().apply {
            setFloatValues(*values)
            this.startDelay = startDelay
            this.duration = duration
            if (lifecycleOwner != null) bindLifecycle(lifecycleOwner)
            this.addUpdateListener {
                onUpdate(it.animatedValue as Float)
            }
            if (listener != null) addListener(listener)
        }
    }

    /**
     * 启动FloatValueAnimator
     */
    @JvmStatic
    @JvmOverloads
    fun startFloatValueAnimator(
        start: Float = 0f,
        end: Float = 1f,
        startDelay: Long = 0,
        duration: Long = 300,
        lifecycleOwner: LifecycleOwner? = null,
        listener: DefaultAnimatorListener? = null,
        onUpdate: (Float) -> Unit
    ): ValueAnimator = createFloatValueAnimator(
        start, end, startDelay, duration, lifecycleOwner, listener, onUpdate
    ).apply { start() }

    /**
     * 启动FloatValueAnimator
     */
    @JvmStatic
    @JvmOverloads
    fun startFloatValueAnimator(
        values: FloatArray,
        startDelay: Long = 0,
        duration: Long = 300,
        lifecycleOwner: LifecycleOwner? = null,
        listener: DefaultAnimatorListener? = null,
        onUpdate: (Float) -> Unit
    ): ValueAnimator = createFloatValueAnimator(
        values, startDelay, duration, lifecycleOwner, listener, onUpdate
    ).apply { start() }

    /**
     * 创建IntValueAnimator
     */
    @JvmStatic
    @JvmOverloads
    fun createIntValueAnimator(
        start: Int = 0,
        end: Int = 100,
        startDelay: Long = 0,
        duration: Long = 300,
        lifecycleOwner: LifecycleOwner? = null,
        listener: DefaultAnimatorListener? = null,
        onUpdate: (Int) -> Unit
    ): ValueAnimator = createIntValueAnimator(
        intArrayOf(start, end), startDelay, duration, lifecycleOwner, listener, onUpdate
    )

    /**
     * 创建IntValueAnimator
     */
    @JvmStatic
    @JvmOverloads
    fun createIntValueAnimator(
        values: IntArray,
        startDelay: Long = 0,
        duration: Long = 300,
        lifecycleOwner: LifecycleOwner? = null,
        listener: DefaultAnimatorListener? = null,
        onUpdate: (Int) -> Unit
    ): ValueAnimator {
        return ValueAnimator().apply {
            setIntValues(*values)
            this.startDelay = startDelay
            this.duration = duration
            if (lifecycleOwner != null) bindLifecycle(lifecycleOwner)
            this.addUpdateListener {
                onUpdate(it.animatedValue as Int)
            }
            if (listener != null) addListener(listener)
        }
    }

    /**
     * 启动IntValueAnimator
     */
    @JvmStatic
    @JvmOverloads
    fun startIntValueAnimator(
        start: Int = 0,
        end: Int = 100,
        startDelay: Long = 0,
        duration: Long = 300,
        lifecycleOwner: LifecycleOwner? = null,
        listener: DefaultAnimatorListener? = null,
        onUpdate: (Int) -> Unit
    ): ValueAnimator = createIntValueAnimator(
        start, end, startDelay, duration, lifecycleOwner, listener, onUpdate
    ).apply { start() }

    /**
     * 启动IntValueAnimator
     */
    @JvmStatic
    @JvmOverloads
    fun startIntValueAnimator(
        values: IntArray,
        startDelay: Long = 0,
        duration: Long = 300,
        lifecycleOwner: LifecycleOwner? = null,
        listener: DefaultAnimatorListener? = null,
        onUpdate: (Int) -> Unit
    ): ValueAnimator = createIntValueAnimator(
        values, startDelay, duration, lifecycleOwner, listener, onUpdate
    ).apply { start() }

    /**
     * 计算Float数值的便捷方法
     */
    @JvmStatic
    fun calculatorFloatValue(first: Float, last: Float, differ: Float, progress: Float): Float {
        if (first == last) return first
        val isPositiveFirst = first > last
        return (first + differ * progress).let {
            if (isPositiveFirst) {
                if (it > first) first else if (it < last) last else it
            } else {
                if (it > last) last else if (it < first) first else it
            }
        }
    }

    /**
     * 计算Int数值的便捷方法
     */
    @JvmStatic
    fun calculatorIntValue(first: Int, last: Int, differ: Int, progress: Float): Int {
        if (first == last) return first
        val isPositiveFirst = first > last
        return (first + differ * progress).let {
            if (isPositiveFirst) {
                if (it > first) first else if (it < last) last else it.toInt()
            } else {
                if (it > last) last else if (it < first) first else it.toInt()
            }
        }
    }

    /**
     * 计算Color数值的便捷方法
     */
    @JvmStatic
    fun calculatorColorValue(
        first: FastColorValue,
        last: FastColorValue,
        differ: FastColorValue,
        progress: Float
    ): Int {
        if (first == last) return first.color
        return Color.argb(
            calculatorIntValue(first.alpha, last.alpha, differ.alpha, progress),
            calculatorIntValue(first.red, last.red, differ.red, progress),
            calculatorIntValue(first.green, last.green, differ.green, progress),
            calculatorIntValue(first.blue, last.blue, differ.blue, progress),
        )
    }
}

open class DefaultAnimatorListener : Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator) {}
    override fun onAnimationEnd(animation: Animator) {}
    override fun onAnimationCancel(animation: Animator) {}
    override fun onAnimationRepeat(animation: Animator) {}
}