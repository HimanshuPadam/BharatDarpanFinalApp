package com.example.bhartiye_jangannah.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.bhartiye_jangannah.activities.MainActivity
import com.example.bhartiye_jangannah.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewLogo.animate().alpha(1f).setDuration(1500).start()
        binding.appIcon.animate().alpha(1f).setDuration(1500).setStartDelay(500).start()
        binding.tvSplashDescription.animate().alpha(1f).setDuration(1500).setStartDelay(1000).start()

        Handler().postDelayed({
            finishWithFadeOut()
        }, 3500)
    }
    private fun finishWithFadeOut() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}