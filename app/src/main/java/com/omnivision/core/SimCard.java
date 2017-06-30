package com.omnivision.core;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

/**
 * Created by Lodeane on 1/6/2017.
 */
@Entity
public class SimCard implements ISimCard {
    @Id
    private long id;
    private String serialNumber;
    private long phoneId;
    private Long simNetworkId;
    @ToOne(joinProperty = "phoneId")
    private Phone phone;//TODO implement function to register simcard to the device
    @ToOne(joinProperty = "simNetworkId")
    private SimNetwork simNetwork;
    @ToMany(referencedJoinProperty = "owner")
    private List<PrepaidCredit> prepaidCredit;
    private String number;
    private String areaCode;
    private double smsCost;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2019687178)
    private transient SimCardDao myDao;
    @Generated(hash = 457073469)
    private transient Long phone__resolvedKey;
    @Generated(hash = 1159325565)
    private transient Long simNetwork__resolvedKey;

    public SimCard(long id, long phoneId, String number, String areaCode, double smsCost,long simNetworkId,String serialNumber) {
        this.id = id;
        this.phoneId = phoneId;
        this.number = number;
        this.areaCode = areaCode;
        this.smsCost = smsCost;
        this.simNetworkId = simNetworkId;
        this.serialNumber = serialNumber;
    }

    @Generated(hash = 1036575371)
    public SimCard(long id, String serialNumber, long phoneId, Long simNetworkId, String number, String areaCode, double smsCost) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.phoneId = phoneId;
        this.simNetworkId = simNetworkId;
        this.number = number;
        this.areaCode = areaCode;
        this.smsCost = smsCost;
    }

    @Generated(hash = 558735383)
    public SimCard() {
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return this.id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public void setPhoneId(long phoneId) {
        this.phoneId = phoneId;
    }

    @Override
    public long getPhoneId() {
        return this.phoneId;
    }

    @Override
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String getNumber() {
        return this.number;
    }

    @Override
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public String getAreaCode() {
        return this.areaCode;
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

    @Override
    public Phone getPhone(Phone phone) {
        return this.phone;
    }

    @Override
    public void setSmsCost(double smsCost) {
        this.smsCost = smsCost;
    }

    @Override
    public double getSmsCost() {
        return this.smsCost;
    }

    public void setSimNetworkId(Long simNetworkId) {
        this.simNetworkId = simNetworkId;
    }

    public Long getSimNetworkId() {
        return this.simNetworkId;
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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1214986753)
    public SimNetwork getSimNetwork() {
        Long __key = this.simNetworkId;
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
    @Generated(hash = 1073800551)
    public void setSimNetwork(SimNetwork simNetwork) {
        synchronized (this) {
            this.simNetwork = simNetwork;
            simNetworkId = simNetwork == null ? null : simNetwork.getId();
            simNetwork__resolvedKey = simNetworkId;
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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2095691975)
    public List<PrepaidCredit> getPrepaidCredit() {
        if (prepaidCredit == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PrepaidCreditDao targetDao = daoSession.getPrepaidCreditDao();
            List<PrepaidCredit> prepaidCreditNew = targetDao._querySimCard_PrepaidCredit(id);
            synchronized (this) {
                if (prepaidCredit == null) {
                    prepaidCredit = prepaidCreditNew;
                }
            }
        }
        return prepaidCredit;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1045450765)
    public synchronized void resetPrepaidCredit() {
        prepaidCredit = null;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1882785717)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSimCardDao() : null;
    }
}
