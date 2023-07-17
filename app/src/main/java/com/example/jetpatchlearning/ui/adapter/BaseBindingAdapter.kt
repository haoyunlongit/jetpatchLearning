package com.example.jetpatchlearning.ui.adapter

import androidx.recyclerview.widget.RecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingAdapter<M, B : ViewDataBinding>(
    protected val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var list: MutableList<M> = mutableListOf()

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: B = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            getLayoutResId(viewType),
            parent,
            false
        )
        return BaseBindingViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding: B? = DataBindingUtil.getBinding(holder.itemView)
        onBindItem(binding, list[position], holder)
        binding?.executePendingBindings()
    }

    @LayoutRes
    protected abstract fun getLayoutResId(viewType: Int): Int

    /**
     * 注意：
     * RecyclerView 中的数据有位置改变（比如删除）时一般不会重新调用 onBindViewHolder() 方法，除非这个元素不可用。
     * 为了实时获取元素的位置，我们通过 ViewHolder.getAdapterPosition() 方法。
     *
     * @param binding .
     * @param item    .
     * @param holder  .
     */
    protected abstract fun onBindItem(binding: B?, item: M, holder: RecyclerView.ViewHolder)

    inner class BaseBindingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
