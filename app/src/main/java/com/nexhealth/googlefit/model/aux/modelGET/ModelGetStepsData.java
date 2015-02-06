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

    public class Data implements Serializable {
        @SerializedName("exercises")
        public Exercise[] exercises;
        @SerializedName("total_steps")
        public int totalSteps;
        @SerializedName("weight_loss")
        public boolean weightLoss;
        @SerializedName("average_calories_burned")
        public int averageCaloriesBurned;
        @SerializedName("total_cycle_distance")
        public int totalCycleDistance;
        @SerializedName("how_many_days_data")
        public int howManyDaysData;
        @SerializedName("total_running_distance")
        public int totalRunningDistance;
    }

    public class Exercise implements Serializable {
        @SerializedName("id")
        public int id;
        @SerializedName("user_id")
        public int userId;
        @SerializedName("steps")
        public int steps;
        @SerializedName("miles")
        public double miles;
        @SerializedName("calories_burned")
        public double caloriesBurned;
        @SerializedName("minutes")
        public int minutes;
        @SerializedName("created_at")
        public String createdAt;
        @SerializedName("updated_at")
        public String updatedAt;
        @SerializedName("distance_cycled")
        public double distanceCycled;
        @SerializedName("steps_in_miles")
        public double stepsInMiles;

    }


}
