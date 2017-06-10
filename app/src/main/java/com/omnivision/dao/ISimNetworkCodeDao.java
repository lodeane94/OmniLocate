package com.omnivision.dao;

import com.omnivision.core.SimNetworkCodes;

import java.util.List;

/**
 * Created by Lodeane on 7/6/2017.
 */

public interface ISimNetworkCodeDao {
    void insert(SimNetworkCodes simNetworkCodes);
    List<SimNetworkCodes> findAll();
    List<SimNetworkCodes> findAllByOwnerId(Long ownerId);
    SimNetworkCodes findByValue(String value);
    SimNetworkCodes find(Long id);
    void update(SimNetworkCodes simNetworkCodes);
    void delete(SimNetworkCodes simNetworkCodes);
    void deleteAll();
}
