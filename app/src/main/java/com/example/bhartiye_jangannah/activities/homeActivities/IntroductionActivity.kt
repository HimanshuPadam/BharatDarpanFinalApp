package com.example.bhartiye_jangannah.activities.homeActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.databinding.ActivityIntroductionBinding

class IntroductionActivity : AppCompatActivity() {
    private lateinit var  activityIntroductionBinding: ActivityIntroductionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityIntroductionBinding= ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(activityIntroductionBinding.root)
        val webSettings: WebSettings = activityIntroductionBinding.webView.settings
        webSettings.javaScriptEnabled = true
        activityIntroductionBinding.webView.loadUrl("https://censusindia.gov.in/census.website/")
    }
    override fun onBackPressed() {
        if (activityIntroductionBinding.webView.canGoBack()) {
            activityIntroductionBinding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}