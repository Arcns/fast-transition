package com.arc.fast.transition.sample.simple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.databinding.DataBindingUtil
import com.arc.fast.core.extensions.dp
import com.arc.fast.transition.FastRoundedItem
import com.arc.fast.transition.FastTransitionViewManager
import com.arc.fast.transition.item.bgfade.FastBackgroundFadeItem
import com.arc.fast.transition.item.disposable.FastDisposableFastTextViewItem
import com.arc.fast.transition.item.rounded.FastRoundedValue
import com.arc.fast.transition.item.system.FastSystemTransitionItem
import com.arc.fast.transition.item.system.FastSystemTransitionType
import com.arc.fast.transition.item.textview.FastTextViewItem
import com.arc.fast.transition.item.textview.FastTextViewValue
import com.arc.fast.transition.sample.R
import com.arc.fast.transition.sample.TestData
import com.arc.fast.transition.sample.databinding.ActivitySimpleBinding
import com.arc.fast.transition.sample.extension.applyFullScreen
import com.arc.fast.transition.sample.extension.setLightSystemBar

class SimpleActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SimpleActivity::class.java))
        }
    }

    lateinit var binding: ActivitySimpleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyFullScreen()
        setLightSystemBar(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_simple)
        binding.toolbar.apply {
            this.navigationIcon = DrawerArrowDrawable(this@SimpleActivity).apply {
                progress = 1f
            }
            this.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        binding.btnStart.setOnClickListener {
            // 在起始页跳转到目标页时：
            // 1、添加需要参与转场的共享元素并配置动画
            val fastTransitionViewManager = FastTransitionViewManager()
            fastTransitionViewManager.addView(
                TestData.KEY_IMAGE,
                binding.ivImage,
                FastRoundedItem(FastRoundedValue(12f.dp)),//圆角动画
                FastSystemTransitionItem(FastSystemTransitionType.ChangeImageTransform)//图片切换动画
            )
            fastTransitionViewManager.addView(
                TestData.KEY_TITLE,
                binding.tvTitle,
                FastTextViewItem(FastTextViewValue(binding.tvTitle)),//textview切换动画
            )
            // 2、通过startActivity启动目标页面
            fastTransitionViewManager.startActivity(
                activity = this@SimpleActivity,
                targetActivityCLass = Simple2Activity::class.java
            )
        }
    }

}