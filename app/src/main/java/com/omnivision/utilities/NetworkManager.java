package com.omnivision.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.omnivision.core.Constants;

/**
 * Created by lkelly on 4/2/2017.
 */

public class NetworkManager {

    public static boolean hasNetworkConnection(Context context){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for(NetworkInfo ni : netInfo){
            if(ni.getTypeName().equalsIgnoreCase(Constants.NetworkConnectionType.WIFI.name())){
                if(ni.isConnected())
                    haveConnectedWifi = true;
            }
            if(ni.getTypeName().equalsIgnoreCase(Constants.NetworkConnectionType.MOBILE.name())){
                if(ni.isConnected())
                    haveConnectedMobile = true;
            }
        }
        return haveConnectedMobile || haveConnectedWifi;
    }
}
