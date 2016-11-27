package com.trainwithme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mruga on 19.11.2016.
 */

public class CurrentTravel {
    @SerializedName("Id")
    @Expose
    String Id;
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

}
