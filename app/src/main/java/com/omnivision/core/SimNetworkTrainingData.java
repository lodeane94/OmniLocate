package com.omnivision.core;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.DaoException;

/**
 * Created by Lodeane on 2/6/2017.
 * Is an entity that contains all the training/sample data
 * in which the application will used to determine the result
 * of the ussd codes
 */
@Entity
public class SimNetworkTrainingData {
    @Id
    private Long id;
    private Long simNetworkCodeId;
    @ToOne(joinProperty = "id")
    private SimNetworkCodes simNetworkCodes;
    @Unique
    private String expectedResultSnippet;
    private int totalUsedCount;
    private String dateAdded;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1566878690)
    private transient SimNetworkTrainingDataDao myDao;
    @Generated(hash = 1837980357)
    private transient Long simNetworkCodes__resolvedKey;

    @Generated(hash = 1752241107)
    public SimNetworkTrainingData(Long id, Long simNetworkCodeId, String expectedResultSnippet, int totalUsedCount, String dateAdded) {
        this.id = id;
        this.simNetworkCodeId = simNetworkCodeId;
        this.expectedResultSnippet = expectedResultSnippet;
        this.totalUsedCount = totalUsedCount;
        this.dateAdded = dateAdded;
    }

    @Generated(hash = 344220303)
    public SimNetworkTrainingData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSimNetworkCodeId() {
        return simNetworkCodeId;
    }

    public void setSimNetworkCodeId(Long simNetworkCodeId) {
        this.simNetworkCodeId = simNetworkCodeId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 980252105)
    public SimNetworkCodes getSimNetworkCodes() {
        Long __key = this.id;
        if (simNetworkCodes__resolvedKey == null || !simNetworkCodes__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SimNetworkCodesDao targetDao = daoSession.getSimNetworkCodesDao();
            SimNetworkCodes simNetworkCodesNew = targetDao.load(__key);
            synchronized (this) {
                simNetworkCodes = simNetworkCodesNew;
                simNetworkCodes__resolvedKey = __key;
            }
        }
        return simNetworkCodes;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1180478058)
    public void setSimNetworkCodes(SimNetworkCodes simNetworkCodes) {
        synchronized (this) {
            this.simNetworkCodes = simNetworkCodes;
            id = simNetworkCodes == null ? null : simNetworkCodes.getId();
            simNetworkCodes__resolvedKey = id;
        }
    }

    public String getExpectedResultSnippet() {
        return expectedResultSnippet;
    }

    public void setExpectedResultSnippet(String expectedResultSnippet) {
        this.expectedResultSnippet = expectedResultSnippet;
    }

    public int getTotalUsedCount() {
        return totalUsedCount;
    }

    public void setTotalUsedCount(int totalUsedCount) {
        this.totalUsedCount = totalUsedCount;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
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
    @Generated(hash = 1155777006)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSimNetworkTrainingDataDao() : null;
    }
}
