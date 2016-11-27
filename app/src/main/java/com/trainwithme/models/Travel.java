package com.trainwithme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mruga on 19.11.2016.
 */
@Data
public class Travel {
    @SerializedName("Id")
    @Expose
    String Id;
    @SerializedName("StartTime")
    @Expose
    private String startTime;
    @SerializedName("StartAddress")
    @Expose
    private String departureStation;
    @SerializedName("EndAddress")
    @Expose
    private String finalStation;
    @SerializedName("FinishTime")
    @Expose
    private String FinishTime;
}
