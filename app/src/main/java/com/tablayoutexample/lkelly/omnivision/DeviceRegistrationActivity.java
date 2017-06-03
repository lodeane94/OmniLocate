package com.tablayoutexample.lkelly.omnivision;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.omnivision.core.Constants;
import com.omnivision.core.Country;
import com.omnivision.core.DaoSession;
import com.omnivision.core.PartnerDevice;
import com.omnivision.core.Phone;
import com.omnivision.core.SimCard;
import com.omnivision.core.SimNetwork;
import com.omnivision.core.User;
import com.omnivision.core.UserDao;
import com.omnivision.dao.ISimCardDao;
import com.omnivision.dao.ISimNetworkDao;
import com.omnivision.dao.JSONHelper;
import com.omnivision.dao.SimCardDaoImpl;
import com.omnivision.dao.SimNetworkDaoImpl;
import com.omnivision.utilities.PartnerDevicesAdapter;
import com.omnivision.utilities.PhoneManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeviceRegistrationActivity extends AppCompatActivity {
    private static final String TAG = DeviceRegistrationActivity.class.getSimpleName() ;
    private Button continueRegistrationBtn;
    private Button finishRegistrationBtn;
    private Button addPartnerDeviceBtn;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText cellNumEt;
    private EditText partnerDeviceNumEt;
    private Spinner countrySpinner;
    private Spinner noOfPartnerDevices;
    private Spinner simNetworkProviderSpinner;
    private ListView partner_devices_lv;

    private ArrayList<Country> countries = new ArrayList();//used to initialize country spinner//TODO change country enum in class diagram to a actual class (visio)
    private final List<PartnerDevice> partnerDevicesCellNumbers = new ArrayList<PartnerDevice>();//used to hold list of partner devices

    private int noOfPartnerDevice;
    private Long partnerDevicesAddedCount = new Long(0);//used to enforce the max amt of partner devices a user can enter

    private DaoSession daoSession;
    private ISimNetworkDao simNetworkDao;
    private ISimCardDao simCardDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"device registration activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_registration);

        daoSession = OmniLocateApplication.getSession(this);
        simCardDao = new SimCardDaoImpl(daoSession);
        simNetworkDao = new SimNetworkDaoImpl(daoSession);

        //activity view initialization
        initializeCountrySpinner();
        initializeSimNetworkProviderSpinner();
        addListenerContinueRegBtn();//listener for the continue registration button

        continueRegistrationBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setContentView(R.layout.activity_device_registration_complete);
                initializeContinuedRegistrationView();
            }
        });
    }

    private void initializeContinuedRegistrationView() {
        /*referencing widgets from activity_device_registration.xml file*/
        finishRegistrationBtn = (Button) findViewById(R.id.finish_registration_btn);
        addPartnerDeviceBtn = (Button)findViewById(R.id.add_partner_device_btn);
        partnerDeviceNumEt = (EditText) findViewById(R.id.partner_device_et);
        partner_devices_lv = (ListView) findViewById(R.id.partner_devices_lv);

        try {
            noOfPartnerDevice = Integer.parseInt(noOfPartnerDevices.getSelectedItem().toString()); //getting the selected item
            final PartnerDevicesAdapter partnerDevicesCellNumAdapter = new PartnerDevicesAdapter(DeviceRegistrationActivity
                    .this, R.layout.partner_devices_view, partnerDevicesCellNumbers);
            partner_devices_lv.setAdapter(partnerDevicesCellNumAdapter);

            addPartnerDeviceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*adding the partner devices numbers to the list to be persisted*/
                    partnerDevicesAddedCount++;
                    PartnerDevice partnerDevice = new PartnerDevice(partnerDevicesAddedCount,partnerDeviceNumEt.getText().toString());
                    partnerDevicesCellNumbers.add(partnerDevice);
                    partnerDevicesCellNumAdapter.notifyDataSetChanged();
                    //disable add button to restrict partner devices total
                    if (partnerDevicesAddedCount == noOfPartnerDevice) {
                        addPartnerDeviceBtn.setEnabled(false);
                        partnerDeviceNumEt.setEnabled(false);//TODO removse these views from the layout after maximum is reached
                    }
                    partnerDeviceNumEt.setText("");//clearing partner number text to accept new number
                }
            });

            finishRegistrationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //// TODO: 3/17/2017 start loading gif to indicate that the process has started
                    setUserInformation();
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void initializeCountrySpinner() {
        JSONHelper jsonHelper = new JSONHelper(this,"countries.json");
        countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        List<String> countryListNames = new ArrayList<String>();
        try {
            JSONArray jsonArrCountries = jsonHelper.getJSONArray("countries");
            for(int i=0; i < jsonArrCountries.length(); i++){
                JSONObject jsonObjCountries = jsonArrCountries.getJSONObject(i);
                //Country country = new Country(jsonObjCountries.getString("name"),jsonObjCountries.getString("code"));
                //countries.add(country);
                countryListNames.add(jsonObjCountries.getString("name"));
            }
            ArrayAdapter<String> countriesDataAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,countryListNames);
            countriesDataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            countrySpinner.setAdapter(countriesDataAdapter);
        } catch (JSONException e) {
            String errMessage = "Error occurred while initializing country spinner";
            Log.d(TAG,errMessage+"\n ex: "+e.getLocalizedMessage());
            Toast.makeText(this,errMessage,Toast.LENGTH_LONG);
            e.printStackTrace();
        }
    }

    private void initializeSimNetworkProviderSpinner(){
        try {
            //testing creating example simnetworkprover
            simNetworkDao.deleteAll();
            SimNetwork simNetwork = new SimNetwork("Digicel","China");
            simNetworkDao.insert(simNetwork);

            simNetworkProviderSpinner = (Spinner) findViewById(R.id.simNetworkProvider);
            List<String> simNetworkProviderNames = new ArrayList<>();
            List<SimNetwork> simNetworks = simNetworkDao.findAll();

            for(SimNetwork network : simNetworks){
                simNetworkProviderNames.add(network.getNetworkProvider());
            }

            if(simNetworkProviderNames != null && simNetworkProviderNames.size() > 0) {
                ArrayAdapter<String> simNetworkProviderDataAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, simNetworkProviderNames);
                simNetworkProviderDataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                simNetworkProviderSpinner.setAdapter(simNetworkProviderDataAdapter);
            }else
                throw new Exception("No sim network provider found");

            }catch (Exception ex){
            String errMessage = "Error occurred while initializing sim network provider spinner";
            Log.d(TAG,errMessage+"\n ex: "+ex.getLocalizedMessage());
            Toast.makeText(this,errMessage,Toast.LENGTH_LONG);
            ex.printStackTrace();
        }
    }

    private void addListenerContinueRegBtn() {
        /*referencing widgets from activity_device_registration.xml file*/
        continueRegistrationBtn = (Button)findViewById(R.id.continueRegistrationButton);
        emailEt = (EditText) findViewById(R.id.emailEditText);
        passwordEt = (EditText) findViewById(R.id.passwordEditText);
        cellNumEt = (EditText) findViewById(R.id.cellNumberText);
        //countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        //simNetworkProvider = (Spinner) findViewById(R.id.simNetworkProvider);
        noOfPartnerDevices = (Spinner) findViewById(R.id.noOfPartnerDevicesSpinner);
        noOfPartnerDevices.setSelection(0);
        noOfPartnerDevices.setSelected(true);
    }

    private void setUserInformation() {
        try{
            Log.i(TAG,"setting user information");
            //TODO add validation to all fields
            DaoSession daoSession = OmniLocateApplication.getSession(DeviceRegistrationActivity.this);//initializing dao session
            //setting unique identifier for phone, it will be deleted upon reset of phone
            //TODO if user is already registered with no phone id, then regenerate this ID and map it back to the user
            Long phoneId = Long.parseLong(String.valueOf(System.currentTimeMillis())
                    .substring(1,10));
            String cellNum = cellNumEt.getText().toString();
            String selectedCountry = countrySpinner.getSelectedItem().toString();
            String selectedSimNetworkProvider = simNetworkProviderSpinner.getSelectedItem().toString();

            Phone phone = new Phone(phoneId,cellNum,selectedCountry, Constants.DeviceStatus.NOT_STOLEN.toString(),partnerDevicesCellNumbers);//partner devices number not set here
            User user = new User(emailEt.getText().toString(),passwordEt.getText().toString(),phone);//TODO hash password in database
            SimCard simCard = new SimCard(phoneId,cellNum, cellNum.substring(0,2),0.00,simNetworkDao.findByValue(selectedSimNetworkProvider,selectedCountry).getId());
            //persisting user to the omnilocate database.
            //TODO validate user i.e. if user already exits give appropriate message and refer user to the remember me functionality
            Log.i(TAG,"persisting user to the omnisecurity database");
            UserDao userDao = daoSession.getUserDao();
            userDao.insert(user);
            phone.setUserId(userDao.getKey(user));
            daoSession.insert(phone);//TODO create phoneDAO and replace
            simCardDao.insert(simCard);

            //persist userId and generated phone if to the device
            PhoneManager phoneManager = new PhoneManager(this);
            phoneManager.registerDevice(phoneId.toString()
                    ,userDao.getKey(user).toString()
                    ,String.valueOf(simCardDao.findByValue(cellNum).getId()));

            Log.i(TAG,"User ID = "+user.getId() + " + " + "Phone ID " + phone.getId() +" added successfully");
            Toast.makeText(this,"User Successfully Registered",Toast.LENGTH_LONG);

            launchLoginActivity();
        }catch (Exception Ex){
            Log.e(TAG,Ex.getMessage());
            Ex.printStackTrace();
            // TODO: 3/16/2017  highlight which input has failed validation
        }

    }

    private void launchLoginActivity() {
        Log.d(TAG,"launchLoginActivity : init");
        Intent loginIntent = new Intent(this,LoginActivity.class);
        Log.d(TAG,"launchLoginActivity : exit");
        startActivity(loginIntent);
    }


}
