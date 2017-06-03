package com.omnivision.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tablayoutexample.lkelly.omnivision.R;


/**
 * Created by Lodeane on 31/5/2017.
 * Helper method is used for the invoking of the alert dialog
 * across all classes
 */

public class AlertDialogHelper {

    private static String TAG = AlertDialogHelper.class.getSimpleName();
    /**
     * @author lkelly
     * @desc invokes the alert dialog and set the response of the dialog
     * @params
     * @return
     * */
    public static void invokeDialog(Context context, String message, boolean isCancelable, String positiveBtnString, String negativeBtnString, int res, final IAlertDialogHelper iAlertDialogHelper){
        Log.d(TAG,"invokeDialog : init");
        AlertDialog alertDialog;

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, res);
        alertDialogBuilder.setMessage(message)
                .setCancelable(isCancelable)
                .setPositiveButton(positiveBtnString,
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //response[0] = Constants.AlertDialog.POSITVE_SELECTED;
                                dialog.dismiss();
                                iAlertDialogHelper.onPositiveClickListener(null);
                            }
                        })
                .setNegativeButton(negativeBtnString,
                        new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //response[0] = Constants.AlertDialog.NEGATIVE_SELECTED;
                                dialog.cancel();
                                iAlertDialogHelper.onNegativeClickListener(null);
                            }
                        });
        //creates dialog box
        alertDialog = alertDialogBuilder.create();
        //displays dialog box
        alertDialog.show();

        Log.d(TAG,"invokeDialog : exit");
    }

    /**
     * @author lkelly
     * @desc invokes the alert dialog and set the response of the dialog with custom views
     * @params
     * @return
     * */
    public static void invokeDialog(Context context, String message, boolean isCancelable, String positiveBtnString, String negativeBtnString, int res, int promptMessageView, int promptResponseView, int layout, final IAlertDialogHelper iAlertDialogHelper){
        Log.d(TAG,"invokeDialog : init");
        AlertDialog alertDialog;

        final String promptMessageText = message;
        LayoutInflater inflater = LayoutInflater.from(context);
        View promptView = inflater.inflate(layout,null);

        final TextView promptMessageTV = (TextView) promptView.findViewById(promptMessageView);
        final EditText promptResponseET = (EditText) promptView.findViewById(promptResponseView);

        //set prompt message
        promptMessageTV.setText(promptMessageText);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context,res);
        //sets the alert dialog to use the prompt.xml view
        alertDialogBuilder.setView(promptView);

        /*creates dialog box to accept input from keyboard containing the voucher number*/
        alertDialogBuilder
                .setCancelable(isCancelable)
                .setPositiveButton(positiveBtnString,
                        new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                iAlertDialogHelper.onPositiveClickListener(promptResponseET.getText().toString());
                            }
                        })
                .setNegativeButton(negativeBtnString,
                        new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                iAlertDialogHelper.onNegativeClickListener(null);
                            }
                        });
        //creates dialog box
        alertDialog = alertDialogBuilder.create();
        //displays dialog box
        alertDialog.show();
        Log.d(TAG,"invokeDialog : exit");
    }


}
