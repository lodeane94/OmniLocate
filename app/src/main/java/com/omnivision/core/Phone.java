package com.omnivision.core;

import com.omnivision.utilities.SystemValidationException;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by lkelly on 3/15/2017.
 */
@Entity
public class Phone implements IPhone {
    @Id
    private Long id;
    private  Long userId;
    @ToMany(referencedJoinProperty = "phoneId")
    private List<SimCard> simCards;//TODO application should be able to link additional simcards
    private String cellNum;
    private String country;
    private String deviceStatus;
    @ToMany(referencedJoinProperty = "id")
    private List<PartnerDevice> partnerDevicesNums = new ArrayList<PartnerDevice>();
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 606281921)
    private transient PhoneDao myDao;

    public Phone(Long id, String cellNumber, String country,
                 String deviceStatus, List<PartnerDevice> partnerDevicesNums)
            throws SystemValidationException{
        if(!validateCellNum()){
            throw new SystemValidationException("Cell number validation failed");
        }
        this.id = id;
        this.cellNum = cellNumber;
        this.country = country;
        this.deviceStatus = deviceStatus;

        this.partnerDevicesNums = partnerDevicesNums;
    }

    @Generated(hash = 1271786037)
    public Phone(Long id, Long userId, String cellNum, String country, String deviceStatus) {
        this.id = id;
        this.userId = userId;
        this.cellNum = cellNum;
        this.country = country;
        this.deviceStatus = deviceStatus;
    }

    @Generated(hash = 429398894)
    public Phone() {
    }

    @Override
    public void setId(Long imeiNum) {
        this.id = imeiNum;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public void setCellNum(String cellNumber) {
        this.cellNum = cellNumber;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public void setDeviceStatus(String status) {
        this.deviceStatus = status;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public String getCountry() {
        return this.country;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getCellNum() {
        return this.cellNum;
    }

    @Override
    public String getDeviceStatus() {
        return deviceStatus;
    }

    @Override
    public void setPartnerDevices(List<PartnerDevice> partnerDevicesNums){
        this.partnerDevicesNums = partnerDevicesNums;
    }

    @Override
    public List<PartnerDevice> getPartnerDevicesNumbers(){
        return this.partnerDevicesNums;
    }

    public static Boolean isAPartnerDeviceNumber(String cellNum){
        return true; //TODO implement the validation of partner device numbers of a phone
    }

    @Override
    public Boolean validateCellNum() {
        return true;//TODO create funnctionality to validate numbers
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 427290691)
    public List<PartnerDevice> getPartnerDevicesNums() {
        if (partnerDevicesNums == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PartnerDeviceDao targetDao = daoSession.getPartnerDeviceDao();
            List<PartnerDevice> partnerDevicesNumsNew = targetDao._queryPhone_PartnerDevicesNums(id);
            synchronized (this) {
                if (partnerDevicesNums == null) {
                    partnerDevicesNums = partnerDevicesNumsNew;
                }
            }
        }
        return partnerDevicesNums;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 85296983)
    public synchronized void resetPartnerDevicesNums() {
        partnerDevicesNums = null;
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
    @Generated(hash = 143049516)
    public List<SimCard> getSimCards() {
        if (simCards == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SimCardDao targetDao = daoSession.getSimCardDao();
            List<SimCard> simCardsNew = targetDao._queryPhone_SimCards(id);
            synchronized (this) {
                if (simCards == null) {
                    simCards = simCardsNew;
                }
            }
        }
        return simCards;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1265663133)
    public synchronized void resetSimCards() {
        simCards = null;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 663553079)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPhoneDao() : null;
    }


}
