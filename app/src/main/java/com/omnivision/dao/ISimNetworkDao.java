package com.omnivision.dao;

import com.omnivision.core.SimCard;
import com.omnivision.core.SimNetwork;

import java.util.List;

/**
 * Created by Lodeane on 3/6/2017.
 */

public interface ISimNetworkDao {
    void insert(SimNetwork simNetwork);
    List<SimNetwork> findAll();
    List<SimNetwork> findAllByOwnerId(Long ownerId);
    SimNetwork findByValue(String value);
    SimNetwork find(Long id);
    void update(SimNetwork simNetwork);
    void delete(SimNetwork simNetwork);
}
