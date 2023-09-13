package com.arc.fast.transition.sample.viewpager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.arc.fast.transition.FastTransitionTargetManager
import com.arc.fast.transition.sample.R
import com.arc.fast.transition.sample.TestData
import com.arc.fast.transition.sample.TestItem
import com.arc.fast.transition.sample.databinding.ActivityViewpagerBinding
import com.arc.fast.transition.sample.databinding.FragmentViewpagerBinding
import com.arc.fast.transition.sample.extension.applyFullScreen
import com.arc.fast.transition.sample.extension.setLightSystemBar

/**
 * ViewPager过渡示例
 * 目标页
 */
class ViewPagerActivity : AppCompatActivity() {

    lateinit var binding: ActivityViewpagerBinding

    // 获取目标页转场管理器
    private val transitionTargetManager by lazy {
        FastTransitionTargetManager.getManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyFullScreen()
        setLightSystemBar(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_viewpager)
        binding.toolbar.apply {
            this.navigationIcon = DrawerArrowDrawable(this@ViewPagerActivity).apply {
                progress = 1f
            }
            this.setNavigationOnClickListener {
                onBackPressed()
            }
        }
        loadTestData()
        // 在目标页应用转场动画
        transitionTargetManager?.applyTransitionEnterAndReturnConfig(
            postponeEnterTransition = true // 这里先暂停转场，等待页面准备好后再调用startTransitionEnter启动动画
        )
        binding.vp.doOnPreDraw {
            val fragment = binding.vp.findFragmentAtPosition(
                supportFragmentManager,
                ViewPagerDataSource.currentItem
            ) as? ViewPagerFragment
            if (fragment != null) {
                transitionTargetManager?.setTransitionView(
                    TestData.KEY_IMAGE,
                    fragment.binding.ivImage
                )
            }
            transitionTargetManager?.startTransitionEnter()
        }
    }

    private fun loadTestData() {
        val defaultItem = ViewPagerDataSource.currentItem
        val data = ViewPagerDataSource.data ?: return
        // 加载ViewPager
        binding.vp.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = data.size
            override fun createFragment(position: Int): Fragment =
                ViewPagerFragment(data.getOrNull(position))
        }
        // 用户切换Item时，更新共享动画元素到当前Item
        binding.vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                ViewPagerDataSource.currentItem = position
                // 在目标页设置需要对应参与转场的共享元素
                val fragment = binding.vp.findFragmentAtPosition(
                    supportFragmentManager,
                    position
                ) as? ViewPagerFragment
                if (fragment != null) {
                    // 用户切换Item时，更新共享动画元素到当前Item
                    transitionTargetManager?.setTransitionView(
                        TestData.KEY_IMAGE,
                        fragment.binding.ivImage
                    )
                }
                super.onPageSelected(position)
            }
        })
        binding.vp.setCurrentItem(defaultItem, false)
    }

    override fun onBackPressed() {
        // 返回上个页面前，需要先调用setResult，以便上个页面触发onActivityReenter回调，进行布局上的处理
        setResult(100)
        super.onBackPressed()
    }

    companion object {
        fun newIntent(
            context: Context,
            data: TestItem
        ): Intent {
            return Intent(context, ViewPagerActivity::class.java).apply {
                putExtra("data", data)
            }
        }

        fun start(
            context: Context,
            data: TestItem
        ) {
            context.startActivity(newIntent(context, data))
        }
    }
}

class ViewPagerFragment(val item: TestItem? = null) : Fragment() {

    lateinit var binding: FragmentViewpagerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewpagerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivImage.setImageResource(item?.image ?: return)
    }
}

object ViewPagerDataSource {
    var data: List<TestItem>? = null
    var currentItem: Int = -1
}

fun ViewPager2.findFragmentAtPosition(
    fragmentManager: FragmentManager,
    position: Int
): Fragment? {
    return fragmentManager.findFragmentByTag("f$position")
}