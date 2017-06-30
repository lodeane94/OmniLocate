package com.omnivision.Services;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.omnivision.core.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by lkelly on 4/2/2017.
 */

public class FetchAddressIntentService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private String TAG = "FetchAddressIS";
    protected ResultReceiver mReceiver;
    private Location mLastLocation;
    private Boolean mAddressRequested = false;
    private GoogleApiClient mGoogleApiclient;
    //private Context context;
    private Intent intent;
    private Geocoder geocoder;
    private LocationRequest mLocationRequest;
    private LocationListenerProvider locationListenerProvider;

    public FetchAddressIntentService(){
        super("FetchAddressIntentService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG,"onHandleIntent : init");
        this.intent = intent;
        mReceiver = intent.getParcelableExtra(Constants.Location.RECEIVER);
        buildGoogleApiClient();

        //context = (Context)intent.getSerializableExtra(Constants.General.CONTEXT.name());
        /*connecting google api client if it is not connected already*/
        if(!mGoogleApiclient.isConnected()){
            mGoogleApiclient.connect();
        }

        geocoder = new Geocoder(this, Locale.getDefault());

        Log.i(TAG,"deliverResultToReceiver : exit");
    }
    /**
     * @author lkelly
     * @desc builds the googleapiclient, adding necessary callbacks - is used for the location services
     * @params
     * @return
     * */
    private synchronized void buildGoogleApiClient() {
        if(mGoogleApiclient == null) {
            mGoogleApiclient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
    /**
     * @author lkelly
     * @desc
     * @params
     * @return
     * */
    private void deliverResultToReceiver(HashMap addressMap) {
        Log.i(TAG,"deliverResultToReceiver : init");
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.Location.RESULT_DATA_KEY,addressMap);
        Log.i(TAG,"deliverResultToReceiver : exit");
        mReceiver.send(Integer.parseInt((String) addressMap.get(Constants.General.RECEIVER_RESULT_MESSAGE.name())),bundle);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG,"onConnected GoogleAPIClient: init");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //TODO throw exception permission not allowed
            return;
        }

        /*mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiclient);

        if(mLastLocation != null){
            //determine whether a geocoder is available

            processDeviceLocation(mLastLocation);
        }*/
        locationListenerProvider = new LocationListenerProvider();
        locationListenerProvider.startLocationUpdate();
        Log.i(TAG,"onConnected GoogleAPIClient: exit");
    }

    /**
     * @author lkelly
     * @desc method is used access information about the location found
     * @params location
     * @return
     * */
    private void processDeviceLocation(Location location) {
        String errorMessage = "";
        HashMap<String,String> locationMap = new HashMap<>();

        //Gets network's availability information from the requester
        boolean hasNetwork = intent.getBooleanExtra(Constants.NetworkConnection.CONNECTION_AVAILABILITY.name(),false);
        List<Address> addresses = null;

        try{
            if(location == null)
                throw new NullPointerException("location could not be retrieved");
            /*
            * if no network connection is found then send only the latitude and longitude
            * to the caller else it should send those coordinates as well as the address name*/
            hasNetwork = false;//TODO testing purpose
            if(hasNetwork && Geocoder.isPresent()) {
                Log.i(TAG,"Network available! getting address line along with coordinates");
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            }else{
                Log.i(TAG,"Network unavailable ! getting only coordinates");
                locationMap.put(Constants.Location.LONGITUDE,String.valueOf(location.getLongitude()));
                locationMap.put(Constants.Location.LATITUDE,String.valueOf(location.getLatitude()));
                locationMap.put(Constants.General.RECEIVER_RESULT_MESSAGE.name(), String.valueOf(Constants.Location.SUCCESS_RESULT));
                Log.i(TAG,"deliverResultToReceiver : exit");
                deliverResultToReceiver(locationMap);
                return;
            }
        }catch (IOException ioException){
            //catching network or any other I/O exceptions
            errorMessage = Constants.Location.ErrorMessages.SERVICE_NOT_AVAILABLE.getMessage();
            Log.e(TAG,errorMessage);
        }catch (IllegalArgumentException illegalArgumentException){
            errorMessage = Constants.Location.ErrorMessages.INVALID_COORODINATES.getMessage();
            Log.e(TAG,errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }catch (NullPointerException nullPointerException){
            errorMessage = nullPointerException.getMessage();
            Log.e(TAG,errorMessage);
        }finally {
            locationListenerProvider.stopLocationUpdate();
        }

        //Handle case where no address is found
        if(addresses == null || addresses.size() == 0){
            if (errorMessage.isEmpty()) {
                errorMessage = Constants.Location.ErrorMessages.ADDRESS_NOT_FOUND.getMessage();
                Log.e(TAG, errorMessage);
            }
            locationMap.put(Constants.General.RECEIVER_RESULT_MESSAGE.name(), String.valueOf(Constants.Location.FAILURE_RESULT));
            //deliverResultToReceiver(locationMap);
        }else{
            String addressLine = "";
            Address address = addresses.get(0);
            locationMap.put(Constants.Location.LONGITUDE,String.valueOf(address.getLongitude()));
            locationMap.put(Constants.Location.LATITUDE,String.valueOf(address.getLatitude()));
            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++){
                addressLine += address.getAddressLine(i) + " ";
            }
            locationMap.put(Constants.General.RECEIVER_RESULT_MESSAGE.name(), String.valueOf(Constants.Location.SUCCESS_RESULT));
            locationMap.put(Constants.Location.ADDRESS_LINE,addressLine);
            Log.i(TAG,Constants.Location.ADDRESS_FOUND);
            //deliverResultToReceiver(locationMap);
        }
        deliverResultToReceiver(locationMap);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG,connectionResult.getErrorMessage());
    }

    private class LocationListenerProvider implements LocationListener{
        /*this will be used to check whether onLocationChanged was called
        * if it was called then it is set to true hence the location requester will
        * receive the latest recent location. if it is false then it will receive the last known
        * location*/
        private boolean latestLocationChecked = false;
        private Location latestLocation = null;
        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "lat " + location.getLatitude());
            Log.i(TAG, "lng " + location.getLongitude());

            latestLocationChecked = true;
            latestLocation = location;
            //processDeviceLocation(location);removed since this will be called as soon as the location changes
        }


        private void startLocationUpdate(){
            initLocationRequest();

            if (ActivityCompat.checkSelfPermission(FetchAddressIntentService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FetchAddressIntentService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiclient,mLocationRequest,this);

            if(!latestLocationChecked){//onLocationChange did not fire i.e. location did not change
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiclient);
                processDeviceLocation(mLastLocation);
            }else if(latestLocation != null){
                processDeviceLocation(latestLocation);
            }
        }

        private void initLocationRequest() {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(2000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        private void stopLocationUpdate(){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiclient,this);
        }
    }
}
