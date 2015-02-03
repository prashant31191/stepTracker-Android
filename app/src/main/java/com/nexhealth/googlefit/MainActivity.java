package com.nexhealth.googlefit;

import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.request.SensorRequest;
import com.nexhealth.googlefit.database.DatabaseHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.result.ListSubscriptionsResult;
import com.nexhealth.googlefit.interfaces.InterfaceGetAllSteps;
import com.nexhealth.googlefit.networks.GET.GETAllStepsData;



public class MainActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        OnDataPointListener,
        InterfaceGetAllSteps{

    /*
    * First have user sign in and confirm subscription. Then show steps for current day.
    * on click of button, send data to server.
    * */

    private static final int REQUEST_OAUTH = 1001;
    private GoogleApiClient mGoogleApiClient;
    private boolean authInProgress = false;
    private static final String AUTH_PENDING = "auth_state_pending";

    private DatabaseHelper databaseHelperStep;
    private SQLiteDatabase database;

    private static final String TAG = "StepCounterService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adapter_step_list_view);


        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        setupGoogleFit();
        getAllStepsData();




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        subscribe();
    }

    @Override
    public void onDataPoint(DataPoint dataPoint) {

    }

    @Override
    public void onConnectionSuspended(int cause) {
        // If your connection to the sensor gets lost at some point,
        // you'll be able to determine the reason and react to it here.
        if (cause == ConnectionCallbacks.CAUSE_NETWORK_LOST) {
            Log.i(TAG, "Connection lost.  Cause: Network Lost.");
        } else if (cause == ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
            Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        if (result.getErrorCode() == FitnessStatusCodes.NEEDS_OAUTH_PERMISSIONS) {
            try {
                result.startResolutionForResult(this, REQUEST_OAUTH);
            } catch (IntentSender.SendIntentException e) {
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OAUTH && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        }
    }

    private void setupGoogleFit(){
        // Create a Google Fit Client instance with default user account.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.API)
                .useDefaultAccount()
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    // Called whenever the API client fails to connect.
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.i(TAG, "Connection failed. Cause: " + result.toString());
                        if (!result.hasResolution()) {
                            // Show the localized error dialog
                            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
                                    MainActivity.this, 0).show();
                            return;
                        }
                        // The failure has a resolution. Resolve it.
                        // Called typically when the app is not yet authorized, and an
                        // authorization dialog is displayed to the user.
                        if (!authInProgress) {
                            try {
                                Log.i(TAG, "Attempting to resolve failed connection");
                                authInProgress = true;
                                result.startResolutionForResult(MainActivity.this,
                                        REQUEST_OAUTH);
                            } catch (IntentSender.SendIntentException e) {
                                Log.e(TAG,
                                        "Exception while starting resolution activity", e);
                            }
                        }
                    }
                })
                .build();

        mGoogleApiClient.connect();

    }

    public void subscribe(){

        Fitness.RecordingApi.subscribe(
                mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            if (status.getStatusCode()
                                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                Log.i(TAG, "Existing subscription for activity detected.");
                            } else {
                                Log.i(TAG, "Successfully subscribed!");
                            }
                        } else {
                            Log.i(TAG, "There was a problem subscribing.");
                        }
                    }
                });
    }

    public void getAllStepsData() {

        new GETAllStepsData(this, this);
    }

    public void getAllStepsSucceed(){
        Log.i(TAG, "It worked");

    }

    public void getAllStepsFailed(){

    }
}
