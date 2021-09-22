package com.suret.moviesapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.suret.moviesapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem

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

            val menuItems = arrayOf(
                CbnMenuItem(
                    R.drawable.ic_round_home_24, // the icon
                    R.drawable.avd_home, // the AVD that will be shown in FAB
                    R.id.movies // optional if you use Jetpack Navigation
                ),
                CbnMenuItem(
                    R.drawable.favorite,
                    R.drawable.avd_fav,
                    R.id.favourite
                )
            )
            bottomNav.setMenuItems(menuItems, 0)
            bottomNav.setupWithNavController(navController)
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.movieDetailsFragment, R.id.fullCastFragment, R.id.personDetailsFragment, R.id.similarFragment, R.id.reviewBottomSheet -> hideBottomNav()

                R.id.movies -> {
                    activityMainBinding.constrainLayout.apply {
                        setBackgroundColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.background_color
                            )
                        )
                    }
                    showBottomNav()
                }
                R.id.favourite -> {
                    activityMainBinding.constrainLayout.apply {
                        setBackgroundColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.martinique
                            )
                        )
                    }
                    showBottomNav()
                }
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