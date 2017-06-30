package com.omnivision.utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.omnivision.core.Constants;
import com.omnivision.core.DaoSession;
import com.omnivision.core.Phone;
import com.omnivision.core.PhoneDao;
import com.omnivision.core.PrepaidCredit;
import com.omnivision.core.PrepaidCreditDao;
import com.omnivision.core.SimCardChangeHistory;
import com.omnivision.dao.IPhoneDao;
import com.omnivision.dao.IPrepaidCreditDao;
import com.omnivision.dao.ISimCardChangeHistDao;
import com.omnivision.dao.PhoneDaoImpl;
import com.omnivision.dao.PrepaidCreditDaoImpl;
import com.omnivision.dao.SimCardChangeHistDaoImpl;
import com.tablayoutexample.lkelly.omnivision.OmniLocateApplication;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lodeane on 17/6/2017.
 */

public class CommandUtility {
    private static String TAG = CommandUtility.class.getSimpleName();
    private static Context context = OmniLocateApplication.getInstance().getApplicationContext();
    private static DaoSession daoSession = OmniLocateApplication.getSession(context);
    private static SessionManager sessionManager = new SessionManager(context);
    private static Map<String,String> userDetails = sessionManager.getUserDetails();

    private static IPrepaidCreditDao prepaidCreditDao = new PrepaidCreditDaoImpl(daoSession);

    private CommandUtility(){}

    /**
     * @author lkelly
     * @desc adds prepaid credit to the application's database
     * @params prepaidCredit
     * @return
     * */
    public static void addCredit(PrepaidCredit prepaidCredit) throws Exception{
        prepaidCreditDao.insert(prepaidCredit);
    }

    /**
     * @author lkelly
     * @desc activates unused prepaid credit on the device
     * @params
     * @return
     * */
    public static void activateCredit() throws Exception{
        prepaidCreditDao = new PrepaidCreditDaoImpl(daoSession);

        QueryBuilder<PrepaidCredit> queryBuilder = prepaidCreditDao.findByQuery();
        queryBuilder.where(PrepaidCreditDao.Properties.IsUsed.eq(false)).limit(1);
        PrepaidCredit prepaidCredit = queryBuilder.unique();

        if(prepaidCredit != null)
            DialingHandler.dial(prepaidCredit.getVoucherNum(),context);
    }

    /**
     * @author lkelly
     * @desc activates selected prepaidcredit on the device
     * @params voucherNumber
     * @return
     * */
    public static void activateCredit(String voucherNumber) throws Exception{
        DialingHandler.dial(voucherNumber,context);
    }

    /**
     * @author lkelly
     * @desc updates phone's status to lost
     * @params
     * @return
     * */
    public static void initiateLostDeviceProtocol() throws Exception{
        //Long ownerId = Long.parseLong(userDetails.get(Constants.SessionManager.PHONE_ID));
        Phone phone = OmniLocateApplication.getPhoneInstance();
        IPhoneDao phoneDao = new PhoneDaoImpl(daoSession);
        phone.setDeviceStatus(Constants.DeviceStatus.LOST);

        phoneDao.update(phone);

        triggerMissingDeviceAlarm();
    }

    /**
     * @author lkelly
     * @desc updates phone's status to stolen
     * @params
     * @return
     * */
    public static void initiateStolenDeviceProtocol() throws Exception{
        //Long ownerId = Long.parseLong(userDetails.get(Constants.SessionManager.PHONE_ID));
        Phone phone = OmniLocateApplication.getPhoneInstance();
        IPhoneDao phoneDao = new PhoneDaoImpl(daoSession);
        phone.setDeviceStatus(Constants.DeviceStatus.STOLEN);

        phoneDao.update(phone);

        triggerMissingDeviceAlarm();

        registerLockScreenIntents();
    }

    /**
     * @author lkelly
     * @desc protocol is to send the new sim's information to the primary partner device
     *       and to update the web portal with this new information
     * @params
     * @return
     * */
    public static void initiateSimChangedProtocol() throws Exception{
       // Long ownerId = Long.parseLong(userDetails.get(Constants.SessionManager.PHONE_ID));
        Phone phone = OmniLocateApplication.getPhoneInstance();
        IPhoneDao phoneDao = new PhoneDaoImpl(daoSession);
        ISimCardChangeHistDao simCardChangeHistDao = new SimCardChangeHistDaoImpl(daoSession);

        String simSerialNum = findSimSerial();
        String simNumber = findSimNumber();
        /*sim number is not always available hence mechanism will be provided
        * to update this record by the user*/
        if (simNumber == null) simNumber = "0000000";

        SimCardChangeHistory simCardChangeHistory = new SimCardChangeHistory(phone.getId(),simSerialNum,simNumber);
        simCardChangeHistDao.insert(simCardChangeHistory);
        //TODO: if settings is set to activate reserved credit upon sim change, then activate credit
        activateCredit();

        String modNotifMsg = Constants.SystemNotifMessages.SIMCARD_CHANGED + "\n New number registered: "+ simNumber;
        NotificationUtility.smsNotifBroadcast(modNotifMsg);
        //NotificationUtility.webNotifBroadcast();
    }

    /**
     * @author lkelly
     * @desc find the serial number registered to the simcard
     * @params
     * @return
     * */
    public static String findSimSerial() throws Exception{
        //receiving simcard serial number
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(context.TELECOM_SERVICE);
        return telephonyManager.getSimSerialNumber();
    }


    /**
     * @author lkelly
     * @desc find the simcard number registered to the simcard
     * @params
     * @return
     * */
    public static String findSimNumber() throws Exception{
        //receiving simcard number
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(context.TELECOM_SERVICE);
        return telephonyManager.getLine1Number();
    }

    /**
     * @author lkelly
     * @desc starts and repeat the activities that should be executed if the device is missing
     * @params
     * @return
     * */
    private static void triggerMissingDeviceAlarm() throws Exception{
        Intent intent = new Intent(context,CommandReceiver.class);
        intent.setAction(Constants.IntentActions.MISSING_DEVICE_ALARM_STARTED);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context,CommandReceiver.MISSING_DEVICE_REQUEST_CODE,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //starting time before alarm is fired is set to current second
        long firstMilSec = System.currentTimeMillis();
        long interval_one_minute = 60 * 1000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        //TODO create shared preference for device configurations to use user settings on the intervals in which alarm should be fired
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,firstMilSec,interval_one_minute,pendingIntent);
        Log.d(TAG,"missing device alarm started");
    }

    /**
     * @author lkelly
     * @desc cancels the alarm that should be executed if the device is missing
     * @params
     * @return
     * */
    private static void cancelMissingDeviceAlarm() throws Exception{
        Intent intent = new Intent(context,CommandReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context,CommandReceiver.MISSING_DEVICE_REQUEST_CODE,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.d(TAG,"missing device alarm cancelled");
    }

    public static void registerLockScreenIntents(){
        CommandReceiver commandReceiver = new CommandReceiver();

        IntentFilter lockIntentFilter = new IntentFilter();
        lockIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        lockIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        lockIntentFilter.addAction(Intent.ACTION_USER_PRESENT);

        context.registerReceiver(commandReceiver,lockIntentFilter);
    }
/*
    public static void deregisterLockScreenIntents(){
        CommandReceiver commandReceiver = new CommandReceiver();

        IntentFilter lockIntentFilter = new IntentFilter();
        lockIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        lockIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        lockIntentFilter.addAction(Intent.ACTION_USER_PRESENT);

        context.unregisterReceiver().registerReceiver(commandReceiver,lockIntentFilter);
    }*/

}
