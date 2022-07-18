package com.example.furniture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.furniture.data.api.SessionManager
import com.example.furniture.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val locale = Locale("ar")
        val res = resources
        val conf = res.configuration
        conf.setLocale(locale)
        Locale.setDefault(locale)
        conf.setLayoutDirection(locale)
        createConfigurationContext(conf)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        sessionManager = SessionManager(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container_main) as NavHostFragment
        navController = navHostFragment.navController

        sessionManager.checkLogin(navController)

    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.loginFragment -> {
                if (doubleBackToExitPressedOnce) {
                    moveTaskToBack(true)
                } else
                    Toast.makeText(this, "Press again to exit login..", Toast.LENGTH_SHORT).show()

                doubleBackToExitPressedOnce = true
                Handler(Looper.getMainLooper()).postDelayed(
                    { doubleBackToExitPressedOnce = false },
                    2000
                )
            }
            R.id.homeFragment -> {
                if (doubleBackToExitPressedOnce) {
                    moveTaskToBack(true)
                } else
                    Toast.makeText(this, "Press again to exit login..", Toast.LENGTH_SHORT).show()

                doubleBackToExitPressedOnce = true
                Handler(Looper.getMainLooper()).postDelayed(
                    { doubleBackToExitPressedOnce = false },
                    2000
                )
            }
            else -> {
                super.onBackPressed()
            }
        }

    }

}