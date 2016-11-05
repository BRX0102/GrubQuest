package edu.csumb.hashmapsallday.hungrylittlemonsters;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by sal on 11/4/2016.
 */

public class MyApplication extends Application {
    final String TAG = "StarvingStudents";
    private static HashMap<String, String> addresses = new HashMap<String, String>();

    public MyApplication(){
        Log.d(TAG, "top");
        addresses.put("a", "an address");
    }

    public String getAddress(String key){
        return addresses.get(key);
    }

    public void setAddress(String key, String value){
        addresses.put(key, value);
    }
}
