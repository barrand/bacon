package com.barrand.bacon.app.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.barrand.bacon.app.R;
import com.barrand.bacon.app.model.Model;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by bbarr233 on 4/5/14.
 */
public class NetworkChangeReceiver extends BroadcastReceiver{

    public static int STATE_DISCONNECTED = 1;
    public static int STATE_DISCONNECTED_FROM_INTERESTED_TRIP_STARTED = 2;
    public static int STATE_CONNETED_TO_INTEREST = 3;
    public static int STATE_CONNECTED_TO_NON_INTEREST = 4;
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

                //if we connected to a network we are interested in, home or work
                if(sp.getInt(Model.CURRENT_STATE, 0) == STATE_DISCONNECTED_FROM_INTERESTED_TRIP_STARTED){
                    //make sure we connected to a unique network (not the one we were just on)
//                    if(!sp.getString(Model.LAST_NETWORK_OF_INTEREST,null).equals(homeOrWorkConstant)){
                        endTrip(context);
//                    }
                }
                editor.putString(Model.LAST_NETWORK_OF_INTEREST, homeOrWorkConstant);
                editor.putInt(Model.CURRENT_STATE, STATE_CONNETED_TO_INTEREST);
            }else{
                //save the state to non interest, but we don't do anything with it, since it doesn't matter
                editor.putInt(Model.CURRENT_STATE, STATE_CONNECTED_TO_NON_INTEREST);

            }
            editor.commit();

        }else if(networkInfo.getState() == NetworkInfo.State.DISCONNECTED){
            //if we were listening, then put an entry in the log, that we left home or work
            String waitingForDisconnect = sp.getString(Model.LAST_NETWORK_OF_INTEREST, null);

            if(waitingForDisconnect != null){
                //time to start a journey that we could possibly care about
                Toast.makeText(context, "disconnected from Home or Work ", Toast.LENGTH_SHORT).show();

                startTrip(context);

            }else{
                //if we just disconnected from some other network then set the state, and don't do anything since we don't care
                editor.putInt(Model.CURRENT_STATE, STATE_DISCONNECTED);

            }
            editor.commit();
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

    private void startTrip(Context context){
        SharedPreferences sp = context.getSharedPreferences(Model.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        //set our state that we started a trip since we just disconnected from a place of interest
        editor.putInt(Model.CURRENT_STATE, STATE_DISCONNECTED_FROM_INTERESTED_TRIP_STARTED);
        editor.putLong(Model.TRIP_START_TIME, System.currentTimeMillis());
        editor.commit();

        DateFormat.getTimeInstance();
        String startTime = DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis()));


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Trip Start")
                        .setContentText("Time: " + startTime);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(8888, mBuilder.build());

    }

    private void endTrip(Context context){
        SharedPreferences sp = context.getSharedPreferences(Model.PREFS_NAME, 0);
//        SharedPreferences.Editor editor = sp.edit();
        Long tripStartTime = sp.getLong(Model.TRIP_START_TIME, 0);
        Long now = System.currentTimeMillis();
        Long millis = now - tripStartTime;

        int minutes = (int) ((millis / 1000) / 60);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Trip End")
                        .setContentText("Duration: " + minutes);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(9999, mBuilder.build());
    }
}
