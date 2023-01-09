package com.arc.fast.transition.sample.simple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.databinding.DataBindingUtil
import com.arc.fast.transition.FastTransitionTargetManager
import com.arc.fast.transition.sample.R
import com.arc.fast.transition.sample.TestData
import com.arc.fast.transition.sample.databinding.ActivitySimple2Binding
import com.arc.fast.transition.sample.extension.applyFullScreen
import com.arc.fast.transition.sample.extension.setLightSystemBar

class Simple2Activity : AppCompatActivity() {

    lateinit var binding: ActivitySimple2Binding

    // 获取目标页转场管理器
    private val transitionTargetManager by lazy {
        FastTransitionTargetManager.getManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyFullScreen()
        setLightSystemBar(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_simple2)
        binding.toolbar.apply {
            this.navigationIcon = DrawerArrowDrawable(this@Simple2Activity).apply {
                progress = 1f
            }
            this.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        // 在目标页设置需要对应参与转场的共享元素
        transitionTargetManager?.setTransitionView(TestData.KEY_IMAGE, binding.ivImage)
        transitionTargetManager?.setTransitionView(TestData.KEY_TITLE, binding.tvTitle)
        // 在目标页应用转场动画
        transitionTargetManager?.applyTransitionEnterAndReturnConfig(
//            postponeEnterTransition = true // 这里先暂停转场，等待页面准备好后再调用startTransitionEnter启动动画
        )
//        binding.rv.doOnPreDraw {
//            transitionTargetManager?.startTransitionEnter()
//        }
    }

}