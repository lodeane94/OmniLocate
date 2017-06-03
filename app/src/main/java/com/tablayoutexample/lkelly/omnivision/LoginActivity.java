package com.tablayoutexample.lkelly.omnivision;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import  android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.omnivision.core.Constants;
import com.omnivision.core.DaoSession;
import com.omnivision.core.User;
import com.omnivision.core.UserDao;
import com.omnivision.utilities.PhoneManager;

import org.greenrobot.greendao.query.QueryBuilder;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "omnisecurity.login" ;
    Button registerDeviceBtn;
    Button loginBtn;
    EditText emailEt;
    EditText passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"login activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
            DaoSession daoSession = OmniLocateApplication.getSession(LoginActivity.this);//initializing dao session
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
                com.omnivision.utilities.SessionManager sessionManager = new com.omnivision.utilities.SessionManager(this);
                PhoneManager phoneManager = new PhoneManager(this);
                sessionManager.createLoginSession(user.getId().toString()
                        ,user.getEmail()
                        ,phoneManager.getPhoneDetails().get(Constants.SessionManager.PHONE_ID)
                        ,phoneManager.getPhoneDetails().get(Constants.SessionManager.SIM_ID));
                //start main activity upon valid credentials
                launchMainActivity();
            }else{
                Toast.makeText(this,"Invalid email/password combination",Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Log.e(TAG,ex.getMessage());
            ex.printStackTrace();
        }

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
