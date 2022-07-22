package ru.surf.gallery

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController


        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView3)
        bottomNavView.setupWithNavController(navController)
        // TODO перенести в di
        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
            .setPopUpTo(navController.graph.startDestinationId, false)
            .build()

        bottomNavView.setOnItemSelectedListener { menuItem ->
            navController.popBackStack(R.id.mainFragment, false)
            navController.navigate(menuItem.itemId,null, options)
            true
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment -> bottomNavView.visibility = View.VISIBLE
                R.id.featuredFragment -> bottomNavView.visibility = View.VISIBLE
                R.id.fragmentProfile -> bottomNavView.visibility = View.VISIBLE
                else -> bottomNavView.visibility = View.GONE
            }
        }
    }
}