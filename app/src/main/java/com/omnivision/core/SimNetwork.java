package com.omnivision.core;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.util.List;
import org.greenrobot.greendao.DaoException;

/**
 * Created by Lodeane on 3/5/2017.
 */
@Entity
public class SimNetwork {
    @Id
    private Long id;
    private Long simNetworkCodesId;
    @ToMany(referencedJoinProperty = "id")
    private List<SimNetworkCodes> simNetworkCodes;
    private String networkProvider;
    private String country;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 362191240)
    private transient SimNetworkDao myDao;

    public SimNetwork(String networkProvider,String country) {
        this.networkProvider = networkProvider;
        this.country = country;
    }

    @Generated(hash = 316214878)
    public SimNetwork(Long id, Long simNetworkCodesId, String networkProvider,
            String country) {
        this.id = id;
        this.simNetworkCodesId = simNetworkCodesId;
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

    public Long getSimNetworkCodesId() {
        return simNetworkCodesId;
    }

    public void setSimNetworkCodesId(Long simNetworkCodesId) {
        this.simNetworkCodesId = simNetworkCodesId;
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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1670209918)
    public List<SimNetworkCodes> getSimNetworkCodes() {
        if (simNetworkCodes == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SimNetworkCodesDao targetDao = daoSession.getSimNetworkCodesDao();
            List<SimNetworkCodes> simNetworkCodesNew = targetDao
                    ._querySimNetwork_SimNetworkCodes(id);
            synchronized (this) {
                if (simNetworkCodes == null) {
                    simNetworkCodes = simNetworkCodesNew;
                }
            }
        }
        return simNetworkCodes;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 865750891)
    public synchronized void resetSimNetworkCodes() {
        simNetworkCodes = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1923185220)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSimNetworkDao() : null;
    }
}
