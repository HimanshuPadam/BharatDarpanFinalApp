package com.example.bhartiye_jangannah.activities.homeActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.databinding.ActivityDeleteEntryBinding

class DeleteEntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteEntryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDeleteEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}