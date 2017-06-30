package com.omnivision.utilities;

import android.content.Context;
import android.os.Bundle;

import com.omnivision.core.Constants;
import com.omnivision.core.DaoSession;
import com.omnivision.core.GPSLocation;
import com.omnivision.core.Phone;
import com.omnivision.dao.IPartnerDeviceDao;
import com.omnivision.dao.PartnerDeviceDaoImpl;
import com.tablayoutexample.lkelly.omnivision.OmniLocateApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lodeane on 30/6/2017.
 */

public class NotificationUtility {
    private String TAG = NotificationUtility.class.getSimpleName();
    private static Context context = OmniLocateApplication.getInstance();
    private static DaoSession daoSession = OmniLocateApplication.getSession(context);
    private static SessionManager sessionManager = new SessionManager(context);
    private static Map<String,String> userDetails = sessionManager.getUserDetails();

    private NotificationUtility(){}

    /**
     * @author lkelly
     * @desc Sends location information through sms
     * @params
     * @return
     * */
    public static void smsLocationResponder(Bundle resultData, String recipient){
        HashMap<String, String> locationMap = (HashMap<String, String>) resultData.getSerializable(Constants.Location.RESULT_DATA_KEY);
        GPSLocation gpsLocation = new GPSLocation(locationMap.get(Constants.Location.LATITUDE), locationMap.get(Constants.Location.LONGITUDE));
        if (locationMap.get(Constants.Location.ADDRESS_LINE) != null) {
            gpsLocation.setAddressLine(locationMap.get(Constants.Location.ADDRESS_LINE));
        }

        SMSUtility smsUtility = new SMSUtility(SMSUtility.divideMessage(gpsLocation.toString(),gpsLocation.getGoogleMapsLink()), recipient, context);

        smsUtility.sendSMS();
    }

    /**
     * @author lkelly
     * @desc Sends location information through the web
     * @params
     * @return
     * */
    public static void webLocationResponder(){
        //TODO unimplemented method
    }

    /**
     * @author lkelly
     * @desc Broadcast general system change notifications through sms  i.e. sends messages
     *       by sms to the primary partner device
     * @params
     * @return
     * */
    public static void smsNotifBroadcast(String msg){
        Long phoneId = Long.parseLong(userDetails.get(Constants.SessionManager.PHONE_ID));
        IPartnerDeviceDao partnerDeviceDao = new PartnerDeviceDaoImpl(daoSession);
        String primaryPartnerRecipient = partnerDeviceDao.findPrimaryDevice(phoneId).getPartnerDeviceNum();

        SMSUtility smsUtility = new SMSUtility(SMSUtility.divideMessage(msg), primaryPartnerRecipient, context);
        smsUtility.sendSMS();
    }

    /**
     * @author lkelly
     * @desc Broadcast general system change notifications through web  i.e. sends system updates
     *       to web service
     * @params
     * @return
     * */
    public static void webNotifBroadcast(Map updateInformation){
        //TODO implement this method
    }
}
