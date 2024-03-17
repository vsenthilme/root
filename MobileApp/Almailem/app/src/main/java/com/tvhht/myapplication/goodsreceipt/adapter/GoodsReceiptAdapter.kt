package com.tvhht.myapplication.goodsreceipt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tvhht.myapplication.databinding.AdapterGoodsReceiptBinding
import com.tvhht.myapplication.goodsreceipt.model.GoodsReceiptResponse
import com.tvhht.myapplication.utils.DateUtil

class GoodsReceiptAdapter(private val goodsReceiptList: List<GoodsReceiptResponse>,private val onDocumentSelected:(receipt:GoodsReceiptResponse)->Unit) :
    RecyclerView.Adapter<GoodsReceiptAdapter.GoodsReceiptViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsReceiptViewModel {
        val binding: AdapterGoodsReceiptBinding =
            AdapterGoodsReceiptBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoodsReceiptViewModel(binding)
    }

    override fun getItemCount(): Int {
        return goodsReceiptList.size
    }

    override fun onBindViewHolder(holder: GoodsReceiptViewModel, position: Int) {
        holder.bindView()
    }

    inner class GoodsReceiptViewModel(val binding: AdapterGoodsReceiptBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView() {
            val goodsReceipt = goodsReceiptList[adapterPosition]
            with(binding) {
                with(goodsReceipt) {
                    txtCell1.text = plantId ?: ""
                    txtCell2.text = refDocNumber ?: ""
                    txtCell3.text = DateUtil.getDateYYYYMMDD(createdOn ?: "")
                    txtCell4.setOnClickListener {
                        onDocumentSelected.invoke(this)
                        txtCell4.isChecked = false
                    }
                }
            }
        }
    }
}