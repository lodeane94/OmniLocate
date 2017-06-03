package com.omnivision.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.omnivision.core.Constants;
import com.tablayoutexample.lkelly.omnivision.LoginActivity;

import java.util.HashMap;

/**
 * Created by Lodeane on 24/5/2017.
 */
//TODO make into singleton class
public class SessionManager {
    private String TAG = SessionManager.class.getSimpleName();
    //shared preferences
    private SharedPreferences pref;
    //editor for the shared preferences
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context){
        this.context = context;
        pref = this.context.getSharedPreferences(Constants.SessionManager.PREF_NAME,Constants.SessionManager.PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * @author lkelly
     * @desc creates login session
     * @params
     * @return
     * */
    public void createLoginSession(String id,String email,String phoneId, String simCardId){
        Log.d(TAG,"createLoginSession : init");
        //setting isLogin pref to true
        editor.putBoolean(Constants.SessionManager.IS_LOGIN,true);
        editor.putString(Constants.SessionManager.USER_ID,id);
        editor.putString(Constants.SessionManager.EMAIL,email);
        editor.putString(Constants.SessionManager.PHONE_ID,phoneId);
        editor.putString(Constants.SessionManager.SIM_ID,simCardId);
        //commit changes to the editor
        editor.commit();
        Log.d(TAG,"ID : "+id+" Email : "+email);
        Log.d(TAG,"createLoginSession : exit");
    }

    /**
     * @author lkelly
     * @desc logs user out of session
     * @params
     * @return
     * */
    public void logOut(){
        Log.d(TAG,"logOut : init");
        //clearing all data from shared preference
        editor.clear();
        editor.commit();

        Intent i = new Intent(this.context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);

        Log.d(TAG,"logOut : exit");
    }

    /**
     * @author lkelly
     * @desc gets stored session details on the user
     * @params
     * @returns boolean
     * */
    public boolean isLoggedIn(){
        return pref.getBoolean(Constants.SessionManager.IS_LOGIN,false);
    }

    /**
     * @author lkelly
     * @desc gets stored session details on the user
     * @params
     * @returns map<USER_ID,EMAIL,PHONE_ID>
     * */
    public HashMap<String,String> getUserDetails(){
        Log.d(TAG,"getUserDetails : init");
        HashMap userDetails = new HashMap<String,String>();
        userDetails.put(Constants.SessionManager.USER_ID,pref.getString(Constants.SessionManager.USER_ID,null));
        userDetails.put(Constants.SessionManager.EMAIL,pref.getString(Constants.SessionManager.EMAIL,null));
        userDetails.put(Constants.SessionManager.PHONE_ID,pref.getString(Constants.SessionManager.PHONE_ID,null));
        userDetails.put(Constants.SessionManager.SIM_ID,pref.getString(Constants.SessionManager.SIM_ID,null));
        Log.d(TAG,"getUserDetails : exit");

        return userDetails;
    }


}
