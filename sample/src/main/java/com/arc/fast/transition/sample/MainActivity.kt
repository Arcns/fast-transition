package com.arc.fast.transition.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.databinding.DataBindingUtil
import com.arc.fast.transition.sample.databinding.ActivityMainBinding
import com.arc.fast.transition.sample.extension.applyFullScreen
import com.arc.fast.transition.sample.extension.setLightSystemBar
import com.arc.fast.transition.sample.loop.LoopActivity
import com.arc.fast.transition.sample.loopanddragexit.LoopAndDragExitActivity
import com.arc.fast.transition.sample.simple.SimpleActivity

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyFullScreen()
        setLightSystemBar(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.toolbar.apply {
            this.navigationIcon = DrawerArrowDrawable(this@MainActivity).apply {
                progress = 1f
            }
            this.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        binding.btnSimple.setOnClickListener {
            SimpleActivity.start(this)
        }
        binding.btnLoop.setOnClickListener {
            LoopActivity.start(this)
        }
        binding.btnLoopAndDragExit.setOnClickListener {
            LoopAndDragExitActivity.start(this)
        }
    }

}