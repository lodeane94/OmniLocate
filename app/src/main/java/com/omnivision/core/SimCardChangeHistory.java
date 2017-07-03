package com.omnivision.core;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by Lodeane on 29/6/2017.
 */
@Entity
public class SimCardChangeHistory {
    @Id
    private long id;
    private long phoneId;
    @ToOne(joinProperty = "phoneId")
    private Phone phone;
    private String serialNumber;
    private String number;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1484431445)
    private transient SimCardChangeHistoryDao myDao;
    @Generated(hash = 457073469)
    private transient Long phone__resolvedKey;


    @Generated(hash = 1139277657)
    public SimCardChangeHistory(long id, long phoneId, String serialNumber,
            String number) {
        this.id = id;
        this.phoneId = phoneId;
        this.serialNumber = serialNumber;
        this.number = number;
    }

    public SimCardChangeHistory(long phoneId, String serialNumber, String number) {
        this.phoneId = phoneId;
        this.serialNumber = serialNumber;
        this.number = number;
    }

    @Generated(hash = 448772103)
    public SimCardChangeHistory() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(long phoneId) {
        this.phoneId = phoneId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 398581896)
    public Phone getPhone() {
        long __key = this.phoneId;
        if (phone__resolvedKey == null || !phone__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PhoneDao targetDao = daoSession.getPhoneDao();
            Phone phoneNew = targetDao.load(__key);
            synchronized (this) {
                phone = phoneNew;
                phone__resolvedKey = __key;
            }
        }
        return phone;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1952452884)
    public void setPhone(@NotNull Phone phone) {
        if (phone == null) {
            throw new DaoException(
                    "To-one property 'phoneId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.phone = phone;
            phoneId = phone.getId();
            phone__resolvedKey = phoneId;
        }
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
    @Generated(hash = 482687922)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSimCardChangeHistoryDao() : null;
    }
}
