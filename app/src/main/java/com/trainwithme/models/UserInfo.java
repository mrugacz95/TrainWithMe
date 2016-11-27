package com.trainwithme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Seba on 20.11.2016.
 */

public class UserInfo {
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("HasRegistered")
    @Expose
    private Boolean hasRegistered;
    @SerializedName("LoginProvider")
    @Expose
    private String loginProvider;

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The Email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The hasRegistered
     */
    public Boolean getHasRegistered() {
        return hasRegistered;
    }

    /**
     *
     * @param hasRegistered
     * The HasRegistered
     */
    public void setHasRegistered(Boolean hasRegistered) {
        this.hasRegistered = hasRegistered;
    }

    /**
     *
     * @return
     * The loginProvider
     */
    public String getLoginProvider() {
        return loginProvider;
    }

    /**
     *
     * @param loginProvider
     * The LoginProvider
     */
    public void setLoginProvider(String loginProvider) {
        this.loginProvider = loginProvider;
    }

}
