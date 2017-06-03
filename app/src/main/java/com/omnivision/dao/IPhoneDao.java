package com.omnivision.dao;

import com.omnivision.core.Phone;
import com.omnivision.core.PrepaidCredit;

import java.util.List;

/**
 * Created by Lodeane on 29/5/2017.
 */

public interface IPhoneDao {
    void insert(Phone phone);
    List<Phone> findAll();
    List<Phone> findAllByOwnerId(Long ownerId);
    Phone findByValue(String value);
    Phone find(Long id);
    void update(Phone phone);
    void delete(Phone phone);
}
