package com.trainwithme.models;

/**
 * Created by mruga on 19.11.2016.
 */

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

@Generated("org.jsonschema2pojo")
public class RegisterForTrainModel {

    @SerializedName("From")
    @Expose
    private String from;
    @SerializedName("To")
    @Expose
    private String to;
    @SerializedName("When")
    @Expose
    private DateTime when;

    /**
     * @return The from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from The From
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return The to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to The To
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return The when
     */
    public DateTime getWhen() {
        return when;
    }

    /**
     * @param when The When
     */
    public void setWhen(DateTime when) {
        this.when = when;
    }

}

