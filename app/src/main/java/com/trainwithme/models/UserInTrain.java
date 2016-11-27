package com.trainwithme.models;

/**
 * Created by mruga on 19.11.2016.
 */
import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class UserInTrain {

    @SerializedName("UserId")
    @Expose
    private String id;
    @SerializedName("UserAvatar")
    @Expose
    private String encodedAvatar;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("StartAddress")
    @Expose
    private String startAddress;

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    @SerializedName("EndAddress")
    @Expose
    private String endAddress;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The Id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The encodedAvatar
     */
    public String getEncodedAvatar() {
        return encodedAvatar;
    }

    /**
     *
     * @param encodedAvatar
     * The EncodedAvatar
     */
    public void setEncodedAvatar(String encodedAvatar) {
        this.encodedAvatar = encodedAvatar;
    }

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
     * The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName
     * The UserName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

}