package com.suret.moviesapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.suret.moviesapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

        navController = navHostFragment.navController

        activityMainBinding.apply {

            NavigationUI.setupWithNavController(bottomNav, navController)
            bottomNav.setOnNavigationItemReselectedListener { }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.movieDetailsFragment, R.id.fullCastFragment, R.id.personDetailsFragment -> hideBottomNav()
                else -> {
                    showBottomNav()
                }
            }
        }
    }

    private fun hideBottomNav() {
        activityMainBinding.apply {
            bottomNav.visibility = View.GONE
        }
    }

    private fun showBottomNav() {
        activityMainBinding.apply {
            bottomNav.visibility = View.VISIBLE
        }
    }
}