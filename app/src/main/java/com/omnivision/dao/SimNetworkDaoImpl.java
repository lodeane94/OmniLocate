package com.omnivision.dao;

import com.omnivision.core.DaoSession;
import com.omnivision.core.SimNetwork;
import com.omnivision.core.SimNetworkDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Lodeane on 3/6/2017.
 */

public class SimNetworkDaoImpl implements ISimNetworkDao {

    private DaoSession daoSession;
    private SimNetworkDao simNetworkDao;

    public SimNetworkDaoImpl(DaoSession daoSession) {
        this.daoSession = daoSession;
        this.simNetworkDao = daoSession.getSimNetworkDao();
    }

    @Override
    public void insert(SimNetwork simNetwork) {
        this.simNetworkDao.insert(simNetwork);
    }

    @Override
    public List<SimNetwork> findAll() {
        return this.simNetworkDao.loadAll();
    }

    @Override
    public List<SimNetwork> findAllByOwnerId(Long ownerId) {
        return null;
    }

    @Override
    public SimNetwork findByValue(String value) {
        QueryBuilder<SimNetwork> queryBuilder = simNetworkDao.queryBuilder();
        queryBuilder.where(SimNetworkDao.Properties.NetworkProvider.eq(value));
        return queryBuilder.unique();
    }

    @Override
    public SimNetwork find(Long id) {
        return this.simNetworkDao.load(id);
    }

    @Override
    public void update(SimNetwork simNetwork) {
        this.simNetworkDao.update(simNetwork);
    }

    @Override
    public void delete(SimNetwork simNetwork) {
        this.simNetworkDao.delete(simNetwork);
    }
}
