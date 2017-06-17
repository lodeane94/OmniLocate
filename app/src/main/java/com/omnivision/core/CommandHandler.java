package com.omnivision.core;
import android.content.Intent;

import com.omnivision.utilities.SMSUtility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lkelly on 3/7/2017.
 */

public class CommandHandler implements ICommandHandler {
    private static String TAG = "CommandHandler";
    private static String command = "";

    private CommandHandler(){}

    /**
     * @author lkelly
     * @desc extracts any parameters that were sent with the code
     * @params command
     * @return Map containing the command sent and any parameter sent with it
     * */
    public static Map<String,String> getCommand(String command) {
        Map<String,String> cmdInfo = new HashMap<>();
        String cmd = command.trim().toLowerCase();
        String cmdName = cmd;
        String param = "";

        if(cmd.contains(":")){
            int colonIndex = cmd.indexOf(":");
            cmdName = cmd.substring(0,colonIndex);
            param = cmd.substring(colonIndex + 1,cmd.length());
        }

        cmdInfo.put(Constants.CommandDetails.COMMAND.name(),cmdName);
        cmdInfo.put(Constants.CommandDetails.CMD_PARAM.name(),param);
        return cmdInfo;
    }

    public static Boolean validateCommand(String command) {
        //String cmd = getCommand(command).get(Constants.CommandDetails.COMMAND.name());
        for(Constants.Commands comm : Constants.Commands.values()){
            if(comm.name().toLowerCase().contains(command)){
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
     public static Map<String,String> extractCommandFromSMS(Intent intent) {
         return SMSUtility.extractCommandFromSMS(intent);
     }
}
