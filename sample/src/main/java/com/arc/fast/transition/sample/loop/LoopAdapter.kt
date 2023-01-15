package com.arc.fast.transition.sample.loop

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arc.fast.transition.item.toggleimage.fastToggleImageviewSelectIcon
import com.arc.fast.transition.sample.R
import com.arc.fast.transition.sample.TestItem
import com.arc.fast.transition.sample.databinding.ItemLoopBinding
import com.arc.fast.transition.sample.databinding.ItemLoopHeaderBinding
import com.arc.fast.transition.sample.databinding.ItemLoopHeaderImageBinding
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.indicator.CircleIndicator
import org.greenrobot.eventbus.EventBus

class LoopAdapter(
    val lifecycleOwner: LifecycleOwner,
    data: MutableList<TestItem>,
    val onHeaderLoadCompleted: (itemBinding: ItemLoopHeaderBinding, imageBinding: ItemLoopHeaderImageBinding) -> Unit,
    val onItemClick: (itemBinding: ItemLoopBinding, item: TestItem) -> Unit
) :
    BaseMultiItemQuickAdapter<TestItem, BaseViewHolder>(data) {

    private var isHeaderLoadCompleted = false

    init {
        addItemType(0, R.layout.item_loop)
        addItemType(1, R.layout.item_loop_header)
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        super.onItemViewHolderCreated(viewHolder, viewType)
        if (viewType == 0) {
            ItemLoopBinding.bind(viewHolder.itemView)
        } else if (viewType == 1) {
            ItemLoopHeaderBinding.bind(viewHolder.itemView)
        }
    }

    override fun convert(holder: BaseViewHolder, item: TestItem, payloads: List<Any>) {
        super.convert(holder, item, payloads)
        payloads.forEach {
            if (it == "update") convert(holder, item)
        }
    }

    override fun convert(holder: BaseViewHolder, item: TestItem) {
        if (item.itemType == 0) {
            DataBindingUtil.getBinding<ItemLoopBinding>(holder.itemView)?.apply {
                ivImage.setImageResource(item.image)
                tvTitle.text = item.title
                root.setOnClickListener {
                    onItemClick.invoke(this, item)
                }
                ivLike.isSelected = item.isLike
                ivLike.fastToggleImageviewSelectIcon = R.drawable.ic_like
                ivLike.setOnClickListener {
                    EventBus.getDefault().post(
                        item.copy(
                            isLike = !item.isLike
                        )
                    )
                }
            }
        } else if (item.itemType == 1) {
            DataBindingUtil.getBinding<ItemLoopHeaderBinding>(holder.itemView)?.apply {
                if (banner.adapter == null) {
                    banner.addBannerLifecycleObserver(this@LoopAdapter.lifecycleOwner)
                    banner.indicator = CircleIndicator(context)
                    banner.setAdapter(
                        LoopImageAdapter(
                            listOf(
                                item.image,
                                R.mipmap.s1,
                                R.mipmap.s2,
                                R.mipmap.s3,
                                R.mipmap.s4,
                            )
                        ) {
                            if (!isHeaderLoadCompleted) {
                                isHeaderLoadCompleted = true
                                onHeaderLoadCompleted.invoke(this, it)
                            }
                        }
                    )
                }
                tvTitle.text = item.title
                tvContent.text = item.content
                ivLike.isSelected = item.isLike
                ivLike.fastToggleImageviewSelectIcon = R.drawable.ic_like2
                ivLike.setOnClickListener {
                    EventBus.getDefault().post(
                        item.copy(
                            isLike = !item.isLike
                        )
                    )
                }
            }
        }
    }

    override fun setDiffNewData(diffResult: DiffUtil.DiffResult, list: MutableList<TestItem>) {
        super.setDiffNewData(diffResult, list)
    }
}

class LoopImageAdapter(
    data: List<Int>,
    val onHeaderLoadCompleted: (itemBinding: ItemLoopHeaderImageBinding) -> Unit,
) : BannerAdapter<Int, LoopImageViewHolder>(data) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): LoopImageViewHolder {
        return LoopImageViewHolder(
            ItemLoopHeaderImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onHeaderLoadCompleted
        )
    }

    override fun onBindView(
        holder: LoopImageViewHolder,
        data: Int,
        position: Int,
        size: Int
    ) {
        holder.convert(data)
    }

}

class LoopImageViewHolder(
    val binding: ItemLoopHeaderImageBinding,
    val onHeaderLoadCompleted: (itemBinding: ItemLoopHeaderImageBinding) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {
    fun convert(item: Int) {
        binding.ivImage.setImageResource(item)
        onHeaderLoadCompleted.invoke(binding)
    }
}