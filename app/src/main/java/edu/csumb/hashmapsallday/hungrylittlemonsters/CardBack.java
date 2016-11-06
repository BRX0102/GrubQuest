package edu.csumb.hashmapsallday.hungrylittlemonsters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by brand on 11/5/2016.
 */

public class CardBack extends AppCompatActivity {

    private String latitude;
    private String longitude;
    private String name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        latitude = i.getStringExtra("LATITUDE");
        longitude = i.getStringExtra("LONGITUDE");
        name = i.getStringExtra("NAME");

        setContentView(R.layout.card_back);
        TextView text = new TextView(this);
        text=(TextView)findViewById(R.id.backText);
        if(name.isEmpty()){
            text.setText("Your Reward is at Taco Bell");
            // Taco Bell | 1830 Fremont Blvd, Seaside, CA 93955 | 36.615726 | -121.842024
            name = "Taco Bell";
            latitude = "36.615726";
            longitude = "-121.842024";

        }
        else{
            text.setText("Your Reward is at " + name);
        }

        Button acceptButton = (Button) findViewById(R.id.acceptQuestButton);
        Button quitButton = (Button) findViewById(R.id.noAcceptQuestButton);

        //ACCEPT BUTTON
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //
                Intent myIntent = new Intent(getApplicationContext(), FeedMap.class);
                Bundle b = new Bundle();
                b.putString("NAME", name);
                b.putString("LATITUDE", latitude);
                b.putString("LONGITUDE", longitude);
                myIntent.putExtras(b);


                startActivity(myIntent);
            }
        });
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), FeedMe.class);
                startActivity(myIntent);


            }
        });
    }

}
