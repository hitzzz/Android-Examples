package com.example.hitesh.cameraapplication;

/**
 * Created by hitesh on 11/21/15.
 */

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ajoy3 on 11/20/2015.
 */
public class Servicing extends IntentService {
    //ResultReceiver class allows you to send a numeric result code and a message containing
    //result data
    protected ResultReceiver mReceiver;

    public Servicing() {

        super("Servicing");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";
        //Geocoder class for reverse geocoding....get address from lat/long
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        if (intent != null) {
            mReceiver = intent.getParcelableExtra(Constanting.RECEIVER);
            // Get the location passed to this service through an extra.
            Location location = intent.getParcelableExtra(Constanting.LOCATION_DATA_EXTRA);
            Log.i("RevGeoLatLng", location.toString());
            List<Address> addressList = null;
            try{
                addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),2);
            }catch(IOException ioException){
                // Catch network or other I/O problems.
                Log.e("Geocoder IO Exception", errorMessage, ioException);
            }catch (IllegalArgumentException illegalArgumentException){
                // Catch invalid latitude or longitude values.
                Log.e("Lat/Lon Invalid", errorMessage + ". " +
                        "Latitude = " + location.getLatitude() +
                        ", Longitude = " + location.getLongitude(), illegalArgumentException);
            }
            // Handle case where no address was found.
            if(addressList == null || addressList.size() == 0){
                if(errorMessage.isEmpty()){
                    Log.e("Empty Address", errorMessage);
                }
                deliverResultToReceiver(Constanting.FAILURE_RESULT, errorMessage);
            } else {
                Address address1 = addressList.get(0);
                //Address address2 = addressList.get(1);
                //address 3 not accurate on testing
                //Address address3 = addressList.get(2);
                ArrayList<String> addressFragments1 = new ArrayList<>();
                //ArrayList<String> addressFragments2 = new ArrayList<>();
                //ArrayList<String> addressFragments3 = new ArrayList<>();

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread.
                for(int i = 0; i < address1.getMaxAddressLineIndex(); i++) {
                    addressFragments1.add(address1.getAddressLine(i));
                }
                /*
                for(int i = 0; i < address2.getMaxAddressLineIndex(); i++) {
                    addressFragments2.add(address2.getAddressLine(i));
                }*/
                /*for(int i = 0; i < address3.getMaxAddressLineIndex(); i++) {
                    addressFragments3.add(address3.getAddressLine(i));
                }*/
                deliverResultToReceiver(Constanting.SUCCESS_RESULT,
                        TextUtils.join(System.getProperty("line.separator"), addressFragments1));
                //TextUtils.join(System.getProperty("line.separator"), addressFragments2));
                //TextUtils.join(System.getProperty("line.separator"), addressFragments3)
            }
        }
    }

    private void deliverResultToReceiver(int resultCode, String address1){
        Bundle bundle = new Bundle();
        //put results in bundle and send to main UI thread.
        Log.i("RevGeo",address1);
        bundle.putString(Constanting.RESULT_DATA_KEY_1,address1);
        //bundle.putString(Constanting.RESULT_DATA_KEY_2,address2);
        //bundle.putString(Constanting.RESULT_DATA_KEY_3,address3);
        mReceiver.send(resultCode,bundle);
    }
}


