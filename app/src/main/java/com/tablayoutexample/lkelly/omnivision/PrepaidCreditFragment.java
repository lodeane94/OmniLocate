package com.tablayoutexample.lkelly.omnivision;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.omnivision.core.Constants;
import com.omnivision.core.DaoSession;
import com.omnivision.core.PrepaidCredit;
import com.omnivision.core.SimCard;
import com.omnivision.dao.IPrepaidCreditDao;
import com.omnivision.dao.ISimCardDao;
import com.omnivision.dao.PrepaidCreditDaoImpl;
import com.omnivision.dao.SimCardDaoImpl;
import com.omnivision.utilities.AlertDialogHelper;
import com.omnivision.utilities.CommandUtility;
import com.omnivision.Adapters.ExpandableListAdapter;
import com.omnivision.utilities.IAlertDialogHelper;
import com.omnivision.utilities.SessionManager;
import com.omnivision.utilities.SystemValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lodeane on 6/5/2017.
 */
/**
 * @author lkelly
 * @desc Fragment that will appear in the content frame on selection of a navigation menu item
 * @params
 * @return
 * */
public class PrepaidCreditFragment extends Fragment implements PartnerDeviceFragment.OnSelectedPartnerDeviceFragmentListener {
    private String TAG = PrepaidCreditFragment.class.getSimpleName();
    public static String ARG_MENU_ITEM_NUMBER = "menu_number";
    public static boolean isGroupExpanded;//used to determine if an item was selected
    private ExpandableListView expandableCreditLv;
    private String voucherNumber;
    private ExpandableListAdapter expandableListAdapter;
    private AlertDialog alertDialog;
    private int groupItemIndexSelected;

    private List<String> credits =  new ArrayList<>();
    private Map<String,Map<String,String>> creditsCollection =  new HashMap<>();

    private HashMap<String,String> userDetails;
    private DaoSession daoSession;
    private SessionManager sessionManager;

    private IPrepaidCreditDao prepaidCreditDao;
    private ISimCardDao simcardDao;
    private SimCard simCard;
    
    private Context context;
    

    public PrepaidCreditFragment(){
        //empty constructor is required for fragment subclasses
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                context = getContext();
                daoSession = OmniLocateApplication.getSession(context);//initializing dao session
                //initializing session manager
                sessionManager = new SessionManager(context);
                //setting user details
                userDetails = sessionManager.getUserDetails();

                prepaidCreditDao = new PrepaidCreditDaoImpl(daoSession);
                simcardDao = new SimCardDaoImpl(daoSession);
                //retrieving all sim cards for phone
                List<SimCard> simCards = simcardDao.findAllByOwnerId(Long.valueOf(userDetails.get(Constants.SessionManager.PHONE_ID)));
                for (SimCard sim : simCards) {
                    if (String.valueOf(sim.getId()).equals(userDetails.get(Constants.SessionManager.SIM_ID))) {
                        simCard = sim;
                    }
                }
                //throw exception if simcard is null at this point
                if (simCard == null)
                    throw new NullPointerException("Session is invalid: simcard value is null");
            }catch (Exception ex){
                String errMessage = "Error occurred while loading prepaid credits";
                Log.e(TAG,errMessage + "\n"+ex.getLocalizedMessage());
                Toast.makeText(context,errMessage,Toast.LENGTH_LONG);
            }
            //PrepaidCreditDao p = daoSession.getPrepaidCreditDao();
           // p.deleteAll();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        //if item is selected, display ic_action_call action icon

