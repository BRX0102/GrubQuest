package edu.csumb.hashmapsallday.hungrylittlemonsters;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by BRX01 on 11/4/2016.
 */

public class CustomizeProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner transportSpinner;
    private Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customize_profile);

        addListenerOnSpinner();

    }

    public void addListenerOnSpinner(){
        transportSpinner = (Spinner)findViewById(R.id.prefTransporation);

        transportSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
