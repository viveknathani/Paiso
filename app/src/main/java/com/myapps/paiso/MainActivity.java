package com.myapps.paiso;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


// Entry point of the application on first run.

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //activity appears only during the first run of the app
        SharedPreferences preferences=getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String firstTime=preferences.getString("FirstTime", "");
        if(firstTime.equals("Yes"))
        {
            Intent intent=new Intent(MainActivity.this, GetDetails.class);
            startActivity(intent);
        }
        else
        {
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("FirstTime", "Yes");
            editor.apply();
        }
    }

    //take name and move to the next activity
    public void onClickNext(View view)
    {
        EditText nameView=(EditText)findViewById(R.id.name);
        String nameText=nameView.getText().toString();
        Intent intent=new Intent(this, GetDetails.class);
        intent.setType("text/plain");
        intent.putExtra("Name", nameText);
        startActivity(intent);
    }
}
