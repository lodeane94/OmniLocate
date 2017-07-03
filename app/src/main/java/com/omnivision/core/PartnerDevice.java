package com.omnivision.core;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.DaoException;

/**
 * Created by lkelly on 3/18/2017.
 */
@Entity
public class PartnerDevice implements IPartnerDevice {
    @Id
    private Long id;
    private Long phoneId;
    @ToOne(joinProperty = "phoneId")
    private Phone phone;
    private Boolean isPrimaryFlag;
    @Unique
    private String partnerDeviceNum;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 718368596)
    private transient PartnerDeviceDao myDao;
    @Generated(hash = 457073469)
    private transient Long phone__resolvedKey;


    public PartnerDevice(Long phoneId, String partnerDeviceNum, Boolean isPrimaryFlag) {
        this.phoneId = phoneId;
        this.partnerDeviceNum = partnerDeviceNum;
        this.isPrimaryFlag = isPrimaryFlag;
    }

    @Generated(hash = 1661640989)
    public PartnerDevice(Long id, Long phoneId, Boolean isPrimaryFlag,
            String partnerDeviceNum) {
        this.id = id;
        this.phoneId = phoneId;
        this.isPrimaryFlag = isPrimaryFlag;
        this.partnerDeviceNum = partnerDeviceNum;
    }

    @Generated(hash = 121553696)
    public PartnerDevice() {
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setPartnerDeviceNum(String partnerDeviceNum) {
        this.partnerDeviceNum = partnerDeviceNum;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getPartnerDeviceNum() {
        return this.partnerDeviceNum;
    }

    public Long getPhoneId() {
        return this.phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public Boolean getIsPrimaryFlag() {
        return this.isPrimaryFlag;
    }

    public void setIsPrimaryFlag(Boolean isPrimaryFlag) {
        this.isPrimaryFlag = isPrimaryFlag;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2078921422)
    public Phone getPhone() {
        Long __key = this.phoneId;
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
    @Generated(hash = 1187165439)
    public void setPhone(Phone phone) {
        synchronized (this) {
            this.phone = phone;
            phoneId = phone == null ? null : phone.getId();
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

    /** returns the value of the object : partner device number*/
    @Override
    public String toString(){
        return partnerDeviceNum;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1823124035)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPartnerDeviceDao() : null;
    }
}
