package com.drewapps.ai.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.drewapps.ai.Config;

public class Connectivity {

    Context context;

    public Connectivity(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    private NetworkInfo getNetworkInfo(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm != null) {
            return cm.getActiveNetworkInfo();
        }else {
            return null;
        }
    }
    public boolean isConnected(){
        NetworkInfo info = getNetworkInfo();
        Config.IS_CONNECTED = info != null && info.isConnected() && (isConnectedMobile() || isConnectedWifi());
        return (info != null && info.isConnected() && (isConnectedMobile() || isConnectedWifi()));
    }

    public boolean isConnectedWifi(){
        NetworkInfo info = getNetworkInfo();
//        Config.IS_CONNECTED = info != null && info.isConnected() && (isConnectedMobile() || isConnectedWifi());
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public boolean isConnectedMobile(){
        NetworkInfo info = getNetworkInfo();
//        Config.IS_CONNECTED = (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

}