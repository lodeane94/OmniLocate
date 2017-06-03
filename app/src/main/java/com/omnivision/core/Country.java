package com.omnivision.core;

/**
 * Created by lkelly on 3/16/2017.
 */

public class Country implements ICountry {
    private String name;
    private String callingCode;

    public Country(String name, String callingCode){
        this.name = name;
        this.callingCode = callingCode;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setCountryCallingCode(String callingCode) {
        this.callingCode = callingCode;
    }
}
