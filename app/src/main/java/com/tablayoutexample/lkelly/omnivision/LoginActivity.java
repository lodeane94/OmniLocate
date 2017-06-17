package com.tablayoutexample.lkelly.omnivision;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import  android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.omnivision.core.Constants;
import com.omnivision.core.DaoSession;
import com.omnivision.core.SimCard;
import com.omnivision.core.SimNetwork;
import com.omnivision.core.SimNetworkCodes;
import com.omnivision.core.User;
import com.omnivision.core.UserDao;
import com.omnivision.dao.ISimCardDao;
import com.omnivision.dao.ISimNetworkCodeDao;
import com.omnivision.dao.ISimNetworkDao;
import com.omnivision.dao.SimCardDaoImpl;
import com.omnivision.dao.SimNetworkCodeDaoImpl;
import com.omnivision.dao.SimNetworkDaoImpl;
import com.omnivision.utilities.DialingHandler;
import com.omnivision.utilities.PhoneManager;
import com.omnivision.utilities.SessionManager;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collections;

public class LoginActivity extends AppCompatActivity {
    private static LoginActivity instance;
    private static final String TAG = LoginActivity.class.getSimpleName() ;
    private Button registerDeviceBtn;
    private Button loginBtn;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText smsCostEt;
    private EditText ussdCommandCodeEt;
    private TextView ussdResultTv;
    private EditText ussdRelevantResultEt;
    private Spinner ussdCommandSpnr;
    private Button getStartedBtn;
    private Button addUssdCommandBtn;
    private SessionManager sessionManager;
    private DaoSession daoSession;

    ArrayAdapter<String> simNetworkCodesDataAdapter;
    ArrayList<String> simNetworkUssdCommandNames;

