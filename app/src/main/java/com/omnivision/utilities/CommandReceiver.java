package com.omnivision.utilities;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.omnivision.Services.CameraService;
import com.omnivision.Services.FetchAddressIntentService;
import com.omnivision.core.CommandHandler;
import com.omnivision.core.Constants;
import com.omnivision.core.Constants.IntentActions;
import com.omnivision.core.DaoSession;
import com.omnivision.core.GPSLocation;
import com.omnivision.core.PartnerDevice;
import com.omnivision.core.PartnerDeviceDao;
import com.omnivision.core.Phone;
import com.omnivision.core.PrepaidCredit;
import com.omnivision.dao.IPartnerDeviceDao;
import com.omnivision.dao.PartnerDeviceDaoImpl;
import com.tablayoutexample.lkelly.omnivision.OmniLocateApplication;
import com.tablayoutexample.lkelly.omnivision.R;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lkelly on 3/29/2017.
 */

public class CommandReceiver extends BroadcastReceiver{
    public static final String SMS_RECEIVED_COMMAND = "com.omnisecurity.intent.action.SMS_RECEIVED_COMMAND";
    public static final int MISSING_DEVICE_REQUEST_CODE = 1;

    private final String TAG = "CommandReceiver";
    private LocationLookUpReceiver mResultReceiver;
    private Map<String,String> commandDetails;
    private Context context;
    private Intent intent;
    private DaoSession daoSession;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails;

    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        * context and intent  variables are necessary to provide
        * these information to the entire class body*/
        this.context = context;
        this.intent = intent;
        daoSession = OmniLocateApplication.getSession(context);
        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getUserDetails();
        try {
            switch (intent.getAction()) {
                case IntentActions.SMS_RECEIVED:
                    Log.d(TAG,"SMS was received | will attempt to extract command from sms");
                    commandDetails = CommandHandler.extractCommandFromSMS(intent);
                    actionCommand();
                    break;
                case IntentActions.SMS_SENT:
                    Log.d(TAG,"SMS was sent");
                    break;
                case IntentActions.SMS_DELIVERED:
                    Log.d(TAG,"SMS was delivered");
                    break;
                case IntentActions.MISSING_DEVICE_ALARM_STARTED:
                    Log.d(TAG,"Missing device alarm has been triggered");
                    //initiateMissingDeviceProtocols();
                    findCommand();
                    break;
                case IntentActions.USSD_RESULTS:
                    Log.d(TAG,"ussd results received");
                    String result = intent.getStringExtra(Constants.IntentActions.USSD_RESULTS);
                    //TODO :(DELAYED) implement functionality that will be used to learn and respond to events from ussd commands
                    /*if(LoginActivity.getInstance() != null)
                        LoginActivity.getInstance().updateUI(result);*/
                    break;
                case Intent.ACTION_SCREEN_ON:
                    Log.d(TAG,"action screen on intent received");
                    captureSilentPhoto(context);
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    Log.d(TAG,"action screen off intent received");
                    break;
                case Intent.ACTION_USER_PRESENT:
                    Log.d(TAG,"lock screen present");


                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @author lkelly
     * @desc Checks the status of the device and respond accordingly:
     *       if device is lost then only poll gps coordinates and return results to the web
     *       if network is available or to the primary partner device
     *
     *       if device is stolen then  poll gps coordinates and return results to the web
     *       if network is available or to the primary partner device and start service that
     *       will capture the potential thief's face or surroundinga
     * @params
     * @return
     *
    private void initiateMissingDeviceProtocols() {
        Phone phone = OmniLocateApplication.getPhoneInstance();
        String status = phone.getDeviceStatus();

        switch (status){
            case Constants.DeviceStatus.LOST:
                findCommand();
                break;
            case Constants.DeviceStatus.STOLEN:
                findCommand();
                break;
        }
    }*/

    /**
     * @author lkelly
     * @desc Action the command that was received
     * @params
     * @return
     * */
    private void actionCommand() throws Exception {
        Map<String,String> cmdInfo = CommandHandler
                .getCommand(commandDetails.get(Constants.CommandDetails.COMMAND.name()));

        String command = cmdInfo.get(Constants.CommandDetails.COMMAND.name());
        String param = cmdInfo.get(Constants.CommandDetails.CMD_PARAM.name());
        //checking if commandDetails map is properly formed
        if (!commandDetails.containsKey(Constants.CommandDetails.COMMAND.name())
                && !commandDetails.containsKey(Constants.CommandDetails.ORIGIN.name())) {
            throw new Exception("Each command map must contain a command and and origin");
        }

        if (CommandHandler.validateCommand(command)
                && Phone.isAPartnerDeviceNumber(commandDetails.get(Constants.CommandDetails.ORIGIN.name()))) {
            Log.i(TAG, "command = " + commandDetails.get(Constants.CommandDetails.COMMAND.name()) + " + origin = " + commandDetails.get(Constants.CommandDetails.ORIGIN.name()) + " is valid");

            if (command.equals(Constants.Commands.FIND.toString())) {//FIND COMMAND
                findCommand();
            }
            if (command.equals(Constants.Commands.LOST.toString())) {//LOST COMMAND
                lostCommand();
            }
            if (command.equals(Constants.Commands.STOLEN.toString())) {//STOLEN COMMAND
                stolenCommand();
            }
            if (command.equals(Constants.Commands.SIM_CHANGE.toString())) {//SIM_CHANGE COMMAND
                //TODO
            }
            if (command.equals(Constants.Commands.WIPE.toString())) {//WIPE COMMAND
                //TODO
            }
            if (command.equals(Constants.Commands.ADD_CREDIT.toString())) {//ADD_CREDIT COMMAND
                addCreditCommand(param);
            }
            if (command.equals(Constants.Commands.CHECK_CREDIT.toString())) {//CHECK_CREDIT COMMAND
                //TODO
            }
            if (command.equals(Constants.Commands.ACTIVATE_CREDIT.toString())) {//ACTIVATE_CREDIT COMMAND
                activateCreditCommand();
            }

        }

    }

    /**
     * @author lkelly
     * @desc marks phone's status as stolen and initiate operations for that status
     * @params
     * @return
     * */
    private void stolenCommand() throws Exception {
        CommandUtility.initiateStolenDeviceProtocol();
    }

    /**
     * @author lkelly
     * @desc marks phone's status as lost and initiate operations for that status
     * @params
     * @return
     * */
    private void lostCommand() throws Exception {
        CommandUtility.initiateLostDeviceProtocol();
    }

    /**
     * @author lkelly
     * @desc method is used to add prepaid credit information to the application
     * @params voucher number
     * @return
     * */
    private void addCreditCommand(String voucherNumber) {
        try {
            PrepaidCredit prepaidCredit = new PrepaidCredit(voucherNumber, commandDetails.get(Constants.CommandDetails.ORIGIN.name()),
                    Long.valueOf(userDetails.get(Constants.SessionManager.SIM_ID)), context);

            CommandUtility.addCredit(prepaidCredit);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void activateCreditCommand(){
        try {
            CommandUtility.activateCredit();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void findCommand() {
        // start the service to fetch the address
        try {
            startFetchAddressIntentService(NetworkManager.hasNetworkConnection(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @author lkelly
     * @desc starts the FetchAddressIntentService
     * @params
     * @return
     * */
    protected void startFetchAddressIntentService(boolean hasNetworkConnection) throws Exception {
        mResultReceiver = new LocationLookUpReceiver(new Handler());//receiver will be used for the callback of the location request

        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putExtra(Constants.Location.RECEIVER, mResultReceiver);
        //intent.putExtra(Constants.Location.LOCATION_DATA_EXTRA, mLastLocation);
        intent.putExtra(Constants.NetworkConnection.CONNECTION_AVAILABILITY.name(),hasNetworkConnection);
        //intent.putExtra(Constants.General.CONTEXT.name(), (Parcelable) this.context);
        context.startService(intent);
    }
    /**
     * @author lkelly
     * @desc determines which medium to respond using either sms or through the web
     * @params
     * @return
     * */
    private String responseMechanism(){
        switch (intent.getAction()) {
            case IntentActions.SMS_RECEIVED:
                return Constants.CommandIssuerMechanism.SMS.name();
            case IntentActions.MISSING_DEVICE_ALARM_STARTED :
                return Constants.CommandIssuerMechanism.SMS_WEB.name();
        }
        return null;
    }
    /**
     * @author lkelly
     * @desc calls service the will silently capture a photo using the front facing
     *       camera if phone's status is stolen
     * @params
     * @return
     * */
    private static void captureSilentPhoto(Context context){
        Phone phone = OmniLocateApplication.getPhoneInstance();
        if(phone.getDeviceStatus().equals(Constants.DeviceStatus.STOLEN)){
            Intent camServiceIntent = new Intent(context,CameraService.class);
            context.startService(camServiceIntent);
        }
    }
    //temp class
    //TODO this class is to be removed and move logic to the onReceive method of the commandreceiver class
    protected class LocationLookUpReceiver extends ResultReceiver {

        public LocationLookUpReceiver(Handler handler) {
            super(handler);
        }

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData){
            /*
            * based on which medium requested information about the device
            * respond accordingly*/
            if(responseMechanism() != null && resultCode == Constants.Location.SUCCESS_RESULT) {
                if(responseMechanism().equals(Constants.CommandIssuerMechanism.SMS.name())) {
                    String recipient = commandDetails.get(Constants.CommandDetails.ORIGIN.name());
                    smsResponder(resultData,recipient);
                }
                if(responseMechanism().equals(Constants.CommandIssuerMechanism.WEB.name())) {
                    //TODO implement response for the web
                }
                if(responseMechanism().equals(Constants.CommandIssuerMechanism.SMS_WEB.name())) {
                    Long phoneId = Long.parseLong(userDetails.get(Constants.SessionManager.PHONE_ID));

                    IPartnerDeviceDao partnerDeviceDao = new PartnerDeviceDaoImpl(daoSession);
                    QueryBuilder<PartnerDevice> queryBuilder = partnerDeviceDao.findByQuery();
                    queryBuilder.where(PartnerDeviceDao.Properties.IsPrimaryFlag.eq(true),
                            PartnerDeviceDao.Properties.PhoneId.eq(phoneId));

                    //String primaryPartnerRecipient = queryBuilder.unique().getPartnerDeviceNum();
                    //smsResponder(resultData,primaryPartnerRecipient);
                    //webResponder();TODO call this function here
                }
            }else{
                try {
                    String message = "Failure retrieving location from the device";
                    Log.e(TAG, message);//TODO attempt to request information for a maximum of three times then throw error message
                    throw new Exception(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        /**
         * @author lkelly
         * @desc Sends location information through sms
         * @params
         * @return
         * */
        private void smsResponder(Bundle  resultData,String recipient){
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
        private void webResponder(){
            //TODO unimplemented method
        }
    }
    //TODO implement in future builds
    /*
    public final class AdminReceiver extends DeviceAdminReceiver{
        @Override
        public void onPasswordFailed(Context ctx, Intent intent){
            CommandReceiver.captureSilentPhoto(ctx);
        }

        @Override
        public void onPasswordSucceeded(Context ctx, Intent intent){
            CommandReceiver.captureSilentPhoto(ctx);
        }
    }*/
}
