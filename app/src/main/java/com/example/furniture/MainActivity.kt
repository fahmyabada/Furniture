package com.example.furniture

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.furniture.data.api.SessionManager
import com.example.furniture.databinding.ActivityMainBinding
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce: Boolean = false
    private lateinit var mAuth: FirebaseAuth

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

        mAuth = FirebaseAuth.getInstance()
        sessionManager = SessionManager(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container_main) as NavHostFragment
        navController = navHostFragment.navController

        if (sessionManager.getString("typeSignIn")!=null) {
            if (sessionManager.getString("typeSignIn") == "facebook") {
                val accessToken = AccessToken.getCurrentAccessToken()
                val isLoggedIn = accessToken != null && !accessToken.isExpired
                if (isLoggedIn) {
                    navController.navigate(R.id.homeFragment)
                } else {
                    navController.navigate(R.id.loginFragment)
                }
            } else if (sessionManager.getString("typeSignIn") == "google") {
                if (mAuth.currentUser != null) {
                    navController.navigate(R.id.homeFragment)
                } else {
                    navController.navigate(R.id.loginFragment)
                }
            }else if (sessionManager.getString("typeSignIn") == "normal") {
                sessionManager.checkLogin(navController)
            }
        }else{
            navController.navigate(R.id.loginFragment)
        }


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