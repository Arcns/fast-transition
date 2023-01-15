package com.arc.fast.transition.sample.loopanddragexit

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.arc.fast.core.extensions.dp
import com.arc.fast.transition.FastRoundedItem
import com.arc.fast.transition.FastTransitionTargetManager
import com.arc.fast.transition.FastTransitionViewManager
import com.arc.fast.transition.item.rounded.FastRoundedValue
import com.arc.fast.transition.item.system.FastSystemTransitionItem
import com.arc.fast.transition.item.system.FastSystemTransitionType
import com.arc.fast.transition.item.textview.FastTextViewItem
import com.arc.fast.transition.item.textview.FastTextViewValue
import com.arc.fast.transition.item.toggleimage.FastToggleImageViewItem
import com.arc.fast.transition.item.toggleimage.FastToggleImageViewValue
import com.arc.fast.transition.sample.R
import com.arc.fast.transition.sample.TestData
import com.arc.fast.transition.sample.TestItem
import com.arc.fast.transition.sample.databinding.ActivityLoopAndDragExitBinding
import com.arc.fast.transition.sample.databinding.ItemLoopBinding
import com.arc.fast.transition.sample.databinding.ItemLoopHeaderBinding
import com.arc.fast.transition.sample.databinding.ItemLoopHeaderImageBinding
import com.arc.fast.transition.sample.extension.applyFullScreen
import com.arc.fast.transition.sample.extension.setLightSystemBar
import com.arc.fast.transition.sample.loop.LoopAdapter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LoopAndDragExitActivity : AppCompatActivity() {

    companion object {
        fun newIntent(
            context: Context,
            headerData: TestItem? = null
        ): Intent {
            return Intent(context, LoopAndDragExitActivity::class.java).apply {
                putExtra("headerData", headerData)
            }
        }

        fun start(
            context: Context,
            headerData: TestItem? = null
        ) {
            context.startActivity(newIntent(context, headerData))
        }
    }

    private lateinit var binding: ActivityLoopAndDragExitBinding
    private var headerItemBinding: ItemLoopHeaderBinding? = null
    private val headerData: TestItem? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("headerData", TestItem::class.java)
        } else {
            intent.getParcelableExtra("headerData")
        }
    }

    // 获取目标页转场管理器
    private val transitionTargetManager by lazy {
        FastTransitionTargetManager.getManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyFullScreen()
        setLightSystemBar(true)
        EventBus.getDefault().register(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loop_and_drag_exit)
        binding.toolbar.apply {
            this.navigationIcon = DrawerArrowDrawable(this@LoopAndDragExitActivity).apply {
                progress = 1f
            }
            this.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        // 加载测试数据
        loadTestData()
        // 在目标页应用转场动画
        transitionTargetManager?.applyTransitionEnterAndReturnConfig(
            postponeEnterTransition = true // 这里先暂停转场，等待页面准备好后再调用startTransitionEnter启动动画
        )
        binding.rv.doOnPreDraw {
            transitionTargetManager?.startTransitionEnter()
        }
        // 启用拖拽退出
        binding.dragExitLayout.enableDragExit(bindExitActivity = this)
    }

    private fun onHeaderLoadCompleted(
        itemBinding: ItemLoopHeaderBinding,
        imageBinding: ItemLoopHeaderImageBinding
    ) {
        headerItemBinding = itemBinding
        // 在目标页设置需要对应参与转场的共享元素
        transitionTargetManager?.setTransitionView(TestData.KEY_IMAGE, imageBinding.ivImage)
        transitionTargetManager?.setTransitionView(TestData.KEY_TITLE, itemBinding.tvTitle)
        transitionTargetManager?.setTransitionView(TestData.KEY_LIKE, itemBinding.ivLike)
    }

    private fun onItemClick(
        itemBinding: ItemLoopBinding,
        item: TestItem
    ) {
        // 在起始页跳转到目标页时：
        // 1、添加需要参与转场的共享元素并配置动画
        val fastTransitionViewManager = FastTransitionViewManager()
        fastTransitionViewManager.addView(
            TestData.KEY_IMAGE,
            itemBinding.ivImage,
            FastRoundedItem(FastRoundedValue(12f.dp)),//圆角动画
            FastSystemTransitionItem(FastSystemTransitionType.ChangeImageTransform)//图片切换动画
        )
        fastTransitionViewManager.addView(
            TestData.KEY_TITLE,
            itemBinding.tvTitle,
            FastTextViewItem(FastTextViewValue(itemBinding.tvTitle))//textview切换动画
        )
        fastTransitionViewManager.addView(
            TestData.KEY_LIKE,
            itemBinding.ivLike,
            FastToggleImageViewItem(FastToggleImageViewValue(itemBinding.ivLike))
        )
        // 2、通过startActivity启动目标页面
        fastTransitionViewManager.startActivity(
            activity = this@LoopAndDragExitActivity,
            targetIntent = LoopAndDragExitActivity.newIntent(this@LoopAndDragExitActivity, item),
            item.id
        )
    }

    // 可选：修复Q及以上系统，activity调用onStop后共享元素动画丢失的BUG
    override fun onStop() {
        transitionTargetManager?.onStop()
        super.onStop()
    }

    // 可选：修复Q及以上系统，3个及以上连续的activity拥有共享元素动画时，共享元素动画丢失的BUG（使用反射）
    override fun finishAfterTransition() {
        if (transitionTargetManager != null) {
            transitionTargetManager?.finishAfterTransition()
            headerItemBinding?.banner?.setCurrentItem(0, false)
            binding.rv.scrollToPosition(0)
            binding.rv.post {
                super.finishAfterTransition()
            }
        } else {
            // ps：如果当前页退出时没有共享元素动画，那么需要调用finish，以修复api32及以上，当前页在启动转场动画后，退出时会出现共享元素残影的问题
            finish()
        }
    }


    // 加载测试数据
    private fun loadTestData() {
        val data = arrayListOf<TestItem>()
        if (headerData != null) {
            data.add(headerData!!.apply { itemType = 1 })
        }
        data.addAll(TestData.simpleData)
        binding.rv.layoutManager = GridLayoutManager(this, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    if (data.getOrNull(position)?.itemType == 1) 2 else 1
            }
        }
        binding.rv.adapter = LoopAdapter(this, data, this::onHeaderLoadCompleted, this::onItemClick)
    }

    // 更新事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateEvent(event: TestItem) {
        (binding.rv.adapter as? LoopAdapter)?.apply {
            if (data.firstOrNull { it.id == event.id } == null) return
            data.forEachIndexed { index, item ->
                if (item.id == event.id) {
                    data[index] = event.copy().apply {
                        itemType = item.itemType
                    }
                    notifyItemChanged(index, "update")
                }
            }
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}
