package com.omnivision.dao;

import com.omnivision.core.DaoSession;
import com.omnivision.core.PrepaidCredit;
import com.omnivision.core.PrepaidCreditDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Lodeane on 29/5/2017.
 */

public class PrepaidCreditDaoImpl implements IPrepaidCreditDao{

    DaoSession daoSession;
    PrepaidCreditDao prepaidCreditDao;

    public PrepaidCreditDaoImpl(DaoSession daoSession){
        this.daoSession = daoSession;
        this.prepaidCreditDao = daoSession.getPrepaidCreditDao();
    }

    @Override
    public void insert(PrepaidCredit prepaidCredit) {
        prepaidCreditDao.insert(prepaidCredit);
    }

    @Override
    public List<PrepaidCredit> findAll() {
        return prepaidCreditDao.loadAll();
    }

    @Override
    public List<PrepaidCredit> findAllByOwnerId(Long ownerId) {
        return daoSession.getSimCardDao().load(ownerId).getPrepaidCredit();
    }

    @Override
    public PrepaidCredit findByValue(String value) {
        QueryBuilder<PrepaidCredit> queryBuilder = prepaidCreditDao.queryBuilder();
        queryBuilder.where(PrepaidCreditDao.Properties.VoucherNum.eq(value));
        return queryBuilder.unique();
    }

    @Override
    public PrepaidCredit find(Long id) {
        return prepaidCreditDao.load(id);
    }

    @Override
    public void update(PrepaidCredit prepaidCredit) {
        prepaidCreditDao.update(prepaidCredit);
    }

    @Override
    public void delete(PrepaidCredit prepaidCredit) {
        prepaidCreditDao.delete(prepaidCredit);
    }
}
