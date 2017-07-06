package com.omnivision.core;

import java.util.List;

/**
 * Created by lkelly on 3/7/2017.
 */

public interface IPhone {
    void setId(Long id);
    void setUserId(Long userId);
    void setCellNum(String cellNumber);
    void setCountry(String country);
    void setDeviceStatus(String status);
    void setPartnerDevices(List<PartnerDevice> partnerDevicesNums);
    Long getId();
    Long getUserId();
    String getCountry();
    String getCellNum();
    String getDeviceStatus();
    Boolean validateCellNum();
}
