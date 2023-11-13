package com.kingelias.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.kingelias.noteapp.databinding.ActivityMainBinding
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar
        setSupportActionBar(binding.mainToolbar)

        // Initialize NavController and NavHostFragment
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        // Configure the AppBar with the homeFragment as the top-level destination
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.homeFragment
        ).build()

        // Set up the ActionBar with the NavController and AppBarConfiguration
        setSupportActionBar(binding.mainToolbar)
        setupActionBarWithNavController(
            navController,
            appBarConfiguration
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        // Handle Up button behavior, navigating up to the destination specified by appBarConfiguration
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}
