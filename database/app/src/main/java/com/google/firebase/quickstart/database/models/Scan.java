package com.google.firebase.quickstart.database.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by john on 11/1/16.
 */
@IgnoreExtraProperties
public class Scan {

    public String firebaseId;
    public boolean processed;
    public boolean cleared;
    public String deviceid;
    public String direction;
    public String location;
    public String scantime;
    public String siteid;
    public String workerid;
    public String workername;

    public Scan() {
        //default constructor
    }

    public Scan(String firebaseId, boolean processed, boolean cleared, String deviceid, String direction, String location,
                String scantime, String siteid, String workerid, String workername) {
        this.firebaseId = firebaseId;
        this.processed = processed;
        this.cleared = cleared;
        this.deviceid = deviceid;
        this.direction = direction;
        this.location = location;
        this.scantime = scantime;
        this.siteid = siteid;
        this.workerid = workerid;
        this.workername = workername;
    }



}
