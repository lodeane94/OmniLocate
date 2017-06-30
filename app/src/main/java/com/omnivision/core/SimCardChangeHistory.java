package com.omnivision.core;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Created by Lodeane on 29/6/2017.
 */
@Entity
public class SimCardChangeHistory {
    @Id
    private long id;
    private long phoneId;
    @ToOne(joinProperty = "phoneId")
    private Phone phone;
    private String serialNumber;
    private String number;


    @Generated(hash = 1139277657)
    public SimCardChangeHistory(long id, long phoneId, String serialNumber,
            String number) {
        this.id = id;
        this.phoneId = phoneId;
        this.serialNumber = serialNumber;
        this.number = number;
    }

    public SimCardChangeHistory(long phoneId, String serialNumber, String number) {
        this.phoneId = phoneId;
        this.serialNumber = serialNumber;
        this.number = number;
    }

    @Generated(hash = 448772103)
    public SimCardChangeHistory() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(long phoneId) {
        this.phoneId = phoneId;
    }
}
