package com.omnivision.Services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.omnivision.core.Constants;
import com.omnivision.utilities.CommandReceiver;

/**
 * Created by Lodeane on 3/5/2017.
 */

public class USSDService extends AccessibilityService {
    String TAG = "USSDService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG,"onAccessibilityEvent: init");
        String text = event.getText().toString();

        if(event.getClassName().equals("android.app.AlertDialog")){
            performGlobalAction(GLOBAL_ACTION_BACK);
            Log.d(TAG,text);

            Intent intent = new Intent(this,CommandReceiver.class);
            intent.setAction(Constants.IntentActions.USSD_RESULTS);
            intent.putExtra(Constants.IntentActions.USSD_RESULTS,text);
            sendBroadcast(intent);
        }
        Log.d(TAG,"onAccessibilityEvent: exit");
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onServiceConnected(){
        super.onServiceConnected();
        Log.d(TAG,"onServiceConnected: init");

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.packageNames = new String[]{"com.android.phone"};
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL;
        setServiceInfo(info);
        Log.d(TAG,"onServiceConnected: exit");
    }
}
