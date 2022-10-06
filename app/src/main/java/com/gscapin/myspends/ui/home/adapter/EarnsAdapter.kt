package com.gscapin.myspends.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gscapin.myspends.core.BaseViewHolder
import com.gscapin.myspends.data.model.Earn
import com.gscapin.myspends.databinding.ItemBinding

class EarnsAdapter(
    private val earnList: List<Earn>,
    private val onEarnClickListener: OnEarnClickListener
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var earnClickListener: OnEarnClickListener? = null

    init {
        earnClickListener = onEarnClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EarnItemViewHolder(itemBinding, parent.context)
    }

    private inner class EarnItemViewHolder(val itemBinding: ItemBinding, val context: Context?) :
        BaseViewHolder<Earn>(itemBinding.root) {
        override fun bind(item: Earn) {
            fillItem(item)
            clickItem(item)
        }

        private fun clickItem(item: Earn) {
            itemBinding.item.setOnClickListener {
                earnClickListener?.onEarnBtnClick(item)
            }
        }

        private fun fillItem(item: Earn) {
            itemBinding.itemName.text = item.name
            itemBinding.itemType.text = item.type
            itemBinding.itemAmount.text = "$ ${item.amount}"
        }

    }


    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is EarnItemViewHolder -> holder.bind(earnList[position])
        }
    }

    override fun getItemCount(): Int = earnList.size
}
interface OnEarnClickListener {
    fun onEarnBtnClick(earn: Earn)
}