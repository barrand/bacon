package com.barrand.bacon.app.model;

/**
 * Created by bbarr233 on 4/25/14.
 */
public class Trip {
    public long startTime;
    public long arriveTime;
    public long durationTime;
    public long sqliteId;

    private int _minutes = -1;
    public int getMinutes(){
        if(_minutes == -1){
            _minutes = (int) ((durationTime / 1000) / 60);
        }
        return _minutes;
    }

    public String toString(){
        return "duration time " + durationTime;
    }
}
