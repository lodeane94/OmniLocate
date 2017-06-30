package com.omnivision.dao;

import com.omnivision.core.DaoSession;
import com.omnivision.core.IPartnerDevice;
import com.omnivision.core.PartnerDevice;
import com.omnivision.core.PartnerDeviceDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Lodeane on 18/6/2017.
 */

public class PartnerDeviceDaoImpl implements IPartnerDeviceDao {
    DaoSession daoSession;
    PartnerDeviceDao partnerDeviceDao;

    public PartnerDeviceDaoImpl(DaoSession daoSession){
        this.daoSession = daoSession;
        this.partnerDeviceDao = daoSession.getPartnerDeviceDao();
    }

    @Override
    public void insert(PartnerDevice partnerDevice) {
        partnerDeviceDao.insert(partnerDevice);
    }

    @Override
    public void insertAll(List<PartnerDevice> partnerDevices) {
        for(PartnerDevice partnerDevice : partnerDevices){
            partnerDeviceDao.insert(partnerDevice);
        }
    }

    @Override
    public List<PartnerDevice> findAll() {
        return partnerDeviceDao.loadAll();
    }

    @Override
    public List<PartnerDevice> findAllByOwnerId(Long ownerId) {
        return daoSession.getPhoneDao().load(ownerId).getPartnerDevicesNumbers();
    }

    @Override
    public PartnerDevice findByValue(String value) {
        QueryBuilder<PartnerDevice> queryBuilder = partnerDeviceDao.queryBuilder();
        queryBuilder.where(PartnerDeviceDao.Properties.PartnerDeviceNum.eq(value));
        return queryBuilder.unique();
    }

    @Override
    public PartnerDevice findPrimaryDevice(Long ownerId) {
        QueryBuilder<PartnerDevice> queryBuilder = partnerDeviceDao.queryBuilder();
        queryBuilder.where(PartnerDeviceDao.Properties.IsPrimaryFlag.eq(true),
                PartnerDeviceDao.Properties.PhoneId.eq(ownerId));
        return queryBuilder.unique();
    }

    @Override
    public QueryBuilder<PartnerDevice> findByQuery() {
        QueryBuilder<PartnerDevice> queryBuilder = partnerDeviceDao.queryBuilder();
        return queryBuilder;
    }

    @Override
    public PartnerDevice find(Long id) {
        return partnerDeviceDao.load(id);
    }

    @Override
    public void update(PartnerDevice partnerDevice) {
        partnerDeviceDao.update(partnerDevice);
    }

    @Override
    public void delete(PartnerDevice partnerDevice) {
        partnerDeviceDao.delete(partnerDevice);
    }
}
