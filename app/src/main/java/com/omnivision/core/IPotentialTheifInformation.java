package com.omnivision.core;

import android.graphics.Bitmap;

/**
 * Created by lkelly on 3/7/2017.
 */

public interface IPotentialTheifInformation {
    void addImageToCatalog(String imeiNum, Bitmap image);
    void setCoordinates(IGPSLocation coordinates);
    void setNoOfIncorrectPassEntries(int noOfIncorrectPassEntries);
    IGPSLocation getCoordinates();
    int getNoOfIncorrectPassEntries();
}
