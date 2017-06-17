package com.omnivision.utilities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.omnivision.core.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @Created by lkelly on 3/29/2017.
 */

public class SMSUtility {
    private static String TAG = "SMSUtility";
    private ArrayList<String> sms;
    private String sender;
    private String receiver;
    private Context context;

    public SMSUtility(ArrayList<String> sms, String sender){
        this.sms = sms;
        this.sender = sender;
    }

    public SMSUtility(ArrayList<String> sms, String receiver, Context context){
        this.sms = sms;
        this.receiver = "15555215554";//receiver;
        this.context = context;
    }

    public ArrayList<String> getSms() {
        return sms;
    }

    public void setSms(ArrayList<String> sms) {
        this.sms = sms;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getreceiver() {
        return receiver;
    }
    public void setreceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * @author lkelly
     * @desc extracts data from sms text
     * @params intent
     * @return map containing sms that was send and the sender of the sms
   * */
    public static Map<String,String> extractCommandFromSMS(Intent intent) {
        Log.i(TAG,"extractCommandFromSMS:init");

        String smsBody = "";
        String smsSender = "";
        Map smsDetails = new HashMap();
        Bundle bundle = intent.getExtras();
        Object[] objArr = (Object[])bundle.get("pdus");


        for(int i = 0; i < objArr.length; i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])objArr[i]);

            smsBody = smsMessage.getMessageBody();
            smsSender = smsMessage.getDisplayOriginatingAddress();

            smsDetails.put(Constants.CommandDetails.COMMAND.name(),smsBody);
            smsDetails.put(Constants.CommandDetails.ORIGIN.name(),smsSender);
        }

        Log.i(TAG,"extractCommandFromSMS:exit");
        return smsDetails;
    }

    /**
     * @author lkelly
     * @desc sends sms messages receiver devices
     * @params
     * @return
     * */
    public void sendSMS(){
        Log.i(TAG,"sendSMS:init");

        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent sentIntent = PendingIntent.getBroadcast(context,0,new Intent(Constants.IntentActions.SMS_SENT),0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(context,0,new Intent(Constants.IntentActions.SMS_DELIVERED),0);

        for (String msg: sms) {
            Log.d(TAG,"Sending SMS = "+sms);
            smsManager.sendTextMessage(receiver,null,msg,sentIntent,deliveredIntent);
        }

        Log.i(TAG,"sendSMS:exit");
    }

    /**
     * @author lkelly
     * @desc divides the sms text into multiple text as to prevent truncation (limited to 60 character)
     * @params
     * @return arraylist of containing the divided messages
     * */
    public static ArrayList<String> divideMessage(String msg){
        Log.i(TAG,"divideMessage:init");

        double smsBoundary = 159;
        int lastIndex = 0;
        double divisionAmt = 0;
        double msgLength = msg.length();
        double mod = msgLength / smsBoundary;
        int remainingLength = 0;
        ArrayList<String> splStr = new ArrayList<>();

        if(msg.length() <= smsBoundary) {
            splStr.add(msg);
            Log.i(TAG,"Messages will be split into "+splStr.size()+" parts");
            return splStr;
        }

        divisionAmt = Math.ceil(mod);

        for (int i = 0; i < divisionAmt; i++){
            if(i == 0 ){
                /*if message is longer than the sms boundary then  split message at the boundary*/
                if( msgLength > smsBoundary) {
                    splStr.add(msg.substring(lastIndex, (int)smsBoundary));
                }
                else{
                    splStr.add(msg);
                }
                remainingLength =  msg.length();
                lastIndex += smsBoundary;
                continue;
            }

            remainingLength -= smsBoundary;
            splStr.add(msg.substring(lastIndex,(lastIndex+remainingLength)));

            lastIndex += smsBoundary;
            Log.i(TAG,"Messages will be split into "+splStr.size()+" parts");
        }
        Log.i(TAG,"divideMessage:exit");
        return splStr;
    }

    /**
     * @author lkelly
     * @desc divides the sms text to ensure that the a separate message will be sent as an attachment to the original message
     * @params
     * @return ArrayList of containing the divided messages
     * */
    public static ArrayList<String> divideMessage(String msg, String separateMsg){
        Log.i(TAG,"divideMessage:init");

        double smsBoundary = 159;
        int lastIndex = 0;
        double divisionAmt = 0;
        int remainingLength = 0;
        ArrayList<String> splStr = new ArrayList<>();
        String[] splitMsg = msg.split("Maps\n");

        String msgLessGPS = splitMsg[0];
        double msgLength = msgLessGPS.length();
        double mod = msgLength / smsBoundary;

        if(msg.length() <= smsBoundary) {
            splStr.add(msg);
            Log.i(TAG,"Messages will be split into "+splStr.size()+" parts");
            return splStr;
        }

        divisionAmt = Math.ceil(mod);

        for (int i = 0; i < divisionAmt; i++){
            if(i == 0 ){
                /*if message is longer than the sms boundary then  split message at the boundary*/
                if( msgLength > smsBoundary) {
                    splStr.add(msgLessGPS.substring(lastIndex, (int)smsBoundary));
                }
                else{
                    splStr.add(msgLessGPS);
                }
                remainingLength =  msgLessGPS.length();
                lastIndex += smsBoundary;
                continue;
            }
            remainingLength -= smsBoundary;
            splStr.add(msgLessGPS.substring(lastIndex,(lastIndex+remainingLength)));

            lastIndex += smsBoundary;
            Log.i(TAG,"Messages will be split into "+splStr.size()+" (+ 1) parts");
        }
        splStr.add(separateMsg);
        Log.i(TAG,"divideMessage:exit");
        return splStr;
    }

}
