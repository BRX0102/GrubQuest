package edu.csumb.hashmapsallday.hungrylittlemonsters;

import android.content.ClipData;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;



public class FoodDragItem extends ImageView {

    private int healthModifier;
    private int costModifier;
    private int tasteModifier;

    public FoodDragItem(Context context) {
        super(context);
        healthModifier = 0;
        costModifier = 0;
        tasteModifier = 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            return startDrag(
                    ClipData.newPlainText("", ""),
                    new View.DragShadowBuilder(this),
                    this,
                    0
            );
        }

        return super.onTouchEvent(motionEvent);
    }
}
