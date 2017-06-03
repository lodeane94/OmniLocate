package com.omnivision.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by Lodeane on 3/5/2017.
 * Class is used for the management of prepaid credits that can be stored to the
 * device for later usage
 */
@Entity
public class PrepaidCredit implements IPrepaidCredit {
    @Id
    private Long id;
    @Unique
    private String voucherNum;
    private Long owner;
    @ToOne(joinProperty = "id")
    private SimCard simCard;
    private Boolean isUsed;
    private String dateAdded;
    private String addedBy;
    @Transient
    private Context context;
    @Transient
    private String TAG = "PrepaidCredit";
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1863472139)
    private transient PrepaidCreditDao myDao;
    @Generated(hash = 1814477768)
    private transient Long simCard__resolvedKey;
    public PrepaidCredit(String voucherNum,String addedBy,Long owner, Context context){
        this.voucherNum = voucherNum;
        this.isUsed = false;
        this.dateAdded = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        this.addedBy = addedBy;
        this.owner = owner;
        this.context = context;
    }

    @Generated(hash = 1582892666)
    public PrepaidCredit(Long id, String voucherNum, Long owner, Boolean isUsed, String dateAdded, String addedBy) {
        this.id = id;
        this.voucherNum = voucherNum;
        this.owner = owner;
        this.isUsed = isUsed;
        this.dateAdded = dateAdded;
        this.addedBy = addedBy;
    }

    @Generated(hash = 1455849440)
    public PrepaidCredit() {
    }

    @Override
    public Boolean addCredit() {
       // Intent intent = new Intent(android.content.Intent.ACTION_CALL, Uri.parse("tel: *121*"+voucherNum+Uri.encode()));
       // context.startService(intent);
        return null;
    }

    @Override
    public Boolean removeCredit(String voucherNum) {
        return null;
    }

    /**
     * @author lkelly
     * @desc ensures the prepaid credit being entered is all numbers
     * @params
     * @return boolean
     * */
    @Override
    public Boolean validateCreditFormat() {
        /*if parseInt does not throw an NumberFormatException
        * then that means the voucher number format is valid*/
        int num = 0;
        try{
            num = Integer.parseInt(voucherNum);
        }catch (NumberFormatException numEx){
            Log.e(TAG,"Voucher number +"+num+", format is invalid \n"+numEx.getLocalizedMessage());
            numEx.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean isUsed(String voucherNum) {
        return false;
    }

    @Override
    public List<String> getPrepaidCredits() {
        return null;
    }

    public String getVoucherNum() {
        return voucherNum;
    }

    public void setVoucherNum(String voucherNum) {
        this.voucherNum = voucherNum;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsUsed() {
        return this.isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public String getDateAdded() {
        return this.dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getAddedBy() {
        return this.addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public Long getOwner() {
        return this.owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1466245024)
    public SimCard getSimCard() {
        Long __key = this.id;
        if (simCard__resolvedKey == null || !simCard__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SimCardDao targetDao = daoSession.getSimCardDao();
            SimCard simCardNew = targetDao.load(__key);
            synchronized (this) {
                simCard = simCardNew;
                simCard__resolvedKey = __key;
            }
        }
        return simCard;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 273772914)
    public void setSimCard(SimCard simCard) {
        synchronized (this) {
            this.simCard = simCard;
            id = simCard == null ? null : simCard.getId();
            simCard__resolvedKey = id;
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
    @Generated(hash = 1763209516)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPrepaidCreditDao() : null;
    }
}
