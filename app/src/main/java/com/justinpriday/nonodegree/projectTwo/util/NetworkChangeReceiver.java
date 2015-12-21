package com.justinpriday.nonodegree.projectTwo.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import java.util.logging.Handler;

/**
 * Created by justinpriday on 2015/12/15.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = NetworkChangeReceiver.class.getSimpleName();

    private static NetworkChangeNotification mChangeListener = null;

    public interface NetworkChangeNotification {
        void networkBecameAvailable();
    }

    public static void setNetworkListener(NetworkChangeNotification inListener) {
        mChangeListener = inListener;
    }

    public static void removeNetworkListener() {
        mChangeListener = null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG, "Broadcast Rx onchange");

        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            Log.v(LOG_TAG,"Network Available");
            //Need to pass this back to fragment to clear no network warnings.
            if (mChangeListener != null) {
                mChangeListener.networkBecameAvailable();
            }
        } else {
            Log.v(LOG_TAG,"Network Not Available");
        }
    }
}
