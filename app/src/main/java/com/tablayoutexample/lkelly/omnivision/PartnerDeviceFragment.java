package com.tablayoutexample.lkelly.omnivision;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.omnivision.core.Constants;
import com.omnivision.core.DaoSession;
import com.omnivision.core.PartnerDevice;
import com.omnivision.core.Phone;
import com.omnivision.dao.IPartnerDeviceDao;
import com.omnivision.dao.PartnerDeviceDaoImpl;
import com.omnivision.utilities.AlertDialogHelper;
import com.omnivision.utilities.CommandUtility;
import com.omnivision.utilities.IAlertDialogHelper;
import com.omnivision.Adapters.PartnerDevicesAdapter;
import com.omnivision.utilities.SessionManager;
import com.omnivision.utilities.SystemValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PartnerDeviceFragment extends Fragment {

    long phoneId;
    private OnSelectedPartnerDeviceFragmentListener mListener;
    private String TAG = PartnerDeviceFragment.class.getSimpleName();
   // public static String ARG_MENU_ITEM_NUMBER = "menu_number";
    private ListView partnerDeviceLv;
    private PartnerDevicesAdapter partnerDeviceAdapter;
    private List<PartnerDevice> partnerDevices =  new ArrayList<>();
    private List<PartnerDevice> partnerDevicesCopy =  new ArrayList<>();//used for displaying purposes

    private HashMap<String,String> userDetails;
    private DaoSession daoSession;
    private SessionManager sessionManager;

   // private IPhoneDao phoneDao;
    private IPartnerDeviceDao partnerDeviceDao;
    private Context context;

    public PartnerDeviceFragment() {
        // Required empty public constructor
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
                partnerDeviceDao = new PartnerDeviceDaoImpl(daoSession);
            }catch (Exception ex){
                String errMessage = "No partner devices found for this device";
                Log.e(TAG,errMessage + "\n"+ex.getLocalizedMessage());
                Toast.makeText(context,errMessage,Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        //loads information from the database
        loadPartnerDeviceNumbers();

        /*initializing views*/
        View rootView = inflater.inflate(R.layout.fragment_partner_device,container,false);
        partnerDeviceAdapter = new PartnerDevicesAdapter(context, R.layout.partner_devices_view, partnerDevicesCopy);
        partnerDeviceLv = (ListView) rootView.findViewById(R.id.partner_device_fragment_lv);
        partnerDeviceLv.setAdapter(partnerDeviceAdapter);
        partnerDeviceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PartnerDevice partnerDevice = partnerDevices.get(position);
                invokeSelectedPartnerDeviceFragment(partnerDevice.getPartnerDeviceNum());
            }
        });

        registerForContextMenu(partnerDeviceLv);//registering listview to be used with context menu

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        MenuItem newActionItem = menu.findItem(R.id.new_action);
        newActionItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.new_action:
                addPartnerDevice();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        if(v.getId() == R.id.partner_device_fragment_lv){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            //locating selected item
            menu.setHeaderTitle(partnerDevices.get(info.position).toString());
            //loading menu items
            String[] menuItems = getResources().getStringArray(R.array.partner_device_frag_cxt_menu);

            for(int i = 0; i < menuItems.length; i++){
                menu.add(Menu.NONE,i,i,menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case Constants.PartnerDeviceContextMenuItems.VIEW_PARTNER_DEVICE:
                invokeSelectedPartnerDeviceFragment(partnerDevices.get(info.position).getPartnerDeviceNum());
                break;
            case Constants.PartnerDeviceContextMenuItems.MAKE_PARTNER_DEVICE_PRIMARY:
                makePartnerDevicePrimary(partnerDevices.get(info.position));
                break;
            case Constants.PartnerDeviceContextMenuItems.DELETE:
                deletePartnerDevice(partnerDevices.get(info.position));
                break;
        }
        return true;
    }

    /**
     * @author lkelly
     * @desc opens fragment to view details of a phone's partner device
     * @params partnerDevice
     * @return
     * */
    private void invokeSelectedPartnerDeviceFragment(String partnerDeviceNum) {
        mListener.onSelectedPartnerDeviceFragmentListener(partnerDeviceNum);
    }

    /**
     * @author lkelly
     * @desc updates the isPrimaryFlag field on the partner device to true
     * @params partnerDevice
     * @return
     * */
    private void makePartnerDevicePrimary(PartnerDevice partnerDevice) {
        if(CommandUtility.makePartnerDevicePrimary(partnerDevice)){
            Toast.makeText(context,partnerDevice.toString()+" is now your primary partner device",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,partnerDevice.toString()+" is already your primary partner device",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @author lkelly
     * @desc adds a partner device to the phone
     * @params partnerDevice
     * @return
     * */
    private void addPartnerDevice() {
        Log.d(TAG,"addPartnerDevice : init");
        String positiveBtnString = context.getString(R.string.alert_dialog_add);
        String negativeBtnString = context.getString(R.string.alert_dialog_cancel);
        String message = context.getString(R.string.alert_dialog_message_add_partner_device);

        AlertDialogHelper.invokeDialog(context, message, false, positiveBtnString, negativeBtnString, R.style.Theme_AppCompat,
                R.id.prompt_message_tv, R.id.prompt_response_et, R.layout.prompt, new IAlertDialogHelper() {
                    @Override
                    public void onPositiveClickListener(String response) {
                        try{
                            if(response != null){
                                Log.d(TAG,"partner device number : "+response);

                                PartnerDevice partnerDevice = new PartnerDevice(phoneId, response, false,true);
                                if(CommandUtility.addPartnerDevice(partnerDevice)){
                                    refreshModel();
                                    loadPartnerDeviceNumbers();
                                    partnerDeviceAdapter.notifyDataSetChanged();
                                    Toast.makeText(context, "Partner device added", Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(context, "You are only allowed to have "+Constants.Constraints.MAX_PARTNER_DEVICE
                                            + "partner devices" , Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(context, "Partner number not valid or empty", Toast.LENGTH_LONG).show();
                                throw new SystemValidationException("partner number not valid or empty");
                            }
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
     * @desc refreshes data models used
     * @params
     * @return
     **/
    private void refreshModel(){
        Phone phone = OmniLocateApplication.getPhoneInstance();
        phone.resetPartnerDevicesNums();
        Log.d(TAG,"models refreshed");
    }

    /**
     * @author lkelly
     * @desc deletes phone's partner device
     * @params partnerDevice
     * @return
     * */
    private void deletePartnerDevice(PartnerDevice partnerDevice) {
        CommandUtility.deletePartnerDevice(partnerDevice);
    }

    /**
     * @author lkelly
     * @desc loads all partner devices for the phone
     * @params
     * @return String (voucher number)
     * */
    private void loadPartnerDeviceNumbers() {
        Log.d(TAG,"loadPartnerDeviceNumbers : init");

        phoneId = Long.parseLong(userDetails.get(Constants.SessionManager.PHONE_ID));
        partnerDevices = partnerDeviceDao.findAllByOwnerId(phoneId);

        partnerDevicesCopy.clear();
        partnerDevicesCopy.addAll(partnerDevices);

        //throw exception if simcard is null at this point
        if (partnerDevices != null && partnerDevices.size() == 0){
            Toast.makeText(context,"No partner devices found",Toast.LENGTH_LONG).show();
        }

        Log.d(TAG,"loadPartnerDeviceNumbers : exit");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSelectedPartnerDeviceFragmentListener) {
            mListener = (OnSelectedPartnerDeviceFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnSelectedPartnerDeviceFragmentListener {

        void onSelectedPartnerDeviceFragmentListener(String partnerDeviceNum);
    }
}
