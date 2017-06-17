package com.omnivision.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.omnivision.core.Constants;

import java.util.HashMap;

/**
 * Created by Lodeane on 26/5/2017.
 */

public class PhoneManager {
    private String TAG = SessionManager.class.getSimpleName();
    //shared preferences
    private SharedPreferences pref;
    //editor for the shared preferences
    private SharedPreferences.Editor editor;
    private Context context;

    public PhoneManager(Context context){
        this.context = context;
        pref = this.context.getSharedPreferences(Constants.PhoneManager.PREF_NAME,Constants.SessionManager.PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * @author lkelly
     * @desc creates shared preference data that holds the information on the phone and it's user
     * @params phoneId, userId
     * @return
     * */
    public void registerDevice(String phoneId, String userId, String simCardId){
        Log.d(TAG,"registerDevice : init");
        //setting isLogin pref to true
        editor.putString(Constants.SessionManager.PHONE_ID,phoneId);
        editor.putString(Constants.SessionManager.USER_ID,userId);
        editor.putString(Constants.SessionManager.SIM_ID,simCardId);
        //commit changes to the editor
        editor.commit();
        Log.d(TAG,"Phone ID : "+phoneId+" User ID : "+userId);
        Log.d(TAG,"registerDevice: exit");
    }


    /**
     * @author lkelly
     * @desc gets stored data that uniquely identifies this device
     * @params
     * @returns map<USER_ID,PHONE_ID>
     * */
    public HashMap<String,String> getPhoneDetails(){
        Log.d(TAG,"getPhoneDetails : init");
        HashMap userDetails = new HashMap<String,String>();
        userDetails.put(Constants.SessionManager.USER_ID,pref.getString(Constants.SessionManager.USER_ID,null));
        userDetails.put(Constants.SessionManager.PHONE_ID,pref.getString(Constants.SessionManager.PHONE_ID,null));
        userDetails.put(Constants.SessionManager.SIM_ID,pref.getString(Constants.SessionManager.SIM_ID,null));
        Log.d(TAG,"getPhoneDetails : exit");

        return userDetails;
    }

    /**
     * @author lkelly
     * @desc sets the IS_INITIAL_SETUP parameter
     * if value is greater than 1 then it is not inital
     * @params
     * @return newCount | int
     * */
    public int getLoginCountNum(){
        Log.d(TAG,"getLoginCountNum : init");

        int oldCount = pref.getInt(Constants.PhoneManager.LOGIN_COUNT_NUM,0);
        int newCount = oldCount + 1;

        editor.putInt(Constants.PhoneManager.LOGIN_COUNT_NUM,newCount);

        //commit changes to the editor
        editor.commit();
        Log.d(TAG,"Count : "+newCount);
        Log.d(TAG,"getLoginCountNum: exit");

        return newCount;
    }

}
