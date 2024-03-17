package com.tvhht.myapplication.goodsreceipt

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.tvhht.myapplication.R
import com.tvhht.myapplication.databinding.ActivityGoodsReceiptListBinding
import com.tvhht.myapplication.goodsreceipt.adapter.GoodsReceiptAdapter
import com.tvhht.myapplication.goodsreceipt.utils.DocumentNoSelectedDialog
import com.tvhht.myapplication.goodsreceipt.viewmodel.GoodsReceiptViewModel
import com.tvhht.myapplication.home.HomePageActivity
import com.tvhht.myapplication.login.LoginLiveData
import com.tvhht.myapplication.login.model.LoginModel
import com.tvhht.myapplication.utils.ApiConstant
import com.tvhht.myapplication.utils.DateUtil
import com.tvhht.myapplication.utils.LogoutFragment
import com.tvhht.myapplication.utils.NetworkUtils
import com.tvhht.myapplication.utils.PrefConstant
import com.tvhht.myapplication.utils.ToastUtils
import com.tvhht.myapplication.utils.WMSApplication
import com.tvhht.myapplication.utils.WMSSharedPref
import kotlinx.android.synthetic.main.activity_quality_details.progress
import kotlinx.android.synthetic.main.tool_bar.toolbar
import kotlinx.android.synthetic.main.tool_bar.view.sign_out

class GoodsReceiptListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGoodsReceiptListBinding
    private lateinit var viewModel: GoodsReceiptViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoodsReceiptListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        instances = this
        initViewModel()
        val loginUserId = WMSSharedPref.getAppPrefs(
            WMSApplication.getInstance().context
        ).getStringValue(PrefConstant.LOGIN_ID)
        binding.grUser.text = loginUserId

        if (!NetworkUtils().haveNetworkConnection(applicationContext)) {
            Handler().postDelayed({
                progress.visibility = View.GONE
                val parentLayout = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(
                    parentLayout, getString(R.string.internet_check_msg),
                    Snackbar.LENGTH_INDEFINITE
                )

                snackbar.setAction("Dismiss") {
                    snackbar.dismiss()
                    HomePageActivity.getInstance()?.reload()
                }
                snackbar.setActionTextColor(Color.BLUE)
                val snackbarView = snackbar.view
                snackbarView.setBackgroundColor(Color.LTGRAY)
                val textView =
                    snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.RED)
                textView.textSize = 16f
                snackbar.show()
            }, 1000)
        } else {
            verifyToken()
        }

        toolbar.sign_out.setOnClickListener {
            if (supportFragmentManager.findFragmentByTag("logout_fragment") == null) {
                val logoutFragment = LogoutFragment()
                supportFragmentManager.beginTransaction().add(logoutFragment, "logout_fragment")
                    .commitAllowingStateLoss()
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[GoodsReceiptViewModel::class.java]
    }

    private fun verifyToken() {
        binding.progress.visibility = View.VISIBLE
        val request = LoginModel(
            ApiConstant.apiName_transaction,
            ApiConstant.clientId,
            ApiConstant.clientSecretKey,
            ApiConstant.grantType,
            ApiConstant.apiName_name,
            ApiConstant.apiName_pass_key
        )
        val model = ViewModelProvider(this)[LoginLiveData::class.java]
        model.getLoginStatus(request).observe(this) { asnList ->
            // update UI
            if (asnList.equals("ERROR")) {
                ToastUtils.showToast(applicationContext, "User not Available")
            } else {
                if (asnList.equals("FAILED")) {
                    ToastUtils.showToast(applicationContext, "No Data Available")
                } else {
                    getGoodsReceiptList()
                }
            }
        }
    }

    private fun getGoodsReceiptList() {
        viewModel.getGoodsReceiptList().observe(this) { goodsList ->
            if (goodsList.isNullOrEmpty().not()) {
                with(binding) {
                    val sortedList = goodsList.sortedByDescending { date ->
                        date.createdOn?.let { it1 ->
                            DateUtil.getDateYYYYMMDD(it1)
                        }
                    }
                    val layoutManager = LinearLayoutManager(this@GoodsReceiptListActivity)
                    goodsReceiptList.layoutManager = layoutManager
                    goodsReceiptList.addItemDecoration(
                        DividerItemDecoration(
                            this@GoodsReceiptListActivity,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    val adapter = GoodsReceiptAdapter(sortedList) { selectedDocument ->
                        if (supportFragmentManager.findFragmentByTag("document_no_selected_fragment") == null) {
                            val documentNoSelectedDialog = DocumentNoSelectedDialog()
                            val bundle = Bundle()
                            bundle.putString("Good_receipt", Gson().toJson(selectedDocument))
                            documentNoSelectedDialog.arguments = bundle
                            supportFragmentManager.beginTransaction()
                                .add(documentNoSelectedDialog, "document_no_selected_fragment")
                                .commitAllowingStateLoss()
                        }
                    }
                    goodsReceiptList.adapter = adapter
                    grCount.text = goodsList.size.toString()
                }
            }
            binding.progress.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        HomePageActivity.getInstance()?.reload()
        super.onBackPressed()
    }

    fun reload() {
        val intent = intent
        overridePendingTransition(0, 0)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
    }

    companion object {
        private var instances: GoodsReceiptListActivity? = null
        fun getInstance(): GoodsReceiptListActivity? {
            return instances
        }
    }
}