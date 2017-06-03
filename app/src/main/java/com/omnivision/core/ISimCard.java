package com.omnivision.core;

import java.math.BigDecimal;

/**
 * Created by Lodeane on 1/6/2017.
 */

public interface ISimCard {
    void setId(long id);
    long getId();
    void setPhoneId(long phoneId);
    long getPhoneId();
    void setNumber(String number);
    String getNumber();
    void setAreaCode(String areaCode);
    String getAreaCode();
    void setPhone(Phone phone);
    Phone getPhone(Phone phone);
    void setSmsCost(float smsCost);
    float getSmsCost();
    void setSimNetworkId(Long simNetworkId);
    Long getSimNetworkId();
}
