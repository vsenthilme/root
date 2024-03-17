package com.clara.timeticket

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.clara.timeticket.utils.CommonUtils
import com.clara.timeticket.utils.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var commonUtils: CommonUtils

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    open fun setToolBar(toolbar: Toolbar?) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
    }
}