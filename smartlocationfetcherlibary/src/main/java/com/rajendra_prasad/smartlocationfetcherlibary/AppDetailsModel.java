package com.rajendra_prasad.smartlocationfetcherlibary;

import android.graphics.drawable.Drawable;

public class AppDetailsModel {
    private String packagename;
    private Drawable icon;
    private String appname;

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }


    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }
}
