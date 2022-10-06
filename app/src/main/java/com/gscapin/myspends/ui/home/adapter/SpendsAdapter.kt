package com.gscapin.myspends.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gscapin.myspends.core.BaseViewHolder
import com.gscapin.myspends.data.model.Earn
import com.gscapin.myspends.data.model.Spend
import com.gscapin.myspends.databinding.ItemBinding

class SpendsAdapter(
    private val spendList: List<Spend>,
    private val onSpendClickListener: OnSpendClickListener
): RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var spendClickListener: OnSpendClickListener? = null

    init {
        spendClickListener = onSpendClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpendItemViewHolder(itemBinding, parent.context)
    }

    private inner class SpendItemViewHolder(val itemBinding: ItemBinding, val context: Context?) :
        BaseViewHolder<Spend>(itemBinding.root) {
        override fun bind(item: Spend) {
            fillItem(item)
            clickItem(item)
        }

        private fun clickItem(item: Spend) {
            itemBinding.item.setOnClickListener {
                spendClickListener?.onSpendBtnClick(item)
            }
        }

        private fun fillItem(item: Spend) {
            itemBinding.itemName.text = item.name
            itemBinding.itemType.text = item.type
            itemBinding.itemAmount.text = "$ ${item.amount}"
        }

    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is SpendItemViewHolder -> holder.bind(spendList[position])
        }
    }

    override fun getItemCount(): Int = spendList.size
}

interface OnSpendClickListener {
    fun onSpendBtnClick(spend: Spend)
}
