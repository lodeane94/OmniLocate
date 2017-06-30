package com.omnivision.dao;

import com.omnivision.core.SimCardChangeHistory;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Lodeane on 30/6/2017.
 */

public interface ISimCardChangeHistDao {
    void insert(SimCardChangeHistory simCardChangeHistory);
    void insertAll(List<SimCardChangeHistory> simCardChangeHistories);
    List<SimCardChangeHistory> findAll();
    List<SimCardChangeHistory> findAllByOwnerId(Long ownerId);
    SimCardChangeHistory findByValue(String value);
    QueryBuilder<SimCardChangeHistory> findByQuery();
    SimCardChangeHistory find(Long id);
    void update(SimCardChangeHistory simCardChangeHistory);
    void delete(SimCardChangeHistory simCardChangeHistory);
}
