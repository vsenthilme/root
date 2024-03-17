package com.clara.clientportal.ui.home

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.clara.clientportal.BaseActivity
import com.clara.clientportal.R
import com.clara.clientportal.databinding.ActivityHomeBinding
import com.clara.clientportal.enums.HomeMenuEnum
import com.clara.clientportal.model.HomeMenu
import com.clara.clientportal.ui.checklist.CheckListActivity
import com.clara.clientportal.ui.documentupload.DocumentUploadActivity
import com.clara.clientportal.ui.home.adapter.MenuAdapter
import com.clara.clientportal.ui.invoice.InvoiceActivity
import com.clara.clientportal.ui.matter.MatterActivity
import com.clara.clientportal.ui.paymentplan.PaymentPlanActivity
import com.clara.clientportal.ui.quotation.InitialRetainerActivity
import com.clara.clientportal.ui.receiptno.ReceiptNoActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class HomeActivity : BaseActivity(), HomeFragment.HomeFragmentListener {

    private lateinit var binding: ActivityHomeBinding
    private var homeMenuList: MutableList<HomeMenu> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        homeMenuList = getHomeMenuList()
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.appBarMain.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        setFragment(HomeFragment())
        homeMenuList = homeMenuList.filter { it.classId != 1 }.toMutableList()
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        val menuAdapter = MenuAdapter(homeMenuList, true, ::onClickMenu)
        binding.menuList.layoutManager = linearLayoutManager
        binding.menuList.adapter = menuAdapter
        binding.textAppVersion.text = String.format(
            Locale.getDefault(),
            this.resources.getString(R.string.app_version),
            commonUtils.getAppVersion(this)
        )
        if (preferenceHelper.isTablet().not()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onClickMenu(menuItem: String) {
        when (menuItem) {
            HomeMenuEnum.MATTER.homeLandingMenu -> startActivity(
                Intent(
                    this,
                    MatterActivity::class.java
                )
            )

            HomeMenuEnum.INITIAL_RETAINER.homeLandingMenu -> startActivity(
                Intent(
                    this,
                    InitialRetainerActivity::class.java
                )
            )

            HomeMenuEnum.PAYMENT_PLAN.homeLandingMenu -> startActivity(
                Intent(
                    this,
                    PaymentPlanActivity::class.java
                )
            )

            HomeMenuEnum.INVOICE.homeLandingMenu -> startActivity(
                Intent(
                    this,
                    InvoiceActivity::class.java
                )
            )

            HomeMenuEnum.CHECKLIST.homeLandingMenu -> startActivity(
                Intent(
                    this,
                    CheckListActivity::class.java
                )
            )

            HomeMenuEnum.DOCUMENT_UPLOAD.homeLandingMenu -> startActivity(
                Intent(
                    this,
                    DocumentUploadActivity::class.java
                )
            )

            HomeMenuEnum.RECEIPT_NO.homeLandingMenu -> startActivity(
                Intent(
                    this,
                    ReceiptNoActivity::class.java
                )
            )

            HomeMenuEnum.LOGOUT.homeLandingMenu -> commonUtils.showLogout(this)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun getHomeMenuList(): MutableList<HomeMenu> {
        val menuList: MutableList<HomeMenu> = mutableListOf()
        val titleList = resources?.getStringArray(R.array.home_items)
        val iconList = resources?.getStringArray(R.array.icons)
        if (titleList.isNullOrEmpty().not()) {
            for ((position, title) in titleList?.withIndex()!!) {
                val homeMenu = HomeMenu(
                    title,
                    iconList?.get(position),
                    if (title == HomeMenuEnum.CHECKLIST.homeLandingMenu) preferenceHelper.getClassId() else 0
                )
                menuList.add(homeMenu)
            }
        }
        return menuList
    }

    override fun onBackPressed() {
        if (HomeFragment::class.java.name == if (getActiveFragment() != null) getActiveFragment()?.javaClass?.name else Fragment()) {
            finish()
        } else {
            setFragment(HomeFragment())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            commonUtils.showLogout(this)
            return true
        }
        return false
    }
}