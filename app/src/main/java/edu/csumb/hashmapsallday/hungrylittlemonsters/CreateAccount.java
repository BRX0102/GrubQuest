package edu.csumb.hashmapsallday.hungrylittlemonsters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import java.io.Serializable;
import java.util.ArrayList;

public class CreateAccount extends Activity {
    private ViewFlipper viewFlipper;
    private float lastX;
    private Monster newMonster;
    ImageView monster1;
    ImageView monster2;
    ImageView monster3;
    private String avatarName;
    private String birthday = "";
    private String userName = "";
//    private MySQLiteHelper database;
    String TAG = "CreateAccount";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

        monster1 = (ImageView)findViewById(R.id.monsterImage);
        monster2 = (ImageView)findViewById(R.id.monster2Image);
        monster3 = (ImageView)findViewById(R.id.monster3Image);
        monster2.setColorFilter(Color.parseColor("#00FF00"), PorterDuff.Mode.MULTIPLY);
        monster3.setColorFilter(Color.parseColor("#FFFF00"), PorterDuff.Mode.MULTIPLY);

        /*Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        userName = extras.getString("FIRSTNAME");
        birthday = extras.getString("BIRTHDAY");*/
    }

    // Using the following method, we will handle all screen swaps.
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = touchevent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float currentX = touchevent.getX();

                // Handling left to right screen swap.
                if (lastX < currentX) {

                    // If there aren't any other children, just break.
                    if (viewFlipper.getDisplayedChild() == 0)
                        break;

                    // Next screen comes in from left.
                    viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
                    // Current screen goes out from right.
                    viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);

                    // Display next screen.
                    viewFlipper.showNext();
                }

                // Handling right to left screen swap.
                if (lastX > currentX) {

                    // If there is a child (to the left), kust break.
                    if (viewFlipper.getDisplayedChild() == 1)
                        break;

                    // Next screen comes in from right.
                    viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
                    // Current screen goes out from left.
                    viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);

                    // Display previous screen.
                    viewFlipper.showPrevious();
                }
                break;
        }
        return false;
    }

//    @Override
//    public void onClick(View v) {
//        if(v.getId()==R.id.submitAvatar){
//
//            this.finish();
//        }
//    }

    public void submitMonsterData(View v){
        avatarName = ((EditText)findViewById(R.id.monsterName)).getText().toString();
        //Karen please add this name to your database.


        // Savethis to monster.name=((EditText)findViewById(R.id.monsterName)).getText().toString();
        int monsterID = viewFlipper.getDisplayedChild();

        //ColorFilter color = ((ImageView)findViewById(monsterID)).getColorFilter();

        newMonster = new Monster();
//        database = new MySQLiteHelper(getApplicationContext());

        newMonster.setName(avatarName);

//        database.setMonsterName(newMonster);

        newMonster.setColor(Integer.toString(monsterID));
        newMonster.setBirthday(birthday);

        Log.d(TAG, "Color:" + Integer.toString(monsterID));
        Log.d(TAG, "NAME:" + avatarName);
        Log.d(TAG, "Color:" + birthday);
//        Log.d(TAG, Integer.toString(database.setCreateAccount(avatarName, newMonster)));
        //database.setCreateAccount(avatarName, newMonster);

        Intent customizeProfile = new Intent(this, CustomizeProfile.class);
        Bundle b = new Bundle();

        b.putString("AVATARNAME", avatarName);
        b.putString("BIRTHDAY", birthday);

        customizeProfile.putExtras(b);

        startActivity(customizeProfile);



    }


}
