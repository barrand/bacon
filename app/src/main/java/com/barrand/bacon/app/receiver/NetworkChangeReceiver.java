package com.barrand.bacon.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.barrand.bacon.app.model.Model;

/**
 * Created by bbarr233 on 4/5/14.
 */
public class NetworkChangeReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(final Context context, final Intent intent){
        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        SharedPreferences sp = context.getSharedPreferences(Model.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();

        if(networkInfo.getState() == NetworkInfo.State.CONNECTED){
            //check if the wifi is home or work
            String newlyConnectedSSID = networkInfo.getExtraInfo();
            String homeOrWorkConstant = isWifiStateHomeOrWork(newlyConnectedSSID, context);
            if(homeOrWorkConstant != null){
                //if it is home or work, turn on that we are listening for the next time we disconnect
                Toast.makeText(context, "connected to Home or Work " + newlyConnectedSSID, Toast.LENGTH_SHORT).show();
                editor.putString(Model.WAITING_FOR_DISCONNECT_FROM, homeOrWorkConstant);
                editor.commit();
            }
        }else if(networkInfo.getState() == NetworkInfo.State.DISCONNECTED){
            //if we were listening, then put an entry in the log, that we left home or work
            String waitingForDisconnect = sp.getString(Model.WAITING_FOR_DISCONNECT_FROM, null);

            if(waitingForDisconnect != null){
                //time to start a journey that we could possibly care about
                Toast.makeText(context, "disconnected from Home or Work ", Toast.LENGTH_SHORT).show();
            }
        }

        //
    }

    private String isWifiStateHomeOrWork(String ssid, Context context){
        SharedPreferences sp = context.getSharedPreferences(Model.PREFS_NAME, 0);
        String homeSsid = sp.getString(Model.HOME, "");
        String workSsid = sp.getString(Model.WORK, "");
        if(ssid.contains(homeSsid)){
            return Model.HOME;
        } else if (ssid.contains(workSsid)){
            return Model.WORK;
        }else{
            return null;
        }

    }
}
