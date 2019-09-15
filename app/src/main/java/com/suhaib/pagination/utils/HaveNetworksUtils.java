package com.suhaib.pagination.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HaveNetworksUtils {


    public static boolean haveNetwork(Context mContext) {
        boolean have_WIFI = false;
        boolean have_MOBILEDATA = false;


        ConnectivityManager connectivityManager = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info : networkInfos) {

            if (info.getTypeName().equalsIgnoreCase("WIFI")) {
                if (info.isConnected()) {
                    have_WIFI = true;
                }
            }//end if

            if (info.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (info.isConnected()) {
                    have_MOBILEDATA = true;
                }
            }//end if

        }// end forLoop

        Log.d("HaveNetworksUtils", (have_MOBILEDATA || have_WIFI) + "");
        return have_MOBILEDATA || have_WIFI;
    }//end have network


}
