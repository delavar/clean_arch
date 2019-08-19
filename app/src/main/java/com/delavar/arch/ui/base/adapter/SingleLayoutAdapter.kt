package com.delavar.arch.ui.base.adapter

import androidx.databinding.ViewDataBinding


/**
 * Simplest implementation of [BaseAdapter] to use as a single layout adapter.
 */
open class SingleLayoutAdapter<T, B : ViewDataBinding>(
        private val layoutId: Int,
        itemBindingId: Int,
        items: List<T>,
        onBind: B.(Int) -> Unit = {}
) : BaseAdapter<T, B>(itemBindingId, items, onBind) {

    override fun getLayoutId(position: Int): Int = layoutId
}