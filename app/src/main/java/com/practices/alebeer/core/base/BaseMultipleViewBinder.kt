package com.practices.alebeer.core.base

import android.os.CountDownTimer
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import mva2.adapter.ItemBinder
import mva2.adapter.ItemViewHolder

abstract class BaseMultipleViewBinder<VB : ViewBinding, M : BaseModel> : ItemBinder<M, BaseMultipleViewBinder<VB, M>.BaseViewHolder>() {
    abstract fun inflateView(parent: ViewGroup?) : VB
    abstract fun onBindData(viewHolder: BaseViewHolder, item: M, viewBinding: VB)

    inner class BaseViewHolder(private val viewBinding: VB) : ItemViewHolder<M>(viewBinding.root) {
        var timer: CountDownTimer? = null

        fun onBind(item: M) {
            onBindData(this, item, viewBinding)
        }
    }

    override fun bindViewHolder(holder: BaseViewHolder?, item: M) {
        holder?.onBind(item)
    }

    override fun createViewHolder(parent: ViewGroup?): BaseViewHolder {
        return BaseViewHolder(inflateView(parent))
    }

    override fun canBindData(item: Any?): Boolean = item != null && item is BaseModel
}