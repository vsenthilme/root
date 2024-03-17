package com.clara.timeticket.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.clara.timeticket.BaseActivity
import com.clara.timeticket.databinding.ActivitySplashBinding
import com.clara.timeticket.ui.login.LoginActivity
import com.clara.timeticket.ui.search.SearchActivity
import com.clara.timeticket.ui.summary.TimeTicketSummaryActivity
import com.clara.timeticket.ui.verifyotp.VerifyOtpActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()
    @SuppressLint("SourceLockedOrientationActivity")
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
        Handler(Looper.getMainLooper()).postDelayed({
            if (preferenceHelper.getLoginDetails().isNotEmpty()) {
                startActivity(Intent(this, TimeTicketSummaryActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        },2000)
    }
}