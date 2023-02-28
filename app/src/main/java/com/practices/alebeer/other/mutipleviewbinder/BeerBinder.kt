package com.practices.alebeer.other.mutipleviewbinder

import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.practices.alebeer.R
import com.practices.alebeer.core.base.BaseMultipleViewBinder
import com.practices.alebeer.data.model.local.ItemBeerLocal
import com.practices.alebeer.databinding.ItemBeerBinding
import com.practices.alebeer.helper.DateHelper
import com.practices.alebeer.helper.constant.COUNTDOWN_INTERVAL
import java.util.Calendar

class BeerBinder(private val clickListener: ItemClickListener) : BaseMultipleViewBinder<ItemBeerBinding, ItemBeerLocal>() {

    override fun inflateView(parent: ViewGroup?): ItemBeerBinding {
        return ItemBeerBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
    }

    override fun onBindData(
        viewHolder: BaseViewHolder,
        item: ItemBeerLocal,
        viewBinding: ItemBeerBinding
    ) {
        viewBinding.run {
            tvName.text = item.name
            tvName.isSelected = true
            tvPrice.text = item.price
            tvPrice.isSelected = true
            etNote.setText(item.note)

            item.saleOffTime?.let {
                tvSaleOff.visibility = View.VISIBLE
                tvSaleOffLeft.visibility = View.VISIBLE

                val currentMillis = Calendar.getInstance().timeInMillis
                Log.d("XXX", "onCreateTimer - currentMillis=$currentMillis, saleOffTime=$it")

                if (currentMillis >= it) {
                    tvSaleOffLeft.visibility = View.GONE
                    tvSaleOff.text = tvSaleOff.context.getString(R.string.sale_off)
                    tvSaleOff.setTextColor(ResourcesCompat.getColor(tvSaleOff.resources, R.color.black, null))
                } else {
                    tvSaleOff.text = String.format(tvSaleOff.context.getString(R.string.sale_off_in), DateHelper.getSaleDateByMillis(it))
                    val saleMillis = it - currentMillis
                    tvSaleOff.setTextColor(ResourcesCompat.getColor(tvSaleOff.resources, R.color.red_hot, null))

                    if (viewHolder.timer != null) {
                        viewHolder.timer?.cancel()
                    }
                    viewHolder.timer = object : CountDownTimer(saleMillis, COUNTDOWN_INTERVAL) {
                        override fun onTick(p0: Long) {
                            tvSaleOffLeft.text = String.format(tvSaleOffLeft.context.getString(R.string.sale_off_left),
                                (p0 / COUNTDOWN_INTERVAL).toString())
                        }

                        override fun onFinish() {
                            tvSaleOffLeft.visibility = View.GONE
                        }
                    }
                    viewHolder.timer?.start()
                }
            } ?: kotlin.run {
                tvSaleOff.visibility = View.GONE
                tvSaleOffLeft.visibility = View.GONE
            }

            if (item.isFav) {
                // fav
                ivAva.run {
                    Glide.with(context).load(item.avatarPath)
                        .placeholder(R.drawable.ic_cloud_sync)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                        .error(R.drawable.ic_error)
                        .thumbnail(0.01f)
                        .into(this)
                }
                btnDelete.visibility = View.VISIBLE
                btnUpdateOrSave.visibility = View.VISIBLE
                btnUpdateOrSave.text = btnUpdateOrSave.context.getString(R.string.update)

            } else {
                // remote
                ivAva.run {
                    Glide.with(context).load(item.avatarUrl)
                        .placeholder(R.drawable.ic_cloud_sync)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                        .error(R.drawable.ic_error)
                        .thumbnail(0.01f)
                        .into(this)
                }
                btnDelete.visibility = View.GONE
                btnUpdateOrSave.visibility = View.VISIBLE
                btnUpdateOrSave.text = btnUpdateOrSave.context.getString(R.string.save)
                if (item.isFreezeNote) {
                    etNote.isEnabled = false
                    btnUpdateOrSave.visibility = View.GONE
                } else {
                    etNote.isEnabled = true
                    btnUpdateOrSave.visibility = View.VISIBLE
                }

                if (item.isSaving) {
                    btnUpdateOrSave.text = btnUpdateOrSave.context.getString(R.string.saving)
                    btnUpdateOrSave.isEnabled = false
                } else {
                    btnUpdateOrSave.isEnabled = true
                }
            }

            btnDelete.setOnClickListener {
                clickListener.onDeleteButtonClick(item)
            }

            btnUpdateOrSave.setOnClickListener {
                item.note = etNote.text.toString()
                etNote.clearFocus()
                if (btnUpdateOrSave.text == btnUpdateOrSave.context.getString(R.string.update)) {
                    clickListener.onUpdateButtonClick(item)
                } else {
                    clickListener.onSaveButtonClick(item)
                }
            }
        }
    }

    interface ItemClickListener {
        fun onDeleteButtonClick(item: ItemBeerLocal)
        fun onUpdateButtonClick(item: ItemBeerLocal)
        fun onSaveButtonClick(item: ItemBeerLocal)
    }
}