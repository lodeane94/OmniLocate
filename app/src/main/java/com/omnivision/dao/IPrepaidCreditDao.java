package com.omnivision.dao;

import com.omnivision.core.PrepaidCredit;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Lodeane on 29/5/2017.
 */

public interface IPrepaidCreditDao {
    void insert(PrepaidCredit prepaidCredit);
    List<PrepaidCredit> findAll();
    List<PrepaidCredit> findAllByOwnerId(Long ownerId);
    PrepaidCredit findByValue(String value);
    QueryBuilder<PrepaidCredit> findByQuery();
    PrepaidCredit find(Long id);
    void update(PrepaidCredit prepaidCredit);
    void delete(PrepaidCredit prepaidCredit);
}
