package com.tablayoutexample.lkelly.omnivision;

import android.Manifest;
import android.app.Fragment;
import android.content.IntentFilter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.omnivision.core.Constants;
import com.omnivision.utilities.CommandReceiver;
import com.omnivision.utilities.PermissionManager;
import com.omnivision.utilities.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PartnerDeviceFragment.OnSelectedPartnerDeviceFragmentListener {

    private CommandReceiver commandReceiver;
    private Button startServiceBtn;
    private TextView textView;
    private String TAG = "MainActivity";

    private String[] mDrawerTitles;
    private List mDrawerIconTitles;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mNavigationPaneLayout;
    private ListView mDrawerListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private Toolbar mainToolBar;
    private int selectedNavigationItemPosition;
    private TextView userNameTV;
    private SessionManager sessionManager;
    private HashMap<String,String> userDetails;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializing session manager
        sessionManager = new SessionManager(this);
        //setting user details
        userDetails = sessionManager.getUserDetails();

        checkDevicePermissions();

        mainToolBar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolBar);

        initializeDrawerLayout(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        selectItem(Constants.Navigation.NavigationPositions.HOME);//selecting the HOME position from the navigation drawer

       // if(PermissionManager.hasPermission(MainActivity.this,Manifest.permission.RECEIVE_SMS)
         //       && PermissionManager.hasPermission(MainActivity.this,Manifest.permission.SEND_SMS)){
        registerCommandReceiver();
        //}
    }

    /**
     * @author lkelly
     * @desc checks and request device permissions
     *       Permissions include:
     *       RECEIVE_SMS
     *       ACCESS_FINE_LOCATION
     *       CALL_PHONE
     *       CAMERA
     *       WRITE_EXTERNAL_STORAGE
     * @params
     * @return
     * */
    private void checkDevicePermissions() {
        PermissionManager.check(this, Manifest.permission.RECEIVE_SMS, Constants.Permissions.SMS_REQUEST_CODE.getCode());
        PermissionManager.check(this, Manifest.permission.ACCESS_FINE_LOCATION, Constants.Permissions.ACCESS_FINE_LOCATION_REQUEST_CODE.getCode());
        PermissionManager.check(this,Manifest.permission.CALL_PHONE,Constants.Permissions.CALL_PHONE_REQUEST_CODE.getCode());
        PermissionManager.check(this,Manifest.permission.CAMERA,Constants.Permissions.CAMERA_REQUEST_CODE.getCode());
        PermissionManager.check(this,Manifest.permission.WRITE_EXTERNAL_STORAGE,Constants.Permissions.CAMERA_REQUEST_CODE.getCode());
    }

    private void registerCommandReceiver(){
        Log.i(TAG,"Registering the command receiver!");
        commandReceiver = new CommandReceiver();
        //creating intent filter for the listening of received sms
        IntentFilter smsReceivedIntentFilter = new IntentFilter(Constants.IntentActions.SMS_RECEIVED);//used for the filtering received sms
        IntentFilter smsSentIntentFilter = new IntentFilter(Constants.IntentActions.SMS_SENT);
        IntentFilter smsDeliveredIntentFilter = new IntentFilter(Constants.IntentActions.SMS_DELIVERED);

        //TODO register receiver in a separate thread
        if (PermissionManager.hasPermission(this,Manifest.permission.RECEIVE_SMS)) {
            registerReceiver(commandReceiver, smsReceivedIntentFilter);
            registerReceiver(commandReceiver, smsSentIntentFilter);
            registerReceiver(commandReceiver, smsDeliveredIntentFilter);
        }

        /*TODO to be removed: testing
        IntentFilter lockIntentFilter = new IntentFilter();
        lockIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        lockIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        lockIntentFilter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(commandReceiver,lockIntentFilter);

        ComponentName cn = new ComponentName(this,CommandReceiver.AdminReceiver.class);
        DevicePolicyManager mgr = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //enable custom device administrator if it is not active
        if(!mgr.isAdminActive(cn)){
            Intent adminIntent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            adminIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,cn);
            adminIntent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,this.getString(R.string.device_admin_description));
            startActivity(adminIntent);
        }*/

        Log.i(TAG,"Command receiver registered successfully!");
    }

    //Handle User's response for your permission request
  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == Constants.Permissions.SMS_REQUEST_CODE.getCode()){//response for SMS permission request
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                registerCommandReceiver();
            }else{
                //What to do if user disallowed requested SMS permission
            }
        }
    }*/

    /**
     * @author lkelly
     * @desc initializes the navigation drawer and it's related items
     * @params
     * @return
     * */
    private void initializeDrawerLayout(Bundle savedInstanceState) {
        mDrawerTitles = getResources().getStringArray(R.array.app_drawer_titles);
        mDrawerIconTitles = new ArrayList<HashMap<String,String>>();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.left_drawer_layout);
        mNavigationPaneLayout = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerListView = (ListView) findViewById(R.id.left_drawer_lv);
        mTitle = mDrawerTitle = getTitle();
        userNameTV = (TextView) findViewById(R.id.user_name_tv) ;

        //sets username/email in drawer layout
        userNameTV.setText(userDetails.get(Constants.SessionManager.EMAIL));

        String[] from = {Constants.Navigation.icon,Constants.Navigation.title};
        int[] to = {R.id.drawer_item_icons,R.id.drawer_list_item_tv};

        initializeDrawerIconTitles();

        //set the adapter for the listview
        mDrawerListView.setAdapter(new SimpleAdapter(this,mDrawerIconTitles,R.layout.drawer_list_item
                ,from,to));

        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedNavigationItemPosition = position;//temp uses this to identify the menuitem position clicked throughout the application
                selectItem(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mainToolBar,R.string.drawer_open,R.string.drawer_close){
            public void onDrawerClosed(View view){
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();//creates call to onPrepareOptionsMenu()
            }
            public void onDrawerOpened(View view){
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();//creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if(savedInstanceState == null){
            selectItem(Constants.Navigation.NavigationPositions.HOME);
        }
    }

    private void initializeDrawerIconTitles() {
        mDrawerIconTitles = new ArrayList<HashMap<String,String>>();
        for(String title : mDrawerTitles){
            HashMap<String,String> iconTitleMap = new HashMap<String,String>();
            switch (title.replace(' ','_').toUpperCase()) {
                case Constants.Navigation.HOME: iconTitleMap.put(Constants.Navigation.icon,Integer.toString(R.drawable.ic_action_dock));
                    break;
                case Constants.Navigation.PARTNER_DEVICES: iconTitleMap.put(Constants.Navigation.icon,Integer.toString(R.drawable.ic_action_phone));
                    break;
                case Constants.Navigation.PREPAID_CREDITS: iconTitleMap.put(Constants.Navigation.icon,Integer.toString(R.drawable.ic_action_attachment));
                    break;
                case Constants.Navigation.COMMAND_HISTORY: iconTitleMap.put(Constants.Navigation.icon,Integer.toString(R.drawable.ic_action_collection));
                    break;
                case Constants.Navigation.DEVICE_WIPE: iconTitleMap.put(Constants.Navigation.icon,Integer.toString(R.drawable.ic_action_discard));
                    break;
            }
            iconTitleMap.put(Constants.Navigation.title,title);
            mDrawerIconTitles.add(iconTitleMap);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after after onRestoreInstanceState has occurred
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Called whenever invalidateOptionsMenu() is called
     * */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem menuItem;
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mNavigationPaneLayout);

        switch (selectedNavigationItemPosition){
            case Constants.Navigation.NavigationPositions.HOME:
                break;
            case Constants.Navigation.NavigationPositions.PARTNER_DEVICES:
                break;
            case Constants.Navigation.NavigationPositions.DEVICE_WIPE:
                break;
            case Constants.Navigation.NavigationPositions.PREPAID_CREDITS:
                menuItem = menu.findItem(R.id.new_action);
                menuItem.setVisible(true);
                break;
            case Constants.Navigation.NavigationPositions.COMMAND_HISTORY:
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.log_out_action :
                SessionManager sessionManager = new SessionManager(this);
                sessionManager.logOut();
                break;
        }
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @author lkelly
     * @desc designate the appropriate action for each navigation drawer items
     * @params
     * @return
     * */
    private void selectItem(int position) {
        //create a new fragment and specify the fragment that should be loaded based on
        switch (position){
            case Constants.Navigation.NavigationPositions.HOME:
                onHomeSelection(position);
                break;
            case Constants.Navigation.NavigationPositions.PARTNER_DEVICES:
                onPartnerDevicesSelection(position);
                break;
            case Constants.Navigation.NavigationPositions.DEVICE_WIPE:
                break;
            case Constants.Navigation.NavigationPositions.PREPAID_CREDITS:
                onPrepaidCreditSelection(position);
                break;
            case Constants.Navigation.NavigationPositions.COMMAND_HISTORY:
                break;
        }
    }

    /**
     * @author lkelly
     * @desc swaps selected fragment into the main content view of the application
     * @params
     * @return
     * */
    private void swapFragment(Fragment fragment, int position){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();

        //update selected item and title, then close the drawer
        mDrawerListView.setItemChecked(position,true);
        mTitle = mDrawerTitle = mDrawerTitles[position];
        mDrawerLayout.closeDrawer(mNavigationPaneLayout);
    }

    /**
     * @author lkelly
     * @desc swaps PartnerDeviceFragment in the main content view of the application
     * @params
     * @return
     * */
    private void onPartnerDevicesSelection(int position) {
        PartnerDeviceFragment fragment = new PartnerDeviceFragment();
        swapFragment(fragment,position);
    }

    /**
     * @author lkelly
     * @desc swaps PrepaidCreditFragment in the main content view of the application
     * @params
     * @return
     * */
    private void onPrepaidCreditSelection(int position) {
        PrepaidCreditFragment fragment = new PrepaidCreditFragment();
        swapFragment(fragment,position);
    }

    /**
     * @author lkelly
     * @desc swaps home fragment in the main content view of the application
     * @params
     * @return
     * */
    private void onHomeSelection(int position) {
        //PrepaidCreditFragment fragment = new PrepaidCreditFragment();

        //android.app.FragmentManager fragmentManager = getFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();

        //update selected item and title, then close the drawer
        mDrawerListView.setItemChecked(position,true);
        mTitle = mDrawerTitle = mDrawerTitles[position];
        mDrawerLayout.closeDrawer(mNavigationPaneLayout);
    }

    @Override
    public void onSelectedPartnerDeviceFragmentListener(long partnerDeviceId) {
        SelectedPartnerDeviceFragment fragment = new SelectedPartnerDeviceFragment();
    }
}
