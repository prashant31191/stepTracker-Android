package com.nexhealth.googlefit.model.aux;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kyawthan on 1/3/15.
 */
public class ModelResponseCodeStatus implements Serializable {
    @SerializedName("code")
    public boolean code;
}