package com.myapps.paiso;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


//Second activity to appear, runs only once.

public class GetDetails extends AppCompatActivity
{
    public String nameText="";  //for retrieving name from previous activity
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_details);

        //activity appears only during the first run of the app
        SharedPreferences preferences=getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String firstTime=preferences.getString("FirstTime2", "");

        if(firstTime.equals("Yes"))
        {
            Intent intent=new Intent(GetDetails.this, Dashboard.class);
            startActivity(intent);
        }
        else
        {
            Intent intentMain=getIntent();
            nameText=intentMain.getStringExtra("Name");
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("FirstTime2", "Yes");
            editor.apply();
        }
    }

    //take the name that came from previous activity and the monetary values and put them into DB.
    public void onClickDashboard(View view)
    {
        EditText[] valuesView={ findViewById(R.id.details_bank),
                                findViewById(R.id.details_cash),
                                findViewById(R.id.details_ewallet), };

        float[] values={ Float.valueOf(valuesView[0].getText().toString()),
                         Float.valueOf(valuesView[1].getText().toString()),
                         Float.valueOf(valuesView[2].getText().toString()) };

        //put content into the database
        DatabaseHandler db=new DatabaseHandler(this);
        db.addDataToUserInfo(nameText, values[0], values[1], values[2]);
        Intent i=new Intent(GetDetails.this, Dashboard.class);
        startActivity(i);
    }
}