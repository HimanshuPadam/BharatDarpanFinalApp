package com.example.bhartiye_jangannah.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bhartiye_jangannah.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbRef= FirebaseDatabase.getInstance().getReference("Census Entry")
    }
}