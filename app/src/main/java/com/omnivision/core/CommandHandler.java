package com.omnivision.core;
import android.content.Intent;

import com.omnivision.utilities.SMSUtility;

import java.util.Map;

/**
 * Created by lkelly on 3/7/2017.
 */

public class CommandHandler implements ICommandHandler {
    private static String TAG = "CommandHandler";
    private static String command = "";

    private CommandHandler(){}

    public static String getCommand() {
        return command;
    }

    public static void setCommand(String command) {
        CommandHandler.command = command;
    }

    public static Boolean validateCommand(String command) {
        for(Constants.Commands comm : Constants.Commands.values()){
            if(comm.name().toLowerCase().equals(command.toLowerCase())){
                return  true;
            }
        }
        return false;
    }

    /**
     * @author lkelly
     * @desc calls function from smsservice class that extracts data from sms text
     * @params intent
     * @return map containing sms that was send and the sender of the sms
     * */
     public static Map extractCommandFromSMS(Intent intent) {
         return SMSUtility.extractCommandFromSMS(intent);
     }
}
