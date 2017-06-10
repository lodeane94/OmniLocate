package com.omnivision.dao;

import com.omnivision.core.DaoSession;
import com.omnivision.core.SimNetworkCodes;
import com.omnivision.core.SimNetworkCodesDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Lodeane on 7/6/2017.
 */

public class SimNetworkCodeDaoImpl implements ISimNetworkCodeDao {
    private DaoSession daoSession;
    private SimNetworkCodesDao simNetworkCodesDao;

    public SimNetworkCodeDaoImpl(DaoSession daoSession) {
        this.daoSession = daoSession;
        this.simNetworkCodesDao = daoSession.getSimNetworkCodesDao();
    }

    @Override
    public void insert(SimNetworkCodes simNetworkCodes) {
        this.simNetworkCodesDao.insert(simNetworkCodes);
    }

    @Override
    public List<SimNetworkCodes> findAll() {
        return this.simNetworkCodesDao.loadAll();
    }

    @Override
    public List<SimNetworkCodes> findAllByOwnerId(Long ownerId) {
        return null;//TODO implement functionality
    }

    @Override
    public SimNetworkCodes findByValue(String value) {
        QueryBuilder<SimNetworkCodes> queryBuilder = simNetworkCodesDao.queryBuilder();
        queryBuilder.where(SimNetworkCodesDao.Properties.UssdCode.eq(value));
        return queryBuilder.unique();
    }

    @Override
    public SimNetworkCodes find(Long id) {
        return this.simNetworkCodesDao.load(id);
    }

    @Override
    public void update(SimNetworkCodes simNetworkCodes) {
        this.simNetworkCodesDao.update(simNetworkCodes);
    }

    @Override
    public void delete(SimNetworkCodes simNetworkCodes) {
        this.simNetworkCodesDao.delete(simNetworkCodes);
    }

    @Override
    public void deleteAll() {
        this.simNetworkCodesDao.deleteAll();
    }
}
