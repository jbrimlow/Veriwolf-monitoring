package com.google.firebase.quickstart.database.models;

/**
 * Created by john on 1/6/17.
 */
public class Site {

    String name;
    String site_id;
    int siteid;

    public Site() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public int getSiteid() {
        return siteid;
    }

    public void setSiteid(int siteid) {
        this.siteid = siteid;
    }
}
