package com.nexhealth.googlefit.networks.POST;
        import android.content.Context;
        import android.text.format.Time;
        import android.util.Log;

        import com.loopj.android.http.AsyncHttpResponseHandler;
        import com.loopj.android.http.RequestParams;
        import com.nexhealth.googlefit.networks.AdapterRESTClient;
        import com.nexhealth.googlefit.utils.Settings;

        import org.apache.http.Header;

        import java.io.Serializable;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.Date;

/**
 * Created by kyawthan on 1/7/15.
 */
public class POSTStepCounters implements Serializable {

    private Context context;
    //private InterfaceAddStepCounter interfaceAddStepCounter;
    private Settings settings;
    private String TAG = getClass().getSimpleName().toUpperCase();


    public POSTStepCounters(Context context){
        this.context = context;
        //this.interfaceAddStepCounter = interfaceAddStepCounter;
        this.settings = new Settings(context);

        shouldSerializeData();

    }

    private void shouldSerializeData() {


        Time dt = new Time();
        dt.setToNow();
        int currentTimeInHour = dt.hour;
        RequestParams params = new RequestParams();
        params.add("auth[remember_token]", settings.getRememberToken());
        params.put("create_exercise[miles]", 0);
        params.put("create_exercise[calories_burned]", 0);
        params.put("create_exercise[distance_cycled]", 0);
        params.put("create_exercise[running_distance]", 0);
        params.put("create_exercise[minutes]", 0);
        params.put("create_exercise[caloric_goal]", settings.getCaloriesBurnGoal());
        params.put("create_exercise[total_steps]", settings.getCurrentStep());
        // params.put("create_exercise[date]", getCurrentTimeStamp());

        int currentStep = settings.getCurrentStep();

        switch (currentTimeInHour){
            case 0:
                params.put("hourly_data[twelve_am]", currentStep);
                settings.setHourly0(currentStep);
                break;
            case 1:
                params.put("hourly_data[one_am]", currentStep);
                settings.setHourly1(currentStep);
                break;
            case 2:
                params.put("hourly_data[two_am]", currentStep);
                settings.setHourly2(currentStep);
                break;
            case 3:
                params.put("hourly_data[three_am]", currentStep);
                settings.setHourly3(currentStep);
                break;
            case 4:
                params.put("hourly_data[four_am]", currentStep);
                settings.setHourly4(currentStep);
                break;
            case 5:
                params.put("hourly_data[five_am]", currentStep);
                settings.setHourly5(currentStep);
                break;
            case 6:
                params.put("hourly_data[six_am]", currentStep);
                settings.setHourly6(currentStep);
                break;
            case 7:
                params.put("hourly_data[seven_am]", currentStep);
                settings.setHourly7(currentStep);
                break;
            case 8:
                params.put("hourly_data[eight_am]", currentStep);
                settings.setHourly8(currentStep);
                break;
            case 9:
                params.put("hourly_data[nine_am]", currentStep);
                settings.setHourly9(currentStep);
                break;
            case 10:
                params.put("hourly_data[ten_am]", currentStep);
                settings.setHourly10(currentStep);
                break;
            case 11:
                params.put("hourly_data[eleven_am]", currentStep);
                settings.setHourly11(currentStep);
                break;
            case 12:
                params.put("hourly_data[twelve_pm]", currentStep);
                settings.setHourly12(currentStep);
                break;
            case 13:
                params.put("hourly_data[one_pm]", currentStep);
                settings.setHourly13(currentStep);
                break;
            case 14:
                params.put("hourly_data[two_pm]", currentStep);
                settings.setHourly14(currentStep);
                break;
            case 15:
                params.put("hourly_data[three_pm]", currentStep);
                settings.setHourly15(currentStep);
                break;
            case 16:
                params.put("hourly_data[four_pm]", currentStep);
                settings.setHourly16(currentStep);
                break;
            case 17:
                params.put("hourly_data[five_pm]", currentStep);
                settings.setHourly17(currentStep);
                break;
            case 18:
                params.put("hourly_data[six_pm]", currentStep);
                settings.setHourly18(currentStep);
                break;
            case 19:
                params.put("hourly_data[seven_pm]", currentStep);
                settings.setHourly19(currentStep);
                break;
            case 20:
                params.put("hourly_data[eight_pm]", currentStep);
                settings.setHourly20(currentStep);
                break;
            case 21:
                params.put("hourly_data[nine_pm]", currentStep);
                settings.setHourly21(currentStep);
                break;
            case 22:
                params.put("hourly_data[ten_pm]", currentStep);
                settings.setHourly22(currentStep);
                break;
            case 23:
                params.put("hourly_data[eleven_pm]", currentStep);
                settings.setHourly23(currentStep);
                break;
            default:
                break;


        }

        Log.i(TAG, params.toString());
        ShouldContactServer(params);
    }

    private void ShouldContactServer(RequestParams params) {
        AdapterRESTClient.post("/step_trackers/create", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.i(TAG, new String(bytes));
                //interfaceAddStepCounter.didSucceedAddStepCounter(new String(bytes));
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                //interfaceAddStepCounter.didFailedAddStepCounter(new String(bytes));

            }
        });
    }


    private String getCurrentTimeStamp(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return (simpleDateFormat.format(date));

    }


}

