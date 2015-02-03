package com.nexhealth.googlefit.utils;

        import android.content.Context;
        import android.content.SharedPreferences;

/**
 * Created by kyawthan on 1/3/15.
 */
public class Settings {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public Settings(Context context) {
        sharedPreferences = context.getSharedPreferences("com.nexHealth.settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public int getInterval() {
        return sharedPreferences.getInt("interval", 60000);
    }

    public boolean getCurrentlyTracking() {
        return sharedPreferences.getBoolean("currently_tracking", true);
    }

    //public void setMealBreakfast(ModelMeal){editor.putString()}



    public String   getRememberToken() {
        return sharedPreferences.getString("remember_token", "");
    }

    public String   getUsername() {
        return sharedPreferences.getString("username", "");
    }


    public String   getPassword() {
        return sharedPreferences.getString("password", "");
    }

    public int      getAppVersion() {
        return sharedPreferences.getInt("app_version", 0);
    }


    public boolean  isPushNotificationPending() {
        return sharedPreferences.getBoolean("is_push_notification_pending", false);
    }

    public boolean  isLoggedIn() {
        return sharedPreferences.getBoolean("is_logged_in", false);
    }
    public boolean  isCodeTrue(){ return sharedPreferences.getBoolean("code", false);}



    public void     setCurrentlyTracking(boolean currentlyTracking) {
        editor.putBoolean("currently_tracking", currentlyTracking).commit();
    }

    public void     setLatitude(String latitude) {
        editor.putString("latitude", latitude).commit();
    }

    public void     setLongitude(String longitude) {
        editor.putString("longitude", longitude).commit();
    }

    public void     setRememberToken(String rememberToken) {
        editor.putString("remember_token", rememberToken).commit();
    }


    public void     setGCMRegID(String regID) {
        editor.putString("gcm_regid", regID).commit();
    }
    public void     setAppVersion(int version) {
        editor.putInt("app_version", version).commit();
    }
    public void     setPassword(String password) {
        editor.putString("password", password).commit();
    }

    //setter and getter for First Name
    public void     setUsername(String username) {
        editor.putString("username", username).commit();
    }
    public void     setFirstName(String firstName){editor.putString("first_name", firstName).commit();}
    public void     setLastName(String lastName){editor.putString("last_name", lastName).commit();}
    public void     setCondition(String condition){editor.putString("condition", condition).commit();}

    public String   getFirstName(){return sharedPreferences.getString("first_name", "");}
    public String   getLastName(){return sharedPreferences.getString("last_name", "");}
    public String   getCondition(){return  sharedPreferences.getString("conditon", "");}

    public void     setPushNotificationPending(boolean isPushNotificationPending) {
        editor.putBoolean("is_push_notification_pending", isPushNotificationPending).commit();
    }

    public void     setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean("is_logged_in", isLoggedIn).commit();
    }

    public void     setCode(boolean b){
        editor.putBoolean("code", b).commit();
    }

    public void     setCodeFromGlucoseList(boolean b){editor.putBoolean("codeFromGlucose", b).commit();}
    public void     setMealListSize(int i){ editor.putInt("MealListSize", i).commit();}


    // Doctor Information setter
    public void     setMyDoctor(boolean b){editor.putBoolean("myDoctorExist", b).commit();}
    public void     setMyDoctorFirstName(String firstName){editor.putString("doc_first_name", firstName).commit();}
    public void     setMyDoctorLastName(String lastName){editor.putString("doc_last_name", lastName).commit();}
    public void     setMyDoctorEmail(String email){editor.putString("doc_email", email).commit();}
    public void     setMyDoctorAddress(String address){editor.putString("doc_address", address).commit();}
    public void     setMyDoctorAccessCode(String accessCode){editor.putString("doc_access_code", accessCode).commit();}
    public void     setMyDoctorNote(String str){editor.putString("doctor_note", str).commit();}

