package com.example.furniture.data.api

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.furniture.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {

    private lateinit var activity: Activity

    fun setView(activity2: Activity) {
        activity = activity2
    }

    // Shared Preferences
    private val pref: SharedPreferences? = context.getSharedPreferences("fahmyabada@gmail.com", 0)

    // Editor for Shared preferences
    private val editor: SharedPreferences.Editor? = pref?.edit()

    fun putBoolean(key: String, value: Boolean) {
        editor?.putBoolean(key, value)?.apply()
    }

    fun getBoolean(key: String): Boolean? {
        return pref?.getBoolean(key, false)
    }

    fun putString(key: String, value: String) {
        editor?.putString(key, value)?.apply()
    }

    fun getString(key: String): String? {
        return pref?.getString(key, null)
    }

    fun putInt(key: String, value: Int) {
        editor?.putInt(key, value)?.apply()
    }

    fun getInt(key: String): Int? {
        return pref?.getInt(key, 0)
    }

    fun createLoginSession(token: String) {
        // Storing login value as TRUE
        editor?.putBoolean("IsLoggedIn", true)

        // Storing email in pref
        editor?.putString("token", token)

        editor?.apply()
    }

    fun checkLogin(navController: NavController) {
        if (!this.isLoggedIn()!!) {
            navController.navigate(R.id.loginFragment)
        } else {
            navController.navigate(R.id.homeFragment)
        }
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    fun isLoggedIn(): Boolean? {
        return pref?.getBoolean("IsLoggedIn", false)
    }


    fun logout2() {
        // Clearing all data from Shared Preferences
        editor?.clear()
        editor?.apply()
        activity.findNavController(R.id.nav_host_fragment_container_main).navigate(R.id.loginFragment)
    }
}