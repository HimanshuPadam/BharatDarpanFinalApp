package com.example.bhartiye_jangannah.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.bhartiye_jangannah.R
import com.example.bhartiye_jangannah.databinding.ActivityHomePageBinding

class HomePageActivity : AppCompatActivity() {

    private lateinit var activityHomePageBinding: ActivityHomePageBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        activityHomePageBinding=ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(activityHomePageBinding.root)
        navController = findNavController(R.id.frag)
        activityHomePageBinding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    navController.navigate(R.id.homeFragment)
                }
                R.id.add -> {
                    navController.navigate(R.id.newEntryFragment)
                }
                R.id.profile -> {
                    navController.navigate(R.id.profileFragment)
                }
            }
            return@setOnItemSelectedListener true
        }
        navController.addOnDestinationChangedListener{_, destination, arguments ->
            when(destination.id){
                R.id.homeFragment-> activityHomePageBinding.bottomNav.menu.get(0).isChecked = true
                R.id.newEntryFragment-> activityHomePageBinding.bottomNav.menu.get(1).isChecked = true
                R.id.profileFragment-> activityHomePageBinding.bottomNav.menu.get(2).isChecked = true
            }
        }
    }
}