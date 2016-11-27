package com.trainwithme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by mruga on 19.11.2016.
 */
@Data
public class PeopleInTrain {
    @SerializedName("MatchedUsers")
    @Expose
    List<UserInTrain> userInTrain;
    @SerializedName("Travel")
    @Expose
    Travel travel;
}
