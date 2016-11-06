package edu.csumb.hashmapsallday.hungrylittlemonsters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String TAG = "StarvingStudents";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySQLiteHelper db = new MySQLiteHelper(this);

        ArrayList<Location> testLoc = new ArrayList<>();
        testLoc = db.getAllLocations();

        for (Location item : testLoc) {
            if(item.getLatitude() != null && !item.getLatitude().isEmpty())
                Log.d(TAG, item.toString());
        }

        //Create a MyApplication object and call on the proper methods
        MyApplication myApp = (MyApplication) getApplicationContext();

        Log.d(TAG, "main activity "+myApp.getAddress("a").toString());

    }
}