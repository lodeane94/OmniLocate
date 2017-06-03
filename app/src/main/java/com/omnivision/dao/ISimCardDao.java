package com.omnivision.dao;

import com.omnivision.core.SimCard;

import java.util.List;

/**
 * Created by Lodeane on 2/6/2017.
 */

public interface ISimCardDao {
    void insert(SimCard simCard);
    List<SimCard> findAll();
    List<SimCard> findAllByOwnerId(Long ownerId);
    SimCard findByValue(String value);
    SimCard find(Long id);
    void update(SimCard simCard);
    void delete(SimCard simCard);
}