    // Doctor Information getter
    public String   getMyDoctorFirstName(){ return sharedPreferences.getString("doc_first_name", "");}
    public String   getMyDototLastName(){ return  sharedPreferences.getString("doc_last_name", "");}
    public String   getMyDoctorEmail(){return sharedPreferences.getString("doc_email","");}
    public String   getMyDoctorAddress(){return sharedPreferences.getString("doc_address", "");}
    public String   getMyDoctorAccessCode (){return  sharedPreferences.getString("doc_access_code", "");}
    public String   getMyDoctorNote(){return  sharedPreferences.getString("doctor_note", "Welcome to NexHealth");}
    public boolean  isMyDoctorExist(){return sharedPreferences.getBoolean("myDoctorExist", false);}

    // goal setter
    public void     setGlucoseAverage (int average){editor.putInt("glucoseAvg", average).commit();}
    public void     setGlucoseGoalBeforeMeal(float lg){editor.putFloat("beforeMeal", lg).commit();}
    public void     setGlucoseGoalAfterMeal(float lg){editor.putFloat("afterMeal", lg).commit();}
    public void     setA1c(float lg){editor.putFloat("a1c", lg).commit();}

    // goal getter
    public int      getAverageGlucose(){return  sharedPreferences.getInt("glucoseAvg", 0);}
    public float    getGlucoseGoalBeforeMeal(){ return  sharedPreferences.getFloat("beforeMeal", 0);}
    public float    getGlucoseGoalAfterMeal(){ return  sharedPreferences.getFloat("afterMeal", 0);}
    public float    getA1c(){return  sharedPreferences.getFloat("a1c", 0);}

    // General Information
    public void     setHeight(float height) {editor.putFloat("height", height).commit();}
    public void     setWeight(int weight){editor.putInt("weight", weight).commit();}
    public void     setSex(String sex){editor.putString("sex", sex).commit();}

    public float    getHeight(){ return  sharedPreferences.getFloat("height", 0);}
    public int      getWeight(){ return sharedPreferences.getInt("weight", 0);}
    public String   getSex(){ return sharedPreferences.getString("sex", "male");}


    public void     setStepCounterServiceStatus(boolean b){editor.putBoolean("running", b).commit();}
    public boolean getStepCounterServiceStatus(){return sharedPreferences.getBoolean("running", false);}


    // step counter tmp
    public void setCurrentStep(int step){editor.putInt("currentStep", step).commit();}
    public int getCurrentStep(){return sharedPreferences.getInt("currentStep", 0);}


    public void setHourly0(int step){editor.putInt("12am",step).commit();}
    public void setHourly1(int step){editor.putInt("1am",step).commit();}
    public void setHourly2(int step){editor.putInt("2am",step).commit();}
    public void setHourly3(int step){editor.putInt("3am",step).commit();}
    public void setHourly4(int step){editor.putInt("4am",step).commit();}
    public void setHourly5(int step){editor.putInt("5am",step).commit();}
    public void setHourly6(int step){editor.putInt("6am",step).commit();}
    public void setHourly7(int step){editor.putInt("7am",step).commit();}
    public void setHourly8(int step){editor.putInt("8am",step).commit();}
    public void setHourly9(int step){editor.putInt("9am",step).commit();}
    public void setHourly10(int step){editor.putInt("10am",step).commit();}
    public void setHourly11(int step){editor.putInt("11am",step).commit();}
    public void setHourly12(int step){editor.putInt("12pm",step).commit();}
    public void setHourly13(int step){editor.putInt("1pm",step).commit();}
    public void setHourly14(int step){editor.putInt("2pm",step).commit();}
    public void setHourly15(int step){editor.putInt("3pm",step).commit();}
    public void setHourly16(int step){editor.putInt("4pm",step).commit();}
    public void setHourly17(int step){editor.putInt("5pm",step).commit();}
    public void setHourly18(int step){editor.putInt("6pm",step).commit();}
    public void setHourly19(int step){editor.putInt("7pm",step).commit();}
    public void setHourly20(int step){editor.putInt("8pm",step).commit();}
    public void setHourly21(int step){editor.putInt("9pm",step).commit();}
    public void setHourly22(int step){editor.putInt("10pm",step).commit();}
    public void setHourly23(int step){editor.putInt("11pm",step).commit();}

