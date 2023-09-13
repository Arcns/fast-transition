package com.arc.fast.transition.sample.viewpager

import androidx.databinding.DataBindingUtil
import com.arc.fast.transition.item.toggleimage.fastToggleImageviewSelectIcon
import com.arc.fast.transition.sample.R
import com.arc.fast.transition.sample.TestItem
import com.arc.fast.transition.sample.databinding.ItemLoopBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import org.greenrobot.eventbus.EventBus

class ListAdapter(
    data: MutableList<TestItem>,
    val onItemClick: (itemBinding: ItemLoopBinding, item: TestItem) -> Unit
) :
    BaseQuickAdapter<TestItem, BaseViewHolder>(R.layout.item_loop, data) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        super.onItemViewHolderCreated(viewHolder, viewType)
        ItemLoopBinding.bind(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: TestItem) {
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
    }
}
