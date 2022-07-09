package com.example.android.bookstore.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.net.NetworkInterface

class ConnectionManager {
    fun CheckConnectivity(context: Context):Boolean{
        val ConnectivityManager= context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ActiveNetwork:NetworkInfo? =ConnectivityManager.activeNetworkInfo

        if(ActiveNetwork?.isConnected!=null){
            return ActiveNetwork.isConnected
        }
        else{
            return false
        }
    }

}