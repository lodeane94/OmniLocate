package com.omnivision.core;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Lodeane on 3/5/2017.
 */
@Entity
public class SimNetwork {
    @Id
    private Long id;
    private String networkProvider;
    private String country;

    public SimNetwork(Long id, String networkProvider, String command, String ussdCode) {
        this.id = id;
        this.networkProvider = networkProvider;
    }

    @Generated(hash = 1182667396)
    public SimNetwork(Long id, String networkProvider, String country) {
        this.id = id;
        this.networkProvider = networkProvider;
        this.country = country;
    }

    @Generated(hash = 2001832851)
    public SimNetwork() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNetworkProvider() {
        return this.networkProvider;
    }

    public void setNetworkProvider(String networkProvider) {
        this.networkProvider = networkProvider;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
