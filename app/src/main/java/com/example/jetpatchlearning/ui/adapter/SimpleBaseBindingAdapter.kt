package com.example.jetpatchlearning.ui.adapter

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * 展示音乐列表的适配器
 */
public abstract class SimpleBaseBindingAdapter <M, B : ViewDataBinding>(protected val mContext: Context,
                                                                        @LayoutRes protected val layoutResId: Int) :
    BaseBindingAdapter<M, B>(mContext) {

    override fun getLayoutResId(viewType: Int): Int {
        return  layoutResId;
    }

    override fun onBindItem(binding: B?, item: M, holder: RecyclerView.ViewHolder) {
        onSimpleBindItem(binding!!, item, holder)
    }

    abstract fun onSimpleBindItem(binding: B, item: M, holder: RecyclerView.ViewHolder)
}