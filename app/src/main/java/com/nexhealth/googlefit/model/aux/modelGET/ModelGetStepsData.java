package com.nexhealth.googlefit.model.aux.modelGET;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by waleed on 2/2/15.
 */
public class ModelGetStepsData implements Serializable{

    @SerializedName("code")
    public boolean code;
    @SerializedName("description")
    public String description;
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("steps")
        public int steps;
        @SerializedName("miles")
        public double miles;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("distance_cycled")
        public double distance_cycled;
        @SerializedName("running_distance")
        public double running_distance;
        @SerializedName("calories_burned")
        public double calories_burned;

    }


}
