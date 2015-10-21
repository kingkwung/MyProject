package com.swdm.cc;

import android.graphics.Bitmap;

public class Weather {
    public int icon;
    public String title;
    public String address;
    
    public Weather(){
        super();
    }
    
    public Weather(int droptop, String title, String address) {
        super();
        this.icon = droptop;
        this.title = title;
        this.address = address;
    }
}