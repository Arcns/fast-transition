package com.arc.fast.transition.sample.loop

import android.annotation.SuppressLint
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.arc.fast.transition.item.toggleimage.fastToggleImageviewSelectIcon
import com.arc.fast.transition.sample.R
import com.arc.fast.transition.sample.TestItem
import com.arc.fast.transition.sample.databinding.ItemLoopBinding
import com.arc.fast.transition.sample.databinding.ItemLoopHeaderBinding
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import org.greenrobot.eventbus.EventBus

class LoopAdapter(
    data: MutableList<TestItem>,
    val onHeaderLoadCompleted: (itemBinding: ItemLoopHeaderBinding) -> Unit,
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
                ivImage.setImageResource(item.image)
                tvTitle.text = item.title
                tvContent.text = item.content
                if (!isHeaderLoadCompleted) {
                    isHeaderLoadCompleted = true
                    onHeaderLoadCompleted.invoke(this)
                }
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