package com.omnivision.utilities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Lodeane on 3/5/2017.
 */

public class DialingHandler {
    private static String TAG = "DialingHandler";

    /**
     * @author lkelly
     * @desc uses the default phone app to dial a specific numbercontext
     *
     * @params num,  @return
     * */
    public static void dial(String num, Context context) {
        Log.d(TAG, "dial number" + num + ": init");
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sanitizeNumber(num)));

        Log.d(TAG, "dial number" + num + ": exit");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(intent);
    }

    /**
     * @author lkelly
     * @desc replaces the # with the encoded version of the character
     * @params num
     * @return String containing the sanitized number
     * */
    private static String sanitizeNumber(String num) {
        return num.replace("#", Uri.encode("#"));
    }
}
