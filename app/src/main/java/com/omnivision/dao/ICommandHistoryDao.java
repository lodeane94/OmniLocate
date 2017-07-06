package com.omnivision.dao;

import com.omnivision.core.CommandHistory;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Lodeane on 4/7/2017.
 */

public interface ICommandHistoryDao {
    void insert(CommandHistory value);
    void insertAll(List<CommandHistory> objects);
    List<CommandHistory> findAll();
    List<CommandHistory> findAllByOwnerId(Long value);
    List<CommandHistory> getAllByCmdIssuer(String key);
    CommandHistory findByValue(String value);
    QueryBuilder<CommandHistory> findByQuery();
    CommandHistory find(Long key);
    void update(CommandHistory object);
    void delete(CommandHistory object);

}
