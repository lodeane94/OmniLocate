package com.omnivision.core;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

/**
 * Created by Lodeane on 2/6/2017.
 */
@Entity
public class SimNetworkCodes {
    @Id
    private Long id;
    private Long simNetworkId;
    @ToOne(joinProperty = "id")
    private SimNetwork simNetwork;
    private String command;
    private String ussdCode;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1099122963)
    private transient SimNetworkCodesDao myDao;
    @Generated(hash = 1159325565)
    private transient Long simNetwork__resolvedKey;

    @Generated(hash = 380711465)
    public SimNetworkCodes(Long id, Long simNetworkId, String command, String ussdCode) {
        this.id = id;
        this.simNetworkId = simNetworkId;
        this.command = command;
        this.ussdCode = ussdCode;
    }

    @Generated(hash = 1987799377)
    public SimNetworkCodes() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSimNetworkId() {
        return simNetworkId;
    }

    public void setSimNetworkId(Long simNetworkId) {
        this.simNetworkId = simNetworkId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 428724756)
    public SimNetwork getSimNetwork() {
        Long __key = this.id;
        if (simNetwork__resolvedKey == null || !simNetwork__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SimNetworkDao targetDao = daoSession.getSimNetworkDao();
            SimNetwork simNetworkNew = targetDao.load(__key);
            synchronized (this) {
                simNetwork = simNetworkNew;
                simNetwork__resolvedKey = __key;
            }
        }
        return simNetwork;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 28081842)
    public void setSimNetwork(SimNetwork simNetwork) {
        synchronized (this) {
            this.simNetwork = simNetwork;
            id = simNetwork == null ? null : simNetwork.getId();
            simNetwork__resolvedKey = id;
        }
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getUssdCode() {
        return ussdCode;
    }

    public void setUssdCode(String ussdCode) {
        this.ussdCode = ussdCode;
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
    @Generated(hash = 2128010500)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSimNetworkCodesDao() : null;
    }
}
