package com.example.andylin.homepackagemonitor.Model;

/**
 * Created by Andy Lin on 2017-04-03.
 */

public class BoxLocationMarker {
    private String deviceID;
    private String latitude;
    private String longitude;
    private boolean isPending;

    public BoxLocationMarker(String deviceID, String latitude, String longitude, boolean isPending){
        this.deviceID = deviceID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isPending = isPending;
    }

    public String getDeviceID(){
        return this.deviceID;
    }

    public String getLatitude(){
        return this.latitude;
    }

    public String getLongitude(){
        return this.longitude;
    }

    public boolean getIsPending(){
        return this.isPending;
    }
}
