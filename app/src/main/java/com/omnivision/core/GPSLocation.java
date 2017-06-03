package com.omnivision.core;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lkelly on 4/2/2017.
 */

public class GPSLocation implements IGPSLocation{

    private String latitude;
    private String longitude;
    private String addressLine;
    private String googleMapsLink;

    public GPSLocation(String latitude,String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        setGoogleMapsLink();
    }

    @Override
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getLatitude() {
        return this.latitude;
    }

    @Override
    public String getLongitude() {
        return this.longitude;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getGoogleMapsLink() {
        return googleMapsLink;
    }

    private void setGoogleMapsLink() {
        if(this.latitude != null && this.longitude != null)
            this.googleMapsLink = "http://maps.google.com/maps?f=q&q="+latitude+","+longitude+"&z=16";
    }

    @Override
    public Boolean isGPSOn() {
        return false;
    }

    @Override
    public String toString(){
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        return "Address: " + this.addressLine + "\nLat: " + this.latitude + "\nLong: " + this.longitude + "\nT: "
               + timeStamp + "\n\nGo To Google Maps\n" + getGoogleMapsLink();
    }
}
