package com.omnivision.dao;

import com.omnivision.core.DaoSession;
import com.omnivision.core.SimCardChangeHistory;
import com.omnivision.core.SimCardChangeHistoryDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Lodeane on 30/6/2017.
 */

public class SimCardChangeHistDaoImpl implements ISimCardChangeHistDao {

    DaoSession daoSession;
    SimCardChangeHistoryDao simCardChangeHistoryDao;

    public SimCardChangeHistDaoImpl(DaoSession daoSession){
        this.daoSession = daoSession;
        this.simCardChangeHistoryDao = daoSession.getSimCardChangeHistoryDao();
    }
    @Override
    public void insert(SimCardChangeHistory simCardChangeHistory) {
        simCardChangeHistoryDao.insert(simCardChangeHistory);
    }

    @Override
    public void insertAll(List<SimCardChangeHistory> simCardChangeHistories) {
        for(SimCardChangeHistory simCardChangeHistory : simCardChangeHistories){
            simCardChangeHistoryDao.insert(simCardChangeHistory);
        }
    }

    @Override
    public List<SimCardChangeHistory> findAll() {
        return simCardChangeHistoryDao.loadAll();
    }

    @Override
    public List<SimCardChangeHistory> findAllByOwnerId(Long ownerId) {
        return daoSession.getPhoneDao().load(ownerId).getSimCardChangeHistories();
    }

    @Override
    public SimCardChangeHistory findByValue(String value) {
        QueryBuilder<SimCardChangeHistory> queryBuilder = simCardChangeHistoryDao.queryBuilder();
        queryBuilder.where(SimCardChangeHistoryDao.Properties.Number.eq(value));
        return queryBuilder.unique();
    }

    @Override
    public QueryBuilder<SimCardChangeHistory> findByQuery() {
        QueryBuilder<SimCardChangeHistory> queryBuilder = simCardChangeHistoryDao.queryBuilder();
        return queryBuilder;
    }

    @Override
    public SimCardChangeHistory find(Long id) {
        return simCardChangeHistoryDao.load(id);
    }

    @Override
    public void update(SimCardChangeHistory simCardChangeHistory) {
        simCardChangeHistoryDao.update(simCardChangeHistory);
    }

    @Override
    public void delete(SimCardChangeHistory simCardChangeHistory) {
        simCardChangeHistoryDao.delete(simCardChangeHistory);
    }
}
