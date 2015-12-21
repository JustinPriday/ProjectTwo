package com.justinpriday.nonodegree.projectTwo.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by justinpriday on 2015/12/15.
 */
public class DeviceUtils {
    public static boolean deviceOnline(Context context){
        ConnectivityManager connectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return(connectManager.getActiveNetworkInfo() != null && connectManager.getActiveNetworkInfo().isAvailable()
                && connectManager.getActiveNetworkInfo().isConnected());
    }
}
