package com.nexhealth.googlefit.networks;

/**
 * Created by waleed on 1/31/15.
 */
import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.nexhealth.googlefit.utils.ConstantKey;
import org.apache.http.entity.StringEntity;

/**
 * Created by kyaw on 8/22/14.
 */

public class AdapterRESTClient {
    public static AsyncHttpClient syncClient = new SyncHttpClient();
    public static AsyncHttpClient asyncClient = new AsyncHttpClient();


    private static final int DEFAULT_TIMEOUT  = 60*1000;


    private static final String BASE_URL = ConstantKey.developmentUrl;
    public static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().get(url, params, responseHandler);
    }

    public static void post(String url, StringEntity stringEntity, AsyncHttpResponseHandler responseHandler) {
        getClient().setTimeout(DEFAULT_TIMEOUT);
        getClient().setMaxRetriesAndTimeout(1, DEFAULT_TIMEOUT);
        getClient().post(null, getAbsoluteUrl(url), stringEntity, "application/json", responseHandler);
    }

    public static void post(String url,final  RequestParams requestParams, AsyncHttpResponseHandler responseHandler) {
        getClient().setTimeout(DEFAULT_TIMEOUT);
        getClient().setMaxRetriesAndTimeout(1, DEFAULT_TIMEOUT);
        getClient().post(getAbsoluteUrl(url), requestParams, responseHandler);
    }

    public static void postShortTimeOut(String url,final  RequestParams requestParams, AsyncHttpResponseHandler responseHandler) {

        getClient().post( getAbsoluteUrl(url), requestParams, responseHandler);
    }

    public static void postNutritionToNx(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        getClient().post(url, params, responseHandler);
    }
    public static void postNutritionToNxStringE(String url, StringEntity params, AsyncHttpResponseHandler responseHandler){
        getClient().post(null, url, params,"application/json", responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static AsyncHttpClient getClient()
    {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return syncClient;
        return asyncClient;
    }


}
