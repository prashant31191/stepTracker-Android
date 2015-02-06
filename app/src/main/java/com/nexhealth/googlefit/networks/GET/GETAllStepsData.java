package com.nexhealth.googlefit.networks.GET;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.nexhealth.googlefit.database.DatabaseHelper;
import com.nexhealth.googlefit.model.aux.ModelResponseCodeStatus;
import com.nexhealth.googlefit.networks.AdapterRESTClient;
import com.nexhealth.googlefit.utils.ConstantKey;
import com.nexhealth.googlefit.utils.Settings;
import com.nexhealth.googlefit.model.aux.modelGET.ModelGetStepsData;


import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.nexhealth.googlefit.interfaces.InterfaceGetAllSteps;

import org.apache.http.Header;

import java.util.Arrays;

/**
 * Created by waleed on 2/2/15.
 */
public class GETAllStepsData implements InterfaceGetAllSteps{

    private String TAG = getClass().getSimpleName().toUpperCase();

    private Context context;
    private InterfaceGetAllSteps interfaceGetAllSteps;
    private Settings settings;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private ModelGetStepsData modelGetStepsData;
    private ProgressDialog progressDialog;

    public GETAllStepsData(Context context, InterfaceGetAllSteps interfaceGetAllSteps){
        this.context = context;
        this.interfaceGetAllSteps = interfaceGetAllSteps;
        settings = new Settings(this.context);
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        showProgressDialog();
        Log.i("HI","Hi");
        shouldRequestAllSteps(ConstantKey.REMEMBER_TOKEN);
    }

    private void shouldRequestAllSteps(String rememberToken) {
        RequestParams params  = new RequestParams();
        params.add("auth[remember_token]", rememberToken);
        params.put("time", 60);
        Log.i("contacting server", "contact?");
        shouldContactServer(params);
    }

    private void shouldContactServer(RequestParams params) {
        AdapterRESTClient.post("/step_trackers/view", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Gson gson = new Gson();
                ModelResponseCodeStatus codeStatus = new ModelResponseCodeStatus();
                codeStatus = gson.fromJson(new String(bytes), ModelResponseCodeStatus.class);
                if (codeStatus.code) {
                    modelGetStepsData = gson.fromJson(new String(bytes), ModelGetStepsData.class);
                    databaseHelper.onUpgrade(database, 1, 1);
                    if (modelGetStepsData != null) {
                        Log.i("success?", "success");
                        shouldDeserialized(modelGetStepsData);
                    }

                } else {
                    Log.i("fail", String.valueOf(i));
                    interfaceGetAllSteps.getAllStepsFailed();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i("fail", String.valueOf(throwable));
                dismissProgressDialog();
                interfaceGetAllSteps.getAllStepsFailed();

            }
        });
    }

    private void shouldDeserialized(ModelGetStepsData modelGetStepsData) {

        Log.e("steps", String.valueOf(modelGetStepsData.data.totalSteps));
        dismissProgressDialog();

    }

    @Override
    public void getAllStepsFailed(){
        dismissProgressDialog();

    }

    @Override
    public void getAllStepsSucceed(){
        dismissProgressDialog();

    }

    public void showProgressDialog(){

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Getting All Data ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }

}
