package com.omnivision.dao;

import com.omnivision.core.PartnerDevice;
import com.omnivision.core.Phone;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Lodeane on 18/6/2017.
 */

public interface IPartnerDeviceDao {
    void insert(PartnerDevice partnerDevice);
    void insertAll(List<PartnerDevice> partnerDevices);
    List<PartnerDevice> findAll();
    List<PartnerDevice> findAllByOwnerId(Long ownerId);
    PartnerDevice findByValue(String value);
    PartnerDevice findPrimaryDevice(Long ownerId);
    QueryBuilder<PartnerDevice> findByQuery();
    PartnerDevice find(Long id);
    void update(PartnerDevice partnerDevice);
    void delete(PartnerDevice partnerDevice);
}
