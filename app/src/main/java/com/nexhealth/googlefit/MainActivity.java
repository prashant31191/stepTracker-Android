package com.nexhealth.googlefit;

import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataDeleteRequest;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.nexhealth.googlefit.database.DatabaseHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;

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
import com.nexhealth.googlefit.utils.Settings;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        OnDataPointListener,
        InterfaceGetAllSteps{



    Button mySimpleStep;

    /*
    * First have user sign in and confirm subscription. Then show steps for current day.
    * on click of button, send data to server.
    *u
    * 1. User opens the app, then is sent to login screen. If already authorized, continue to main.
    * 2. App contacts Google for latest data. We need cumulative steps, calories burned for the day.
    * 3. If User clicks on exercise fragment, then we need to request all data in specified timeframe. Use ListView.
    * 4. In the backend, we will assign a new OAuth token field to the User model and specify which api it is from.
    * 5. Then setup a cron job to retrieve the latest data for android users everyday.
    * 6. Setup exercise fields in the backend.
    * 7. Need to make a live sensor when app is opened to update steps in view.
    */

    private static final int REQUEST_OAUTH = 1001;
    private GoogleApiClient mGoogleApiClient;
    private boolean authInProgress = false;
    private static final String AUTH_PENDING = "auth_state_pending";

    private DatabaseHelper databaseHelperStep;
    private SQLiteDatabase database;

    private static final String TAG = "StepCounterService";
    private static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
    Settings settings ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adapter_step_list_view);

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }
        settings = new Settings(this);
        setupGoogleFit();
        getAllStepsData();
        mySimpleStep = (Button) findViewById(R.id.simpleStep);
        mySimpleStep.setText(String.valueOf(settings.getCurrentStep()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cancel_subs) {
            cancelSubscription();
            return true;
        } else if (id == R.id.action_dump_subs) {
            dumpSubscriptionsList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        subscribe();
        new InsertAndVerifyDataTask().execute();

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
            mGoogleApiClient, DataType.TYPE_STEP_COUNT_CUMULATIVE)
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

    private void cancelSubscription() {
        final String dataTypeStr = DataType.TYPE_ACTIVITY_SAMPLE.toString();
        Log.i(TAG, "Unsubscribing from data type: " + dataTypeStr);

        // Invoke the Recording API to unsubscribe from the data type and specify a callback that
        // will check the result.
        // [START unsubscribe_from_datatype]
        Fitness.RecordingApi.unsubscribe(mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Successfully unsubscribed for data type: " + dataTypeStr);
                        } else {
                            // Subscription not removed
                            Log.i(TAG, "Failed to unsubscribe for data type: " + dataTypeStr);
                        }
                    }
                });
        // [END unsubscribe_from_datatype]
    }

    private void dumpSubscriptionsList() {
        // [START list_current_subscriptions]
        Fitness.RecordingApi.listSubscriptions(mGoogleApiClient, null)
                // Create the callback to retrieve the list of subscriptions asynchronously.
                .setResultCallback(new ResultCallback<ListSubscriptionsResult>() {
                    @Override
                    public void onResult(ListSubscriptionsResult listSubscriptionsResult) {
                        for (Subscription sc : listSubscriptionsResult.getSubscriptions()) {
                            DataType dt = sc.getDataType();
                            Log.i(TAG, "Active subscription for data type: " + dt.getName());
                        }
                    }
                });
        // [END list_current_subscriptions]
    }

    public void getAllStepsData() {

        new GETAllStepsData(this, this);
    }

    @Override
    public void getAllStepsSucceed(){
        Log.i("get all steps:", "It worked");

    }

    @Override
    public void getAllStepsFailed(){

    }

    /**
     *  Create a {@link DataSet} to insert data into the History API, and
     *  then create and execute a {@link DataReadRequest} to verify the insertion succeeded.
     *  By using an {@link AsyncTask}, we can schedule synchronous calls, so that we can query for
     *  data after confirming that our insert was successful. Using asynchronous calls and callbacks
     *  would not guarantee that the insertion had concluded before the read request was made.
     *  An example of an asynchronous call using a callback can be found in the example
     *  on deleting data below.
     */
    private class InsertAndVerifyDataTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            //First, create a new dataset and insertion request.
            DataSet dataSet = insertFitnessData();

            // [START insert_dataset]
            // Then, invoke the History API to insert the data and await the result, which is
            // possible here because of the {@link AsyncTask}. Always include a timeout when calling
            // await() to prevent hanging that can occur from the service being shutdown because
            // of low memory or other conditions.

