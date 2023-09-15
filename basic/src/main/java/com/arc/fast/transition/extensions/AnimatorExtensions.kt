package com.arc.fast.transition.extensions

import android.animation.Animator
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner


val bindLifecycleAnimators = arrayListOf<Animator>()

fun pauseAllBindLifecycleAnimators() {
    bindLifecycleAnimators.forEach { it.pause() }
}

/**
 * 动画绑定生命周期
 */
@JvmOverloads
fun Animator.bindLifecycle(
    // 生命周期
    lifecycleOwner: LifecycleOwner,
    // 检查是否跳过本次动作
    checkSkipAction: ((animator: Animator, isPause: Boolean) -> Boolean)? = null,
    // 生命周期暂停时执行的动作
    lifecyclePauseAction: AnimatorLifecyclePauseAction = AnimatorLifecyclePauseAction.Pause,
): Animator {
    bindLifecycleAnimators.add(this)
    val cancelAnimator = {
        bindLifecycleAnimators.remove(this@bindLifecycle)
        removeAllListeners()
        cancel()
    }
    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onPause(owner: LifecycleOwner) {
            super.onPause(owner)
            if (checkSkipAction?.invoke(this@bindLifecycle, true) == true) return
            if (lifecyclePauseAction == AnimatorLifecyclePauseAction.Pause) {
                pause()
            } else if (lifecyclePauseAction == AnimatorLifecyclePauseAction.Cancel) {
                owner.lifecycle.removeObserver(this)
                cancelAnimator()
            }
        }

        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
            if (checkSkipAction?.invoke(this@bindLifecycle, false) == true) return
            if (lifecyclePauseAction == AnimatorLifecyclePauseAction.Pause) {
                resume()
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            owner.lifecycle.removeObserver(this)
            cancelAnimator()
        }
    })
    return this
}

enum class AnimatorLifecyclePauseAction {
    Pause, Cancel, NoAction
}