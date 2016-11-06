package edu.csumb.hashmapsallday.hungrylittlemonsters;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.DragEvent;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.R.attr.id;


public class FeedMonster extends AppCompatActivity {

    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);


    View column1, column2, column3;
    ImageView monster;
    private static Integer counter = 0;
    private Monster newMonster;
    private String monsterName;
    private String firstChoice;
    private String secondChoice;
    private String thirdChoice;
    private String birthday;
    private String doCook;
    private String transportation;
    private String budget;
    MySQLiteHelper database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new MySQLiteHelper(this);
        setContentView(R.layout.activity_feedmonster);
        column1 = (View)findViewById(R.id.item1Column);
        column2 = (View)findViewById(R.id.item2Column);
        column3 = (View)findViewById(R.id.item3Column);
        monster = ((ImageView)findViewById(R.id.monster));

        Intent i = getIntent();
        monsterName = i.getStringExtra("AVATARNAME");
        birthday = i.getStringExtra("BIRTHDAY");
        doCook = i.getStringExtra("COOKING");
        transportation = i.getStringExtra("TRANS");
        budget = i.getStringExtra("BUDGET");


        startIdleAnimation(monster);

        resetActivity(null);
        monsterDefaultFace(findViewById(R.id.monsterColumn));
//        column1.setOnDragListener(new DragListener());
//        ((ViewGroup)column1).removeAllViews();
//        FoodDragItem temp = new FoodDragItem(this);
//        temp.setImageResource(R.drawable.broccoli);
//        ((ViewGroup)column1).addView(temp);


    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {


            int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    startIdleAnimation(monster);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    counter++;
                    startWaveAnimation(monster);

                    // Dropped foodTile on monster
                    FoodDragItem temp = (FoodDragItem) event.getLocalState();

                    if(counter == 1){
                        firstChoice = Integer.toString(temp.getId());
                    }
                    else if(counter == 2){
                        secondChoice = Integer.toString(temp.getId());
                    }
                    else if(counter == 3){
                        thirdChoice = Integer.toString(temp.getId());
                        submitMonsterPreferences();
                    }

                    //addStatModifier(FoodDragItem);
                    temp.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }
    }

    private void setItemView(View column, FoodDragItem item, int itemID){


        column.setOnDragListener(new DragListener());
        ((ViewGroup)column).removeAllViews();
        item.setImageResource(itemID);
        ((ViewGroup)column).addView(item);
    }

    private void startIdleAnimation(ImageView monster){


        monster.setImageResource(R.drawable.idle_monster);

        AnimationDrawable frameAnimation = (AnimationDrawable)monster.getDrawable();

        frameAnimation.start();
    }

    private void startWaveAnimation(ImageView monster){
        monster.setImageResource(R.drawable.wave_monster);

        AnimationDrawable frameAnimation = (AnimationDrawable)monster.getDrawable();

        frameAnimation.start();
    }

    private void monsterDefaultFace(View v){

        ImageView eyes = new ImageView(this);
        eyes.setImageResource(R.drawable.crosseye);
        eyes.setId(View.generateViewId());
        ((RelativeLayout)v).addView(eyes);

        ImageView mouth = new ImageView(this);
        mouth.setImageResource(R.drawable.grinsmile);
        ((RelativeLayout)v).addView(mouth);

    }


    public void resetActivity(View v){
        column1 = (View)findViewById(R.id.item1Column);
        column2 = (View)findViewById(R.id.item2Column);
        column3 = (View)findViewById(R.id.item3Column);
        monster = ((ImageView)findViewById(R.id.monster));

        startIdleAnimation(monster);

        setItemView(column1, new FoodDragItem(this), R.drawable.broccoli);
        setItemView(column2, new FoodDragItem(this), R.drawable.coins);
        setItemView(column3, new FoodDragItem(this), R.drawable.utensils);
        monster.setOnDragListener(new DragListener());
    }

    private void submitMonsterPreferences(){
        Monster newMonster = new Monster();

        newMonster.setChoices(firstChoice,secondChoice,thirdChoice);
        //newMonster.setMonsterAttributes();

        Intent i = new Intent(this, FeedMe.class);
        this.finish();
        startActivity(i);

    }

    // Still need to add choices to newMonster and then add newMonster to database
    // newMonster.setChoices(firstChoice, secondChoice, thirdChoice);
    // Log.d(TAG, "ADD Customize Profile: " + database.setCustomizeProfile(monsterName, newMonster));


}
