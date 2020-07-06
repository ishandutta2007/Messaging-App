
package com.google.firebase.quickstart.database.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FCMSend {

    @SerializedName("to")
    @Expose
    public String to;
    @SerializedName("notification")
    @Expose
    public Notification notification;
    @SerializedName("data")
    @Expose
    public Data data;

}