        if(isGroupExpanded){
            MenuItem callActionItem = menu.findItem(R.id.call_action);
            callActionItem.setVisible(true);
            MenuItem removeActionItem = menu.findItem(R.id.remove_action);
            removeActionItem.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.new_action:
                addCredit();
                break;
            case R.id.call_action:
                try {
                    activateUSSDCall();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.remove_action:
                deletePrepaidCard();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        //loads information from the database
        loadPrepaidCreditInformation();

        /*initializing views*/
        View rootView = inflater.inflate(R.layout.expandable_group_view,container,false);
        expandableCreditLv = (ExpandableListView) rootView.findViewById(R.id.list_view_items_elv);
        expandableListAdapter = new ExpandableListAdapter(getActivity(),credits,creditsCollection);
        expandableCreditLv.setAdapter(expandableListAdapter);

        expandableCreditLv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener(){
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup){
                    expandableCreditLv.collapseGroup(previousGroup);
                    previousGroup = groupPosition;
                }
                /*calling onPrepareOptionsMenu to display call icon*/
                isGroupExpanded = true;
                groupItemIndexSelected = groupPosition;//setting selected item
                getActivity().invalidateOptionsMenu();
            }
        });

        expandableCreditLv.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener(){

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*calling onPrepareOptionsMenu to remove call icon*/
                isGroupExpanded = false;
                groupItemIndexSelected = -1;//setting selected item to -1 when nothing is selected
                getActivity().invalidateOptionsMenu();
            }
        });
        return rootView;
    }
    /**
     * @author lkelly
     * @desc gets the selected voucher number
     * @params
     * @return String (voucher number)
     * */
    private void loadPrepaidCreditInformation() {
        Log.d(TAG,"loadPrepaidCreditInformation : init");
        List<PrepaidCredit> prepaidCredits = simCard.getPrepaidCredit();
        //clear list and map before reloading data on the screen
        credits.clear();
        creditsCollection.clear();

        if(prepaidCredits != null && prepaidCredits.size() > 0) {
            for (PrepaidCredit credit : prepaidCredits) {
                PrepaidCredit prepaidCredit = credit;
                HashMap<String, String> creditCollection = new HashMap<>();
                //adding credits to the credit list necessary for the expandable list view
                credits.add(prepaidCredit.getVoucherNum());
                creditCollection.put(Constants.PrepaidCredit.ADDED_BY, prepaidCredit.getAddedBy());
                creditCollection.put(Constants.PrepaidCredit.DATE_ADDED, prepaidCredit.getDateAdded());

                String isUsed = prepaidCredit.getIsUsed() ? Constants.General.YES.name() : Constants.General.NO.name();
                creditCollection.put(Constants.PrepaidCredit.IS_USED, isUsed);
                creditsCollection.put(prepaidCredit.getVoucherNum(),creditCollection);
            }

            Log.d(TAG, "credit collection populated");
        }else{
            Toast.makeText(context,"Prepaid credit collection is empty",Toast.LENGTH_LONG).show();
        }
        Log.d(TAG,"loadPrepaidCreditInformation : exit");
    }
    /**
     * @author lkelly
     * @desc calls the ussd voucher number
     * @params
     * @return
     * */
    private void activateUSSDCall() throws Exception {
        String selectedVoucherNumber = getSelectedItem();
        CommandUtility.activateCredit(selectedVoucherNumber);
    }
    /**
     * @author lkelly
     * @desc gets the selected voucher number
     * @params
     * @return String (voucher number)
     * */
    private String getSelectedItem() {
        return credits.get(groupItemIndexSelected);
    }
    /**
     * @author lkelly
     * @desc adds voucher number to the phone's database that can be activated by app's gui or a text message
     * @params
     * @return
     * */
    /*
    private void addCredit(){
        Log.d(TAG,"addCredit : init");
        if(this.voucherNumber != null){
            try {
                PrepaidCredit prepaidCredit = new PrepaidCredit(this.voucherNumber, userDetails.get(Constants.SessionManager.EMAIL), Long.valueOf(userDetails.get(Constants.SessionManager.PHONE_ID)), context);
                PrepaidCreditDao prepaidCreditDao = daoSession.getPrepaidCreditDao();
                 adding prepaid credit to database | insert returns a row number
                if row is greater than -1 then the record was inserted successfully adding prepaid credit to database |
                if (prepaidCreditDao.insert(prepaidCredit)) {
                prepaidCreditDao.insert(prepaidCredit);
                refreshModel();
                loadPrepaidCreditInformation();
                Toast.makeText(context, "Credit added successfully", Toast.LENGTH_LONG).show();
            }catch (Exception ex){
                Toast.makeText(context, "Failure adding credit", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
        Log.d(TAG,"addCredit : exit");
    }*/
    /**
     * @author lkelly
     * @desc adds voucher number to the phone's database that can be activated by app's gui
     * @params
     * @return
     * */
    private void addCredit() {
        Log.d(TAG,"voucherNumberAlertDialogEntry : init");
        String positiveBtnString = context.getString(R.string.alert_dialog_add);
        String negativeBtnString = context.getString(R.string.alert_dialog_cancel);
        String message = context.getString(R.string.alert_dialog_message_add_credit);

        AlertDialogHelper.invokeDialog(context, message, false, positiveBtnString, negativeBtnString, R.style.Theme_AppCompat,
                R.id.prompt_message_tv, R.id.prompt_response_et, R.layout.prompt, new IAlertDialogHelper() {
                    @Override
                    public void onPositiveClickListener(String response) {
                        try{
                            if(response != null){
                                setVoucherNumber(response);
                                Log.d(TAG,"voucher number : "+getVoucherNumber());
                                PrepaidCredit prepaidCredit = new PrepaidCredit(response, userDetails.get(Constants.SessionManager.EMAIL), Long.valueOf(userDetails.get(Constants.SessionManager.SIM_ID)), context);
                                CommandUtility.addCredit(prepaidCredit);
                                refreshModel();
                                loadPrepaidCreditInformation();
                                Toast.makeText(context, "Credit added successfully", Toast.LENGTH_LONG).show();
                            }else
                                throw new SystemValidationException("Voucher number not valid or empty");
                        }catch (SystemValidationException ex){
                            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_LONG).show();
                            ex.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNegativeClickListener(String response) {
                        //do nothing on callback
                    }
                });
        Log.d(TAG,"voucherNumberAlertDialogEntry : exit");
    }
    /**
     * @author lkelly
     * @desc deletes the selected voucher number
     * @params
     * @return
     * */
    private void deletePrepaidCard() {
        Log.d(TAG,"deletePrepaidCard : init");
        String positiveBtnString = context.getString(R.string.alert_dialog_yes);
        String negativeBtnString = context.getString(R.string.alert_dialog_no);
        String message = context.getString(R.string.alert_dialog_message_remove_credit);

        AlertDialogHelper.invokeDialog(context, message, false, positiveBtnString
                , negativeBtnString, R.style.Theme_AppCompat_Light_Dialog, new IAlertDialogHelper() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onPositiveClickListener(String response) {
                    String selectedVoucherNumber = getSelectedItem();
                    prepaidCreditDao.delete(prepaidCreditDao.findByValue(selectedVoucherNumber));
                    refreshModel();
                    loadPrepaidCreditInformation();
                    expandableListAdapter.notifyDataSetChanged();
                    isGroupExpanded = false;
                    getActivity().invalidateOptionsMenu();//calls onprepareoptions menu
                    Toast.makeText(context,"Prepaid credit was deleted successfully",Toast.LENGTH_LONG).show();
                    Log.d(TAG,"Prepaid credit was deleted successfully");
                }

                @Override
                public void onNegativeClickListener(String response) {
                    //do nothing on callback
                }
                });
        Log.d(TAG,"deletePrepaidCard : exit");
    }
    /**
     * @author lkelly
     * @desc refreshes data models used
     * @params
     * @return
     * */
    private void refreshModel(){
        Log.d(TAG,"refreshModel : init");
        simCard.resetPrepaidCredit();
        Log.d(TAG,"models refreshed");
        Log.d(TAG,"refreshModel : exit");
    }
    /**
     * @author lkelly
     * @desc sets voucher number
     * @params
     * @return
     * */
    public void setVoucherNumber(String voucherNumber){
        this.voucherNumber = voucherNumber;
    }
    /**
     * @author lkelly
     * @desc returns voucher number
     * @params
     * @return String (voucher number)
     * */
    public String getVoucherNumber(){
        return this.voucherNumber;
    }

    @Override
    public void onSelectedPartnerDeviceFragmentListener(long partnerDeviceId) {

    }
}
