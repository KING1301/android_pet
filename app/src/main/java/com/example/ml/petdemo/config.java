package com.example.ml.petdemo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ML on 2016/5/22.
 */
//petdemo 获取相关配置类
public class config {
    private static config petconfig;
    private SharedPreferences.Editor petconfigprefedit;
    private SharedPreferences petconfigpref;

    private config(Context context) {
        petconfigpref = context.getSharedPreferences("petconfig", Context.MODE_PRIVATE);
        petconfigprefedit = petconfigpref.edit();

    }

    public static config getpetconfig(Context context) {
        if (petconfig == null)
            petconfig = new config(context);
        return petconfig;
    }

    public void setpetname(String name) {
        petconfigprefedit.putString("petname", name);
        petconfigprefedit.commit();
    }

    public void setpetyear(String year) {
        petconfigprefedit.putString("petyear", year);
        petconfigprefedit.commit();
    }

    //type 代表不同的宠物模型
    public void setpettype(int type) {
        petconfigprefedit.putInt("pettype", type);
        petconfigprefedit.commit();
    }

    public String getpetname() {
        return petconfigpref.getString("petname", "petname");
    }

    public String getpetyear() {
        return petconfigpref.getString("petyear", "petyear");
    }

    public int gettype() {
        return petconfigpref.getInt("pettype", 0);
    }


}