    public int getHourlySteps1(){return sharedPreferences.getInt("1am", 0);}
    public int getHourlySteps2(){return sharedPreferences.getInt("2am", 0);}
    public int getHourlySteps3(){return sharedPreferences.getInt("3am", 0);}
    public int getHourlySteps4(){return sharedPreferences.getInt("4am", 0);}
    public int getHourlySteps5(){return sharedPreferences.getInt("5am", 0);}
    public int getHourlySteps6(){return sharedPreferences.getInt("6am", 0);}
    public int getHourlySteps7(){return sharedPreferences.getInt("7am", 0);}
    public int getHourlySteps8(){return sharedPreferences.getInt("8am", 0);}
    public int getHourlySteps9(){return sharedPreferences.getInt("9am", 0);}
    public int getHourlySteps10(){return sharedPreferences.getInt("10am", 0);}
    public int getHourlySteps11(){return sharedPreferences.getInt("11am", 0);}
    public int getHourlySteps12(){return sharedPreferences.getInt("12pm", 0);}
    public int getHourlySteps13(){return sharedPreferences.getInt("1pm", 0);}
    public int getHourlySteps14(){return sharedPreferences.getInt("2pm", 0);}
    public int getHourlySteps15(){return sharedPreferences.getInt("3pm", 0);}
    public int getHourlySteps16(){return sharedPreferences.getInt("4pm", 0);}
    public int getHourlySteps17(){return sharedPreferences.getInt("5pm", 0);}
    public int getHourlySteps18(){return sharedPreferences.getInt("6pmm", 0);}
    public int getHourlySteps19(){return sharedPreferences.getInt("7pm", 0);}
    public int getHourlySteps20(){return sharedPreferences.getInt("8pm", 0);}
    public int getHourlySteps21(){return sharedPreferences.getInt("9pm", 0);}
    public int getHourlySteps22(){return sharedPreferences.getInt("10pm", 0);}
    public int getHourlySteps23(){return sharedPreferences.getInt("11pm", 0);}
    public int getHourlySteps0(){return sharedPreferences.getInt("12am", 0);}


    // Profile Image Url setter
    public void setDoctorProfileImageUrl(String url){editor.putString("docProfileUrl", url).commit();}
    public void setPatientProfilImageeUrl(String url){editor.putString("patientProfileUrl", url).commit();}

    //Profile Image Url Getter

    public String getDoctorProfileImageUrl(){return sharedPreferences.getString("docProfileUrl", "https://s3.amazonaws.com/nexhealth-images/data.png");}
    public String getPatientProfileImageUrl(){return sharedPreferences.getString("patientProfileUrl", "https://s3.amazonaws.com/nexhealth-images/data.png");}



    // exercise goal setter

    public void setCaloriesBurnGoal(int goal){editor.putInt("calories_burn_goal", goal).commit();}
    public void setWeightGoal(float goal){editor.putFloat("weight_goal", goal).commit();}
    public void setBodyFatPercentageGoal(float goal){editor.putFloat("body_fat", goal).commit();}
    public void setMilesRanGoal(int goal){editor.putInt("goal_mile_to_ran", goal).commit();}
    public void setMilesBikedGoal(int goal){editor.putInt("mile_bike_goal", goal).commit();}

    //exercise goal getter

    public int getCaloriesBurnGoal(){return sharedPreferences.getInt("calories_burn_goal", 0);}
    public float getWeightGoal(){return sharedPreferences.getFloat("weight_goal", 0);}
    public float getBodyfatGoal(){return sharedPreferences.getFloat("body_fat", 0);}
    public int getMileToRanGoal(){return sharedPreferences.getInt("goal_mile_to_ran",0);}
    public int getMilesBikedGoal(){return sharedPreferences.getInt("mile_bike_goal", 0);}


    public void clearAll() {
        editor.clear();
        editor.commit();

//        setLoggedIn(false);
//        setUsername("");
//        setPassword("");
//        setLatitude("");
//        setLongitude("");
//        setRememberToken("");
//        setFirstName("");
//        setLastName("");
//        setCondition("");
//        setMyDoctorAccessCode("");
//        setMyDoctorFirstName("");
//        setMyDoctorLastName("");
//
//        setLoggedIn(false);
//        setCurrentlyTracking(false);
//        setMyDoctor(false);
//        setCode(false);
//        setLoggedIn(false);
//        resetUserInfo();

    }



}

