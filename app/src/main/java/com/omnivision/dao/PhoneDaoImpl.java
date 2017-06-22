package com.omnivision.dao;

import com.omnivision.core.DaoSession;
import com.omnivision.core.Phone;
import com.omnivision.core.PhoneDao;
import com.omnivision.core.PrepaidCredit;
import com.omnivision.core.PrepaidCreditDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Lodeane on 29/5/2017.
 */

public class PhoneDaoImpl implements IPhoneDao {

    DaoSession daoSession;
    PhoneDao phoneDao;

    public PhoneDaoImpl(DaoSession daoSession){
        this.daoSession = daoSession;
        this.phoneDao = daoSession.getPhoneDao();
    }

    @Override
    public void insert(Phone phone) {
        phoneDao.insert(phone);
    }

    @Override
    public List<Phone> findAll() {
        return phoneDao.loadAll();
    }

    @Override
    public List<Phone> findAllByOwnerId(Long ownerId) {
        return daoSession.getUserDao().load(ownerId).getDevices();
    }

    @Override
    public Phone findByValue(String value) {
        QueryBuilder<Phone> queryBuilder = phoneDao.queryBuilder();
        queryBuilder.where(PhoneDao.Properties.CellNum.eq(value));
        return queryBuilder.unique();
    }

    @Override
    public QueryBuilder<Phone> findByQuery() {
        QueryBuilder<Phone> queryBuilder = phoneDao.queryBuilder();
        return queryBuilder;
    }

    @Override
    public Phone find(Long id) {
        return phoneDao.load(id);
    }

    @Override
    public void update(Phone phone) {
        phoneDao.update(phone);
    }

    @Override
    public void delete(Phone phone) {
        phoneDao.delete(phone);
    }
}