//            Log.i(TAG, "Inserting the dataset in the History API");
//            com.google.android.gms.common.api.Status insertStatus =
//                    Fitness.HistoryApi.insertData(mGoogleApiClient, dataSet)
//                            .await(1, TimeUnit.MINUTES);
//
//            // Before querying the data, check to see if the insertion succeeded.
//            if (!insertStatus.isSuccess()) {
//                Log.i(TAG, "There was a problem inserting the dataset. " + insertStatus.getStatusMessage());
//                return null;
//            }
//
//            // At this point, the data has been inserted and can be read.
//            Log.i(TAG, "Data insert was successful!");
            // [END insert_dataset]

            // Begin by creating the query.
            DataReadRequest readRequest = queryFitnessData();

            // [START read_dataset]
            // Invoke the History API to fetch the data with the query and await the result of
            // the read request.
            DataReadResult dataReadResult =
                    Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES);
            // [END read_dataset]

            // For the sake of the sample, we'll print the data so we can see what we just added.
            // In general, logging fitness information should be avoided for privacy reasons.
            printData(dataReadResult);

            return null;
        }
    }

    /**
     * Create and return a {@link DataSet} of step count data for the History API.
     */
    private DataSet insertFitnessData() {
        Log.i(TAG, "Creating a new data insert request");

        // [START build_insert_data_request]
        // Set a start and end time for our data, using a start time of 1 hour before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        long startTime = cal.getTimeInMillis();

        // Create a data source
        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(this)
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setName(TAG + " - step count")
                .setType(DataSource.TYPE_RAW)
                .build();

        // Create a data set
        int stepCountDelta = 1000;
        DataSet dataSet = DataSet.create(dataSource);
        // For each data point, specify a start time, end time, and the data value -- in this case,
        // the number of new steps.
        DataPoint dataPoint = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        dataPoint.getValue(Field.FIELD_STEPS).setInt(stepCountDelta);
        dataSet.add(dataPoint);
        // [END build_insert_data_request]

        return dataSet;
    }

    /**
     * Return a {@link DataReadRequest} for all step count changes in the past week.
     */
    private DataReadRequest queryFitnessData() {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, -2);
        long startTime = cal.getTimeInMillis();
        Log.i("START TIME", new SimpleDateFormat("yyyy.MM.dd").format(startTime));

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                // The data request can specify multiple data types to return, effectively
                // combining multiple data queries into one call.
                // In this example, it's very unlikely that the request is for several hundred
                // datapoints each consisting of a few steps and a timestamp.  The more likely
                // scenario is wanting to see how many steps were walked per day, for 7 days.
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                        // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                        // bucketByTime allows for a time span, whereas bucketBySession would allow
                        // bucketing by "sessions", which would need to be defined in code.
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        // [END build_read_data_request]

        return readRequest;
    }

    /**
     * Log a record of the query result. It's possible to get more constrained data sets by
     * specifying a data source or data type, but for demonstrative purposes here's how one would
     * dump all the data. In this sample, logging also prints to the device screen, so we can see
     * what the query returns, but your app should not log fitness information as a privacy
     * consideration. A better option would be to dump the data you receive to a local data
     * directory to avoid exposing it to other applications.
     */
    private void printData(DataReadResult dataReadResult) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: "
                    + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
        // [END parse_read_data_result]
    }

    // [START parse_dataset]
    private void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        Log.i(TAG, "empty: " + dataSet.getDataPoints());

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for(Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));

                settings.setCurrentStep(dp.getValue(field).asInt());

            }


        }
    }
    // [END parse_dataset]

/*    *//**
     * Delete a {@link DataSet} from the History API. In this example, we delete all
     * step count data for the past 24 hours.
     *//*
    private void deleteData() {
        Log.i(TAG, "Deleting today's step count data");

        // [START delete_dataset]
        // Set a start and end time for our data, using a start time of 1 day before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        //  Create a delete request object, providing a data type and a time interval
        DataDeleteRequest request = new DataDeleteRequest.Builder()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .build();

        // Invoke the History API with the Google API client object and delete request, and then
        // specify a callback that will check the result.
        Fitness.HistoryApi.deleteData(mGoogleApiClient, request)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Successfully deleted today's step count data");
                        } else {
                            // The deletion will fail if the requesting app tries to delete data
                            // that it did not insert.
                            Log.i(TAG, "Failed to delete today's step count data");
                        }
                    }
                });
        // [END delete_dataset]
    }*/

}
