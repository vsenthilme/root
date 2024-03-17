package com.tvhht.myapplication.goodsreceipt.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.tvhht.myapplication.R
import com.tvhht.myapplication.databinding.DialogDocumentSelectedBinding
import com.tvhht.myapplication.goodsreceipt.GoodsReceiptSelectedActivity
import com.tvhht.myapplication.goodsreceipt.model.GoodsReceiptResponse


class DocumentNoSelectedDialog :
    DialogFragment {

    private lateinit var binding: DialogDocumentSelectedBinding
    private var ctx: Context? = null
    private var goodsReceiptResponse: GoodsReceiptResponse? = null

    constructor()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ReasonDialogStyle)
        val selectedDocument = arguments?.getString("Good_receipt")
        goodsReceiptResponse = Gson().fromJson(selectedDocument, GoodsReceiptResponse::class.java)
    }

    override fun onStart() {
        super.onStart()
        val dialogWidth =
            resources.getDimensionPixelSize(R.dimen.dp_350)
        dialog?.window?.setLayout(
            dialogWidth,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDocumentSelectedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            textMessage.text = ctx?.resources?.getString(R.string.order_no_selected)
            textTitle.text = goodsReceiptResponse?.refDocNumber ?: ""
            buttonNo.setOnClickListener {
                dismissAllowingStateLoss()
            }
            buttonYes.setOnClickListener {
                dismissAllowingStateLoss()
                val intent = Intent(ctx, GoodsReceiptSelectedActivity::class.java)
                intent.putExtra("CaseCode", goodsReceiptResponse?.caseCode ?: "")
                intent.putExtra("StagingNo", goodsReceiptResponse?.stagingNo ?: "")
                intent.putExtra("GOODS_RECEIPT_NO", goodsReceiptResponse?.goodsReceiptNo ?: "")
                startActivity(intent)
            }
        }
    }
}