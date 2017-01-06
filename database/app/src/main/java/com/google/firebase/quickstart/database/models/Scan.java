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
    public Long scantime;
    public Long siteid;
    public String workerid;
    //public Long workerid;
    public String workername;

    public Scan() {
        //default constructor
    }

    public Scan(String firebaseId, boolean processed, boolean cleared, String deviceid, String direction, String location,
                Long scantime, Long siteid, String workerid, String workername) {
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

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public boolean isCleared() {
        return cleared;
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getScantime() {
        return scantime;
    }

    public void setScantime(Long scantime) {
        this.scantime = scantime;
    }

    public Long getSiteid() {
        return siteid;
    }

    public void setSiteid(Long siteid) {
        this.siteid = siteid;
    }

    public String getWorkerid() {
        return workerid;
    }

    public void setWorkerid(String workerid) {
        this.workerid = workerid;
    }

    public String getWorkername() {
        return workername;
    }

    public void setWorkername(String workername) {
        this.workername = workername;
    }
}
