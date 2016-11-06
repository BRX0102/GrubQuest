package edu.csumb.hashmapsallday.hungrylittlemonsters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  Customize Profile takes in the user's weekly budget, preference to cook, and transportation preference.
 */

public class CustomizeProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner transportSpinner;
    private Button submitButton;
    private EditText weeklyBudget;
    private RadioGroup doCook;
    private String monsterName;
    private String transportation;
    private Monster monster;
    private Context context;
    private String birthday;
//    MySQLiteHelper database = new MySQLiteHelper(this);

    //SQLiteOpenHelper helper = new SQLiteOpenHelper(context, "StarvingStudents", null, 9);
    //SQLiteDatabase database = helper.getWritableDatabase();


    String TAG = "Customize";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customize_profile);

        Intent i = getIntent();
        monsterName = i.getStringExtra("AVATARNAME");
        birthday = i.getStringExtra("BIRTHDAY");

        addListenerOnSpinner();

        Button submitPref = (Button)findViewById(R.id.custProfSubmit);
        submitPref.setOnClickListener(this);
        //monster = new Monster();
        
        //database = new MySQLiteHelper(getApplicationContext());
        //database = context.getWritableDatabase();;
    }

    public void addListenerOnSpinner(){
        transportSpinner = (Spinner)findViewById(R.id.prefTransporation);

        transportSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MyApplication myApp = (MyApplication) getApplicationContext();
        myApp.setAddress("prefTransportation", parent.getItemAtPosition(position).toString());
        Log.d(TAG, "Pref Transportation "+myApp.getAddress("prefTransportation").toString());
        transportation = myApp.getAddress("prefTransportation").toString();
        //monster.setTransportation(myApp.getAddress("prefTransportation").toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        MyApplication myApp = (MyApplication) getApplicationContext();
        if(v.getId() == R.id.custProfSubmit){
            doCook = (RadioGroup)findViewById(R.id.iCook);
            String doCookString = ((RadioButton)findViewById(doCook.getCheckedRadioButtonId())).getText().toString();
            myApp.setAddress("doCook", doCookString);
            //monster.setCooking(doCookString);

            Log.d(TAG, "COOKING" + doCookString);

            weeklyBudget = (EditText)findViewById(R.id.weeklyBudget);
            myApp.setAddress("weeklyBudget", weeklyBudget.getText().toString());
            //monster.setWeeklyBudget(weeklyBudget.getText().toString());

            Log.d(TAG, "BUDGET" + weeklyBudget);

            //Log.d(TAG, "ADD Customize Profile: " + database.setCustomizeProfile(monsterName, monster));

            //Monster temp = new Monster();
            //temp = database.getMonster(monsterName);
            //Log.d(TAG, temp.toString());

            Intent i = new Intent(this, FeedMonster.class);
            Bundle b = new Bundle();

            b.putString("AVATARNAME", monsterName);
            b.putString("BIRTHDAY", birthday);
            b.putString("COOKING", doCookString);
            b.putString("TRANS", transportation);
            b.putString("BUDGET", weeklyBudget.getText().toString());

            i.putExtras(b);
            startActivity(i);
            this.finish();
        }
    }
}