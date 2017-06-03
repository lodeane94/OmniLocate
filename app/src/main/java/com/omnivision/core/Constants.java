package com.omnivision.core;

/**
 * Created by lkelly on 3/16/2017.
 */

public class Constants {
    public static String CommandString = "COMMAND";//command string constant
    public enum DeviceStatus {STOLEN,LOST,NOT_STOLEN}
    public enum CommandStatus {VALID,NOT_VALID}
    public enum CommandDetails{COMMAND,ORIGIN}
    public enum NetworkConnection{CONNECTION_AVAILABILITY}
    public enum NetworkConnectionType{WIFI,MOBILE}
    public enum General{RECEIVER_RESULT_MESSAGE,YES,NO}
    public enum CommandIssuerMechanism{SMS,WEB}
    public enum Commands {
        STOLEN,LOST,FIND,WIPE,ADD_CREDIT,CHECK_CREDIT,SIM_CHANGE;

        @Override
        public String toString(){
            return this.name().toLowerCase();
        }
    }
    public enum Permissions{
        //Define constants for each permission request
        SMS_REQUEST_CODE(1),
        ACCESS_FINE_LOCATION_REQUEST_CODE(2),
        CALL_PHONE_REQUEST_CODE(3);
        private int code;

        Permissions(int code){
            this.code = code;
        }

        public int getCode(){return this.code;}
    }


    public static final class Navigation{
        public static final String HOME = "HOME";
        public static final String PARTNER_DEVICES = "PARTNER_DEVICES";
        public static final String PREPAID_CREDITS = "PREPAID_CREDITS";
        public static final String COMMAND_HISTORY = "COMMAND_HISTORY";
        public static final String DEVICE_WIPE = "DEVICE_WIPE";

        public static final String icon= "icon";
        public static final String title= "title";

        public static final class NavigationPositions{
            public static final int HOME = 0;
            public static final int PARTNER_DEVICES = 1;
            public static final int PREPAID_CREDITS = 2;
            public static final int COMMAND_HISTORY = 3;
            public static final int DEVICE_WIPE = 4;
        }
    }

    public static final class IntentActions{
        public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
        public static final String SMS_SENT = "SMS_SENT";
        public static final String SMS_DELIVERED = "SMS_DELIVERED";
        public static  final String USSD_RESULTS = "USSD_RESULTS";
    }

    public static final class Location{
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME = "com.omnivision.core";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
        public static final String ADDRESS_FOUND = "Address Found";
        public static  final String LATITUDE = "Latitude";
        public static final String LONGITUDE = "Longitude";
        public static final String ADDRESS_LINE = "ADDRESS_LINE_1";

        public enum ErrorMessages{
            SERVICE_NOT_AVAILABLE("Nerwork Not Available"),
            INVALID_COORODINATES("Invalid Coordinates"),
            ADDRESS_NOT_FOUND("Address Not Found");
            private String message;

            ErrorMessages(String message){
                this.message = message;
            }

            public String getMessage(){
                return this.message;
            }
        }
    }

    public static final class PrepaidCredit{
        public static final String ADDED_BY = "ADDED_BY";
        public static final String IS_USED = "IS_USED";
        public static final String DATE_ADDED = "DATE_ADDED";
    }

    public static final class SessionManager{
        //sharedpref file name
        public static final String PREF_NAME = "OMNI_LOCATE_SESSION_PREF";
        public static final String IS_LOGIN = "IS_LOGGED_IN";
        public static final String USER_ID = "USER_ID";
        public static final String EMAIL = "EMAIL";
        public static final String PHONE_ID = "PHONE_ID";
        public static final String SIM_ID = "SIM_ID";
        public static final int PRIVATE_MODE = 0;
    }

    public static final class PhoneManager {
        //sharedpref file name
        public static final String PREF_NAME = "OMNI_LOCATE_PHONE_PREF";
    }

    //Identify the response given from an alert dialog
    public static final class AlertDialog{
        public static final String POSITVE_SELECTED = "POSITVE_SELECTED";
        public static final String NEGATIVE_SELECTED = "NEGATIVE_SELECTED";
    }

}