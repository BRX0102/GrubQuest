package edu.csumb.hashmapsallday.hungrylittlemonsters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class CreateAccount extends Activity implements View.OnClickListener {
    private ViewFlipper viewFlipper;
    private float lastX;
    private String avatarName;
    ImageView monster1 = (ImageView)findViewById(R.id.monster);
    ImageView monster2 = (ImageView)findViewById(R.id.monster2);
    ImageView monster3 = (ImageView)findViewById(R.id.monster3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

        monster1 = (ImageView)findViewById(R.id.monster);
        monster2 = (ImageView)findViewById(R.id.monster2);
        monster3 = (ImageView)findViewById(R.id.monster3);
        monster2.setColorFilter(Color.parseColor("#00FF00"), PorterDuff.Mode.MULTIPLY);
        monster3.setColorFilter(Color.parseColor("#FFFF00"), PorterDuff.Mode.MULTIPLY);
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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.submitAvatar){
            avatarName = ((EditText)findViewById(R.id.avatarName)).getText().toString();
            //Karen please add this name to your database.

            Intent customizeProfile = new Intent(this, CustomizeProfile.class);
            startActivity(customizeProfile);
            this.finish();
        }
    }

    public void submitMonsterData(){
        // Savethis to monster.name=((EditText)findViewById(R.id.monsterName)).getText().toString();
        int monsterID = viewFlipper.getDisplayedChild();
        ColorFilter color = ((ImageView)findViewById(monsterID)).getColorFilter();
    }
}