    public static LoginActivity getInstance(){
        return instance;
    }
    public void updateUI(final String result){
        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ussdResultTv.setText(result);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"login activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        instance = this;

        loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.i(TAG,"Preparing to login user");
                loginUser();
            }
        });

        registerDeviceBtn = (Button) findViewById(R.id.registerDeviceBtn);
        registerDeviceBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){//onclick of register device button
                Log.i(TAG,"Preparing to register user");
                launchRegistrationActivity();
            }
        });

        com.omnivision.utilities.SessionManager sessionManager = new com.omnivision.utilities.SessionManager(this);
        if(sessionManager.isLoggedIn()){
            //start main activity if user is still logged in
            launchMainActivity();
        }
    }

    private void loginUser() {
        EditText emailEt = (EditText)findViewById(R.id.emailEditText);
        EditText passwordEt = (EditText)findViewById(R.id.passwordEditText);

        try{
            daoSession = OmniLocateApplication.getSession(LoginActivity.this);//initializing dao session
            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();

            UserDao userDao = daoSession.getUserDao();
            QueryBuilder<User> queryBuilder = userDao.queryBuilder();
            queryBuilder.where(UserDao.Properties.Email.eq(email),UserDao.Properties.Password.eq(password));
            User user = queryBuilder.unique();
            //user.setDevices(user.getDevices());
            //PhoneDao.dropTable(OmniLocateApplication.getSession(LoginActivity.this).getDatabase(),true);
            //PhoneDao.createTable(OmniLocateApplication.getSession(LoginActivity.this).getDatabase(),true);
            //userDao.deleteAll(); //TODO remove this comment
            if(user != null){
                //create new session for user
                sessionManager = new SessionManager(this);
                PhoneManager phoneManager = new PhoneManager(this);
                sessionManager.createLoginSession(user.getId().toString()
                        ,user.getEmail()
                        ,phoneManager.getPhoneDetails().get(Constants.SessionManager.PHONE_ID)
                        ,phoneManager.getPhoneDetails().get(Constants.SessionManager.SIM_ID));

                //if it is a initial login then display the activity_login_initial_setup layout else start main activity
            // if(phoneManager.getLoginCountNum() > 1)//1 indicates initial login
                    //start main activity upon valid credentials
            launchMainActivity();
             //  else
             //       initiateInitialSetup();
            }else{
                Toast.makeText(this,"Invalid email/password combination",Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Log.e(TAG,ex.getMessage());
            ex.printStackTrace();
        }

    }

    private void initiateInitialSetup() {
        setContentView(R.layout.activity_login_initial_setup);
        smsCostEt = (EditText) findViewById(R.id.sms_cost_et);
        ussdCommandCodeEt = (EditText) findViewById(R.id.ussd_command_et);
        ussdResultTv = (TextView) findViewById(R.id.ussd_result_tv);
        ussdRelevantResultEt = (EditText) findViewById(R.id.ussd_relevant_result_et);
        getStartedBtn = (Button) findViewById(R.id.get_started_btn);
        addUssdCommandBtn = (Button) findViewById(R.id.add_ussd_command_btn);
        ussdCommandSpnr = (Spinner) findViewById(R.id.ussdCommandSpr);

        simNetworkUssdCommandNames = new ArrayList<>();
        //populating ussd command spinner
        Collections.addAll(simNetworkUssdCommandNames, getResources().getStringArray(R.array.array_ussd_commands));
        simNetworkCodesDataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, simNetworkUssdCommandNames);
        simNetworkCodesDataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ussdCommandSpnr.setAdapter(simNetworkCodesDataAdapter);


        addUssdCommandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSimNetworkCode();
            }
        });

        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueLogin();
            }
        });
    }

    /**
     * @author lkelly
     * @desc validates that ussd commands are setup on the device before continuing to the main activity
     * @params
     * */
    private void continueLogin() {
        String notifMess = "Please add ussd codes for all items";
        /*uses the data input for the spinner to identify if the ussd codes have all been added to the device*/
        if(simNetworkUssdCommandNames != null && simNetworkUssdCommandNames.size() != 0)
            Toast.makeText(this,notifMess,Toast.LENGTH_LONG);
        else
            launchMainActivity();
    }

    /**
     * @author lkelly
     * @desc adds the network code information
     * @params
     * */
    //TODO move code into a fragment for reuse capability
    private void addSimNetworkCode() {
        try {
            ISimCardDao simCardDao = new SimCardDaoImpl(daoSession);
            ISimNetworkCodeDao simCardNetworkCodeDao = new SimNetworkCodeDaoImpl(daoSession);
            SimCard simCard = simCardDao.find(Long.parseLong(sessionManager.getUserDetails().get(Constants.SessionManager.SIM_ID)));

            String selectedItem = ussdCommandSpnr.getSelectedItem().toString();
            //ISimNetworkDao simNetworkDAO = new SimNetworkDaoImpl(daoSession);
            //SimNetwork simNetwork = simNetworkDAO.find(simCard.getSimNetworkId());//TODO allow greenDAO to load lazy load this entity: simCard.getSimNetwork = null
            SimNetworkCodes simNetworkCodes = new SimNetworkCodes(simCard.getSimNetwork().getId()
                    ,selectedItem , ussdCommandCodeEt.getText().toString());
            simCardNetworkCodeDao.insert(simNetworkCodes);

            loadUSSDResult(ussdCommandCodeEt.getText().toString());

            simNetworkUssdCommandNames.remove(selectedItem);
            simNetworkCodesDataAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Ussd Code added successfully",Toast.LENGTH_SHORT);
        }catch (Exception ex){
            String errMessage = "Error occurred while adding USSD code";
            Log.e(TAG,errMessage +" \n"+ ex.getLocalizedMessage());
            Toast.makeText(this,errMessage,Toast.LENGTH_LONG);
            ex.printStackTrace();
        }
    }

    /**
     * @author lkelly
     * @desc loads the result of the ussd commands into a textview
     * @params
     * */
    //TODO move into a fragment
    private void loadUSSDResult(String ussdCode) {
        DialingHandler.dial(ussdCode,this);
    }

    /*
    * params
    * launches the registration activity*/
    private void launchRegistrationActivity(){
        Intent registrationIntent = new Intent(this,DeviceRegistrationActivity.class);
        Log.i(TAG,"starting registration activity");
        startActivity(registrationIntent);
    }
    /*
    * params
    * launches the registration activity*/
    private void launchMainActivity(){
        Log.d(TAG,"launchMainActivity : init");
        Intent mainIntent = new Intent(this,MainActivity.class);
        // Closing all the Activities
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d(TAG,"launchMainActivity : exit");
        startActivity(mainIntent);
    }
}
