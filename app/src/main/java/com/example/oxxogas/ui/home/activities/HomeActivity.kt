package com.example.oxxogas.ui.home.activities

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import com.example.oxxogas.R
import com.example.oxxogas.databinding.ActivityHomeBinding
import com.example.oxxogas.ui.main.activities.BaseActivity
import com.example.oxxogas.ui.main.utils.Profile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private var navHostFragment: NavHostFragment? = null

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navHostFragment?.navController?.popBackStack() == false) {
                    finish()
                }
            }

        }

    override fun initBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)

    override fun initView(saveInstanceState: Bundle?) {
        Profile().createDataWedgeProfile(this)
        Profile().enableIntentOutput(this)
        setNavigation()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setNavigation() {
        navHostFragment = supportFragmentManager.findFragmentById(
            R.id.container_fragment
        ) as? NavHostFragment
        val inflater = navHostFragment?.navController?.navInflater
        val graph = inflater?.inflate(R.navigation.home_nav_graph)
        if (navHostFragment != null && graph != null) {
            graph.setStartDestination(R.id.menu)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}