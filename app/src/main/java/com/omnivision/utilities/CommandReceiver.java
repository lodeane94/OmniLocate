package com.omnivision.utilities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;
import com.omnivision.core.CommandHandler;
import com.omnivision.core.Constants;
import com.omnivision.core.Constants.IntentActions;
import com.omnivision.core.GPSLocation;
import com.omnivision.core.Phone;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lkelly on 3/29/2017.
 */

public class CommandReceiver extends BroadcastReceiver{
    public static final String SMS_RECEIVED_COMMAND = "com.omnisecurity.intent.action.SMS_RECEIVED_COMMAND";
    private final String TAG = "CommandReceiver";
    private LocationLookUpReceiver mResultReceiver;
    private Map commandDetails;
    private Context context;
    private Intent intent;

    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        * context and intent  variables are necessary to provide
        * these information to the entire class body*/
        this.context = context;
        this.intent = intent;
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
                case IntentActions.USSD_RESULTS:
                    Log.d(TAG,intent.getStringExtra("message"));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actionCommand() throws Exception {
        String command = commandDetails.get(Constants.CommandDetails.COMMAND).toString().trim().toLowerCase();
        //checking if commandDetails map is properly formed
        if (!commandDetails.containsKey(Constants.CommandDetails.COMMAND)
                && !commandDetails.containsKey(Constants.CommandDetails.ORIGIN)) {
            throw new Exception("Each command map must contain a command and and origin");
        }

        if (CommandHandler.validateCommand(command)
                && Phone.isAPartnerDeviceNumber((String) commandDetails.get(Constants.CommandDetails.ORIGIN))) {
            Log.i(TAG, "command = " + commandDetails.get(Constants.CommandDetails.COMMAND) + " + origin = " + commandDetails.get(Constants.CommandDetails.ORIGIN) + " is valid");

            if (command.equals(Constants.Commands.FIND.toString())) {//FIND COMMAND
                findCommand();
            }
            if (command.equals(Constants.Commands.LOST.toString())) {//LOST COMMAND
                //TODO
            }
            if (command.equals(Constants.Commands.STOLEN.toString())) {//STOLEN COMMAND
                //TODO
            }
            if (command.equals(Constants.Commands.SIM_CHANGE.toString())) {//SIM_CHANGE COMMAND
                //TODO
            }
            if (command.equals(Constants.Commands.WIPE.toString())) {//WIPE COMMAND
                //TODO
            }
            if (command.equals(Constants.Commands.ADD_CREDIT.toString())
                    || command.equals(Constants.Commands.ADD_CREDIT.toString().replace('_',' '))) {//ADD_CREDIT COMMAND
                //TODO
            }
            if (command.equals(Constants.Commands.CHECK_CREDIT.toString())) {//CHECK_CREDIT COMMAND
                //TODO
            }

        }

    }

    private void findCommand() {
     /*   mGoogleApiclient =
                new GoogleApiClient.Builder(this.context)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).addApi(Plus.API)
                        .build();*/

        // Only start the service to fetch the address if GoogleApiClient is
        // connected.
       // if (mGoogleApiclient.isConnected() && mLastLocation != null) {
            startFetchAddressIntentService(NetworkManager.hasNetworkConnection(context));
      //  }
        // If GoogleApiClient isn't connected, process the user's request by
        // setting mAddressRequested to true. Later, when GoogleApiClient connects,
        // launch the service to fetch the address. As far as the user is
        // concerned, pressing the Fetch Address button
        // immediately kicks off the process of getting the address.
   //     mAddressRequested = true;
    }


    /**
     * @author lkelly
     * @desc starts the FetchAddressIntentService
     * @params
     * @return
     * */
    protected void startFetchAddressIntentService(boolean hasNetworkConnection) {
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
        }
        return null;
    }
/*
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Gets the best and most recent location currently available,
        // which may be null in rare cases when a location is not available
        PermissionManager.check((Activity) context, android.Manifest.permission.ACCESS_FINE_LOCATION, Constants.Permissions.ACCESS_FINE_LOCATION_REQUEST_CODE.getCode());

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //TODO throw exception permission not allowed
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiclient);

        if(mLastLocation != null){
            //determine whether a geocoder is available
            if(!Geocoder.isPresent()){
                //TODO throw exception for the prescence of the geocoder
                return;
            }

            if(mAddressRequested){
                startFetchAddressIntentService(NetworkManager.hasNetworkConnection(context));
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }*/
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
            if(responseMechanism() != null) {
                if(responseMechanism() == Constants.CommandIssuerMechanism.SMS.name()) {
                    if (resultCode == Constants.Location.SUCCESS_RESULT) {
                        String receiver = (String) commandDetails.get(Constants.CommandDetails.ORIGIN);
                        HashMap<String, String> locationMap = (HashMap<String, String>) resultData.getSerializable(Constants.Location.RESULT_DATA_KEY);
                        GPSLocation gpsLocation = new GPSLocation(locationMap.get(Constants.Location.LATITUDE), locationMap.get(Constants.Location.LONGITUDE));
                        if (locationMap.get(Constants.Location.ADDRESS_LINE) != null) {
                            gpsLocation.setAddressLine(locationMap.get(Constants.Location.ADDRESS_LINE));
                        }

                        SMSUtility smsUtility = new SMSUtility(SMSUtility.divideMessage(gpsLocation.toString(),gpsLocation.getGoogleMapsLink()), receiver, context);

                        smsUtility.sendSMS();
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
            }
        }
    }
}
