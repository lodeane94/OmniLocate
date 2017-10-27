package com.omnivision.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.omnivision.utilities.Authenticator;

/**
 * Created by Lodeane on 27/8/2017.
 * A bound Service that instantiates the authenticator
 * when started.
 */

public class AuthenticatorService extends Service {
    //instance that stores authenticator object
    private Authenticator mAuthenticator;

    @Override
    public void onCreate(){
        mAuthenticator = new Authenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
