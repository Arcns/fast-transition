package com.arc.fast.transition.sample.loop

import androidx.databinding.DataBindingUtil
import com.arc.fast.transition.sample.R
import com.arc.fast.transition.sample.TestItem
import com.arc.fast.transition.sample.databinding.ItemLoopBinding
import com.arc.fast.transition.sample.databinding.ItemLoopHeaderBinding
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

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

    override fun convert(holder: BaseViewHolder, item: TestItem) {
        if (item.itemType == 0) {
            DataBindingUtil.getBinding<ItemLoopBinding>(holder.itemView)?.apply {
                ivImage.setImageResource(item.image)
                tvTitle.text = item.title
                root.setOnClickListener {
                    onItemClick.invoke(this, item)
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
            }
        }
    }
}