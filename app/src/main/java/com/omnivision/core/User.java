package com.omnivision.core;

import java.lang.Object;
import java.util.ArrayList;
import java.util.List;
import com.omnivision.utilities.*;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;
import com.omnivision.core.DaoSession;
import com.omnivision.core.UserDao;
import org.greenrobot.greendao.DaoException;


/**
 * Created by lkelly on 3/7/2017.
 */
@Entity
public class User implements IUser{

    @Id
    private  Long id;
    private String email;
    private String password;
    @ToMany(referencedJoinProperty = "userId")
    private List<Phone> devices;
    @Keep
    @Transient
    private DaoSession daoSession;
    @Keep
    @Transient
    private UserDao myDao;

    public User(String email, String password, Phone device) throws SystemValidationException {
        if(!validateEmail(email)){
            throw new SystemValidationException("Email Validation Failed");
        }
        if(!validatePassword(password)){
            throw new SystemValidationException("Password Validation Failed");
        }

        this.email = email;
        this.password = password;

        this.devices = new ArrayList<Phone>();
        this.devices.add(device);
    }

    @Generated(hash = 1471192367)
    public User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setDevices(List<Phone> devices){
        this.devices = devices;
    }

    @Override
    public Boolean registerUser() {
        return null;
    }

     @Override
   public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Boolean validateEmail(String email) {
        return true;
    }

    @Override
    public Boolean validatePassword(String password) {
        return true;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 728560747)
    public List<Phone> getDevices() {
        if (devices == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PhoneDao targetDao = daoSession.getPhoneDao();
            List<Phone> devicesNew = targetDao._queryUser_Devices(id);
            synchronized (this) {
                if (devices == null) {
                    devices = devicesNew;
                }
            }
        }
        return devices;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1428662284)
    public synchronized void resetDevices() {
        devices = null;
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
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

}
