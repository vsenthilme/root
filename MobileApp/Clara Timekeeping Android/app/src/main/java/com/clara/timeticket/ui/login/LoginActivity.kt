package com.clara.timeticket.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import com.clara.timeticket.BaseActivity
import com.clara.timeticket.R
import com.clara.timeticket.databinding.ActivityLoginBinding
import com.clara.timeticket.ui.verifyotp.VerifyOtpActivity
import com.clara.timeticket.utils.Constants
import com.clara.timeticket.utils.setEnable
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    private val mViewModel: LoginViewModel by viewModels()
    private lateinit var frameAnimation: AnimationDrawable

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (preferenceHelper.isTablet().not()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        setListener()
        observeObservers()
    }

    private fun setListener() {
        with(binding) {
            btnLogin.setOnClickListener {
                initApiCall()
            }
            edtTxtPassword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    initApiCall()
                }
                false
            }
        }
    }

    private fun initApiCall() {
        if (isValid()) {
            mViewModel.userId = binding.edtTxtUserId.text.toString()
            mViewModel.password = binding.edtTxtPassword.text.toString()
            mViewModel.getAuthToken()
        }
    }

    private fun isValid(): Boolean {
        with(binding) {
            when {
                edtTxtUserId.text.toString().isEmpty() && edtTxtPassword.text.toString()
                    .isEmpty() -> {
                    commonUtils.showToastMessage(
                        this@LoginActivity,
                        this@LoginActivity.resources.getString(R.string.user_id_password_empty_message)
                    )
                    return false
                }

                edtTxtUserId.text.toString().isEmpty() -> {
                    commonUtils.showToastMessage(
                        this@LoginActivity,
                        this@LoginActivity.resources.getString(R.string.user_id_empty_message)
                    )
                    return false
                }

                edtTxtPassword.text.toString().isEmpty() -> {
                    commonUtils.showToastMessage(
                        this@LoginActivity,
                        this@LoginActivity.resources.getString(R.string.password_empty_message)
                    )
                    return false
                }

                else -> {
                    return true
                }
            }
        }
    }

    private fun observeObservers() {
        mViewModel.loginMutableLiveDat.observe(this) {
            it?.let { response ->
                commonUtils.showToastMessage(
                    this,
                    this.resources.getString(R.string.otp_success_message)
                )
                val intent = Intent(this, VerifyOtpActivity::class.java)
                intent.putExtra(Constants.EMAIL, response.emailId ?: "")
                intent.putExtra(Constants.USER_ID, response.userId ?: "")
                startActivity(intent)
            }
        }

        mViewModel.apiFailureMutableLiveData.observe(this) {
            commonUtils.showToastMessage(
                this,
                if (it.isNullOrEmpty()
                        .not()
                ) it else this.resources.getString(R.string.api_failure_message)
            )
        }
        mViewModel.loaderMutableLiveData.observe(this) {
            with(binding) {
                if (it) {
                    lytProgressParent.lytProgress.visibility = View.VISIBLE
                    lytProgressParent.lytProgress.setEnable(false)
                    lytProgressParent.imgProgress.setBackgroundResource(R.drawable.progress_frame_animation)
                    frameAnimation =
                        lytProgressParent.imgProgress.background as AnimationDrawable
                    commonUtils.enableDisableViews(lytLoginBottom, false)
                    frameAnimation.start()
                } else {
                    lytProgressParent.lytProgress.visibility = View.GONE
                    if (::frameAnimation.isInitialized && frameAnimation.isRunning) {
                        frameAnimation.stop()
                    }
                    commonUtils.enableDisableViews(lytLoginBottom, true)
                }
            }
        }
        mViewModel.networkMutableLiveDat.observe(this) {
            if (it) {
                commonUtils.showToastMessage(this, this.resources.getString(R.string.no_network))
            }
        }
    }
}