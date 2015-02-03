package com.nexhealth.googlefit.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by waleed on 1/30/15.
 */
public class StepCounterService extends Service{


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
