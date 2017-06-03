package com.omnivision.core;

import android.util.Log;

/**
 * Created by lkelly on 3/18/2017.
 */

public interface IPartnerDevice {
    void setId(Long Id);
    void setPartnerDeviceNum(String partnerDeviceNum);
    Long getId();
    String getPartnerDeviceNum();
}
