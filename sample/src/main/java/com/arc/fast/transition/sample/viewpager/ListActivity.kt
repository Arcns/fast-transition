package com.arc.fast.transition.sample.viewpager

import android.app.SharedElementCallback
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.arc.fast.core.extensions.dpToPx
import com.arc.fast.transition.FastRoundedItem
import com.arc.fast.transition.FastTransitionViewManager
import com.arc.fast.transition.item.rounded.FastRoundedValue
import com.arc.fast.transition.item.system.FastSystemTransitionItem
import com.arc.fast.transition.item.system.FastSystemTransitionType
import com.arc.fast.transition.sample.R
import com.arc.fast.transition.sample.TestData
import com.arc.fast.transition.sample.TestItem
import com.arc.fast.transition.sample.databinding.ActivityListBinding
import com.arc.fast.transition.sample.databinding.ItemLoopBinding
import com.arc.fast.transition.sample.extension.applyFullScreen
import com.arc.fast.transition.sample.extension.setLightSystemBar
import org.greenrobot.eventbus.EventBus

/**
 * ViewPager过渡示例
 * 起始页
 */
class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding

    // 获取目标页转场管理器
    private val fastTransitionViewManager by lazy { FastTransitionViewManager() }

    // 数据
    private var data: ArrayList<TestItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyFullScreen()
        setLightSystemBar(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list)
        binding.toolbar.apply {
            this.navigationIcon = DrawerArrowDrawable(this@ListActivity).apply {
                progress = 1f
            }
            this.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        // 加载测试数据
        loadTestData()
        // 由于在ViewPager页面我们可能浏览到了其他的Item，因此返回到当前页面时，我们需要把共享元素更新到对应的Item中
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                if (ViewPagerDataSource.currentItem == -1) return
                val selectedViewHolder =
                    binding.rv.findViewHolderForAdapterPosition(ViewPagerDataSource.currentItem) ?: return
                fastTransitionViewManager.changeExitSharedElements(
                    sharedElements,
                    TestData.KEY_IMAGE to selectedViewHolder.itemView.findViewById(R.id.ivImage)
                )
            }
        })
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        if (ViewPagerDataSource.currentItem == -1) return
        // 由于在ViewPager页面我们可能浏览到了其他的Item，因此返回到当前页面时，我们需要把共享元素更新到对应的Item中
        // 而在此之前，我们需要先把动画暂停，然后把列表移动到目标Item，再恢复动画，以便在setExitSharedElementCallback回调中把共享元素更新到对应的Item中
        postponeEnterTransition()
        binding.rv.layoutManager?.scrollToPosition(ViewPagerDataSource.currentItem)
        binding.rv.post {
            startPostponedEnterTransition()
        }
    }

    private fun onItemClick(
        itemBinding: ItemLoopBinding,
        item: TestItem
    ) {
        // 在起始页跳转到目标页时：
        // 1、添加需要参与转场的共享元素并配置动画
        fastTransitionViewManager.addView(
            TestData.KEY_IMAGE,
            itemBinding.ivImage,
            FastRoundedItem(FastRoundedValue(12f.dpToPx)),//圆角动画
            FastSystemTransitionItem(FastSystemTransitionType.ChangeImageTransform)//图片切换动画
        )
        // 2、通过startActivity启动目标页面
        ViewPagerDataSource.data = data
        ViewPagerDataSource.currentItem = data?.indexOf(item) ?: 0
        fastTransitionViewManager.startActivity(
            activity = this@ListActivity,
            targetIntent = ViewPagerActivity.newIntent(this@ListActivity, item),
            item.id
        )
    }

    // 加载测试数据
    private fun loadTestData() {
        this.data = TestData.simpleData
        binding.rv.layoutManager = GridLayoutManager(this, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    if (data?.getOrNull(position)?.itemType == 1) 2 else 1
            }
        }
        binding.rv.adapter = ListAdapter(data ?: return, this::onItemClick)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }


    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ListActivity::class.java)
        }

        fun start(context: Context) {
            context.startActivity(newIntent(context))
        }
    }
}