package com.tablayoutexample.lkelly.omnivision;

import android.app.Application;
import android.content.Context;

import com.omnivision.core.Constants;
import com.omnivision.core.DaoSession;
import com.omnivision.core.Phone;
import com.omnivision.dao.DbHelper;
import com.omnivision.dao.IPhoneDao;
import com.omnivision.dao.PhoneDaoImpl;
import com.omnivision.utilities.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lkelly on 3/14/2017.
 */

public class OmniLocateApplication extends Application {

    private static OmniLocateApplication _instance = null;
    private static Phone _phoneInstance = null;
    private static SessionManager sessionManager = null;
    private static HashMap<String,String> userDetails = null;
    private static ArrayList<String> appIssuedCommand = new ArrayList<String>();
    private DbHelper _dbHelper = new DbHelper();
    private static String userName = "Lodeane";

    @Override
    public void onCreate(){
        super.onCreate();
        _instance = this;
        sessionManager = new SessionManager(this);
        userDetails = sessionManager.getUserDetails();
    }

    public static OmniLocateApplication getInstance(){
        return _instance;
    }

    /**
     * @author lkelly
     * @desc Returns Phone object since only one instance of it is necessary throughout the application
     *       this is done to reduce calls to the database
     * @params
     * @return
     * */
    public static Phone getPhoneInstance() {
        /*implementing the Phone object singleton within the application*/
        if(_phoneInstance != null) {
            return _phoneInstance;
        }else {
            if (userDetails.get(Constants.SessionManager.PHONE_ID) != null
                    || userDetails.get(Constants.SessionManager.PHONE_ID) != "") {
                long phoneId = Long.parseLong(userDetails.get(Constants.SessionManager.PHONE_ID));
                IPhoneDao phoneDao = new PhoneDaoImpl(getSession(getInstance()));
                _phoneInstance = phoneDao.find(phoneId);
            }
        }
        return _phoneInstance;
    }
    /**
     * @author lkelly
     * @desc Signal a change on one of the properties of the phone object
     *       hence the instance is nulled in order to pull new information
     * @params
     * @return
     * */
    public static void signalPhoneInstanceChange() {
        _phoneInstance = null;
    }

    public  static  DaoSession getNewSession(Context context){
        return getInstance()._dbHelper.getNewSession(context);
    }

    public  static  DaoSession getSession(Context context){
        return getInstance()._dbHelper.getSession(context);
    }

    /**
     * @author lkelly
     * @desc returns boolean which indicates that a command was tailored for this app
     * @params
     * @return
     * */
    public static boolean isAppIssuedCommand(String command) {
        return appIssuedCommand.contains(command);
    }
    /**
     * @author lkelly
     * @desc adds command to the list of commands issued by the app
     * @params
     * @return
     * */
    public static void addAppIssuedCommand(String command) {
        OmniLocateApplication.appIssuedCommand.add(command);
    }
    /**
     * @author lkelly
     * @desc removes command from the list of commands issued by the app
     * @params
     * @return
     * */
    public static boolean removeAppIssuedCommand(String command) {
        return OmniLocateApplication.appIssuedCommand.remove(command);
    }
    /**
     * @author lkelly
     * @desc returns username logged into the application
     * @params
     * @return
     * */
    public static String getUserName() {
        return userName;
    }
    /**
     * @author lkelly
     * @desc removes command from the list of commands issued by the app
     * @params
     * @return
     * */
    public static void setUserName(String name) {
        userName = name;
    }
}
