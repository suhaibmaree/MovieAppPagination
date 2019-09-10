package com.delaroystudios.paginationinfinitescroll.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HaveNetworks {

    private Context mContext;
    private boolean have_WIFI = false;
    private boolean have_MOBILEDATA = false;

    public HaveNetworks(Context mContext) {
         this.mContext =mContext;
    }//end have network


    public boolean haveNetwork() {

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

        return have_MOBILEDATA || have_WIFI;
    }//end have network




}
