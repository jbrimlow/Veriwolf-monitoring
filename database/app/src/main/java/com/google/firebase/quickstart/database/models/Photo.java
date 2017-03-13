package com.google.firebase.quickstart.database.models;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.Date;

/**
 * Created by john on 12/16/16.
 */
public class Photo {

    public String imgBase64;
    public String description;
    public String contractor;
    public Date scantime;
    public String filepath;

    public Photo() {

    }

    public Photo(String imgBase64, String description, String contractor, Date scantime, String filepath) {
        this.imgBase64 = imgBase64;
        this.description = description;
        this.contractor = contractor;
        this.scantime = scantime;
        this.filepath = filepath;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public Date getScantime() {
        return scantime;
    }

    public void setScantime(Date scantime) {
        this.scantime = scantime;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
