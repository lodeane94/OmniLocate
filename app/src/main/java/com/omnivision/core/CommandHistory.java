package com.omnivision.core;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by Lodeane on 4/7/2017.
 */
@Entity
public class CommandHistory {
    @Id
    private Long id;
    private Long phoneId;
    @ToOne(joinProperty = "phoneId")
    private Phone phone;
    private String cmd;
    private String dateIssued;
    private String issuedBy;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1124907004)
    private transient CommandHistoryDao myDao;
    @Generated(hash = 457073469)
    private transient Long phone__resolvedKey;


    public CommandHistory(long phoneId, String cmd, String dateIssued, String issuedBy) {
        this.phoneId = phoneId;
        this.cmd = cmd;
        this.dateIssued = dateIssued;
        this.issuedBy = issuedBy;
    }

    @Generated(hash = 724470204)
    public CommandHistory(Long id, Long phoneId, String cmd, String dateIssued,
            String issuedBy) {
        this.id = id;
        this.phoneId = phoneId;
        this.cmd = cmd;
        this.dateIssued = dateIssued;
        this.issuedBy = issuedBy;
    }

    @Generated(hash = 676261819)
    public CommandHistory() {
    }

    public long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(long phoneId) {
        this.phoneId = phoneId;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(String dateIssued) {
        this.dateIssued = dateIssued;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
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

    public Long getId() {
        return this.id;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 374077705)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCommandHistoryDao() : null;
    }

}
