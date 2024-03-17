package com.clara.clientportal.ui.splash

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.viewModels
import com.clara.clientportal.BaseActivity
import com.clara.clientportal.R
import com.clara.clientportal.databinding.ActivitySplashBinding
import com.clara.clientportal.ui.home.HomeActivity
import com.clara.clientportal.ui.signin.SignInActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lytSplashParent.tag?.let {
            preferenceHelper.setTablet(commonUtils.isTablet(it.toString()))
        }
        if (preferenceHelper.isTablet().not()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        viewModel.statusMutableLiveData.observe(this) {
            if (it.isNullOrEmpty().not()) {
                preferenceHelper.setStatus(Gson().toJson(it))
            }

            if (preferenceHelper.getClientDetails().isNotEmpty()) {
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
            finish()
        }
        viewModel.networkMutableLiveDat.observe(this) {
            if (it) {
                commonUtils.showToastMessage(this, this.resources.getString(R.string.no_network))
            }
        }
    }
}