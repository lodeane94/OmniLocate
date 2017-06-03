package com.tablayoutexample.lkelly.omnivision;

import android.app.Application;
import android.content.Context;

import com.omnivision.core.DaoSession;
import com.omnivision.dao.DbHelper;

import java.util.ArrayList;

/**
 * Created by lkelly on 3/14/2017.
 */

public class OmniLocateApplication extends Application {

    private static OmniLocateApplication _instance = null;
    private static ArrayList<String> appIssuedCommand = new ArrayList<String>();
    private DbHelper _dbHelper = new DbHelper();
    private static String userName = "Lodeane";

    @Override
    public void onCreate(){
        super.onCreate();
        _instance = this;
    }

    private static OmniLocateApplication getInstance(){
        return _instance;
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
