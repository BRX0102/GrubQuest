package edu.csumb.hashmapsallday.hungrylittlemonsters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    String TAG = "StarvingStudents";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "test");

        //Create a MyApplication object and call on the proper methods
        MyApplication myApp = (MyApplication) getApplicationContext();

        Log.d(TAG, "main activity "+myApp.getAddress("a").toString());

    }
}