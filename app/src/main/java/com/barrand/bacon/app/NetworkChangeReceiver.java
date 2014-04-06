package com.barrand.bacon.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * Created by bbarr233 on 4/5/14.
 */
public class NetworkChangeReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(final Context context, final Intent intent){
        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(networkInfo.getState() == NetworkInfo.State.CONNECTED){
            //check if the wifi is home or work
            String newlyConnectedSSID = networkInfo.getExtraInfo();
            if(isWifiStateHomeOrWork(newlyConnectedSSID, context)){
                //if it is home or work, turn on that we are listening for the next time we disconnect
                Toast.makeText(context, "connected to Home or Work " + newlyConnectedSSID, Toast.LENGTH_SHORT).show();
            }
        }else if(networkInfo.getState() == NetworkInfo.State.DISCONNECTED){
            //if we were listening, then put an entry in the log, that we left home or work
//            if()
//                Toast.makeText(context, "disconnected from Home or Work ", Toast.LENGTH_SHORT).show();
        }

        //
    }

    private boolean isWifiStateHomeOrWork(String ssid, Context context){
        SharedPreferences sp = context.getSharedPreferences(Model.PREFS_NAME, 0);
        String homeSsid = sp.getString(Model.HOME, "");
        String workSsid = sp.getString(Model.WORK, "");
        if(ssid.contains(homeSsid) || ssid.contains(workSsid)){
            return true;
        }else{
            return false;
        }

    }
}
