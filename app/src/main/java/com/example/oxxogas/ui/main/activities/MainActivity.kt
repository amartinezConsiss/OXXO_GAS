package com.example.oxxogas.ui.main.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.example.oxxogas.R
import com.example.oxxogas.databinding.ActivityMainBinding
import com.example.oxxogas.ui.home.activities.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun initBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun initView(saveInstanceState: Bundle?) {
        val logo = findViewById<ImageView>(R.id.iv_splash_icon)

        logo.animate()
            .translationY(0f)
            .setDuration(2500)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
            .start()
    }
}