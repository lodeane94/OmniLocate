package com.omnivision.core;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by lkelly on 3/18/2017.
 */
@Entity
public class PartnerDevice implements IPartnerDevice {
    @Id
    private Long id;
    @Unique
    private String partnerDeviceNum;


    @Generated(hash = 1705856336)
    public PartnerDevice(Long id, String partnerDeviceNum) {
        this.id = id;
        this.partnerDeviceNum = partnerDeviceNum;
    }

    @Generated(hash = 121553696)
    public PartnerDevice() {
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setPartnerDeviceNum(String partnerDeviceNum) {
        this.partnerDeviceNum = partnerDeviceNum;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getPartnerDeviceNum() {
        return this.partnerDeviceNum;
    }
}
