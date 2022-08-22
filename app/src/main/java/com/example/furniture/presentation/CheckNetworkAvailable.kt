package com.example.furniture.presentation

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class CheckNetworkAvailable(private val app: Application) {

    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                // you can this code for check if network connect with internet or not before send any request
                /*  if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                ) {
                    try {
                        val urlc: HttpURLConnection = URL("https://google.com/")
                            .openConnection() as HttpURLConnection
                        urlc.setRequestProperty("User-Agent", "Android")
                        urlc.setRequestProperty("Connection", "close")
                        urlc.connectTimeout = 1500
                        urlc.connect()
                        if (urlc.responseCode == 200) {
                            return true
                        }
                    } catch (e: IOException) {
                        Log.e("INTERNET", "Error checking internet connection")
                        return false
                    }

                }*/

                // instead of code in top
                // you can use IOException in try and catch if you want check network in viewModel
                when {
                     capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                         Log.i("NetworkCapabilities", "********* TRANSPORT_CELLULAR")
                         return true
                     }
                     capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                         Log.i("NetworkCapabilities", "********* TRANSPORT_WIFI")
                         return true
                     }
                     capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                         Log.i("NetworkCapabilities", "********* TRANSPORT_ETHERNET")
                         return true
                     }
                 }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                // you can this code for check if network connect with internet or not before send any request
                /* try {
                     val urlc: HttpURLConnection = URL("https://google.com/")
                         .openConnection() as HttpURLConnection
                     urlc.setRequestProperty("User-Agent", "Android")
                     urlc.setRequestProperty("Connection", "close")
                     urlc.connectTimeout = 1500
                     urlc.connect()
                     if (urlc.responseCode == 200) {
                         return true
                     }
                 } catch (e: IOException) {
                     Log.e("INTERNET", "Error checking internet connection")
                     return false
                 }*/
                return true
            }
        }
        return false

    }
}