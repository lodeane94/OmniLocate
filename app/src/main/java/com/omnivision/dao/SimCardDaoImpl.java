package com.omnivision.dao;

import com.omnivision.core.DaoSession;
import com.omnivision.core.SimCard;
import com.omnivision.core.SimCardDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Lodeane on 2/6/2017.
 */

public class SimCardDaoImpl implements ISimCardDao {
    private DaoSession daoSession;
    private SimCardDao simCardDao;

    public SimCardDaoImpl(DaoSession daoSession) {
        this.daoSession = daoSession;
        this.simCardDao = daoSession.getSimCardDao();
    }

    @Override
    public void insert(SimCard simCard) {
        this.simCardDao.insert(simCard);
    }

    @Override
    public List<SimCard> findAll() {
        return this.simCardDao.loadAll();
    }

    @Override
    public List<SimCard> findAllByOwnerId(Long ownerId) {
        return this.daoSession.getPhoneDao().load(ownerId).getSimCards();
    }

    @Override
    public SimCard findByValue(String value) {
        QueryBuilder<SimCard> queryBuilder = simCardDao.queryBuilder();
        queryBuilder.where(SimCardDao.Properties.Number.eq(value));
        return queryBuilder.unique();
    }

    @Override
    public SimCard find(Long id) {
        return this.simCardDao.load(id);
    }

    @Override
    public void update(SimCard simCard) {
        this.simCardDao.update(simCard);
    }

    @Override
    public void delete(SimCard simCard) {
        this.simCardDao.delete(simCard);
    }

    @Override
    public void deleteAll() {
        this.simCardDao.deleteAll();
    }
}
