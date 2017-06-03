package com.omnivision.core;

import java.util.List;

/**
 * Created by lkelly on 3/7/2017.
 */

public interface IUser {
    void setId(Long id);
    Long getId();
    void setEmail(String email);
    void setPassword(String password);
    void setDevices(List<Phone> devices);
    Boolean registerUser();
    String getEmail();
    String getPassword();
    Boolean validateEmail(String email);
    Boolean validatePassword(String password);
}
