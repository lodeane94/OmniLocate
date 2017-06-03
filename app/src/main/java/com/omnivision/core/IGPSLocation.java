package com.omnivision.core;

/**
 * Created by lkelly on 3/7/2017.
 */

public interface IGPSLocation {
    void setLatitude(String latitude);
    void setLongitude(String longitude);
    String getLatitude();
    String getLongitude();
    Boolean isGPSOn();
}
