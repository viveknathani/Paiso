package com.myapps.paiso;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.List;

public class Dashboard extends AppCompatActivity
{
    private String user_name="";
    private float bank_details=0;
    private float cash_details=0;
    private float ewallet_details=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView helloText=findViewById(R.id.getPoint);
        TextView db_details_0=findViewById(R.id.db_details_0);
        TextView db_details_1=findViewById(R.id.db_details_1);
        TextView db_details_2=findViewById(R.id.db_details_2);

        //get user info from database
        DatabaseHandler db=new DatabaseHandler(this);
        List<Float> detailsList=db.getMoneyDetailsFromDB();

        helloText.setText("Hello, "+db.getUserNameFromDB()+"!");
        db_details_0.setText("Bank : "+Float.toString(detailsList.get(0)));
        db_details_1.setText("Cash : "+Float.toString(detailsList.get(1)));
        db_details_2.setText("E-Wallet : "+Float.toString(detailsList.get(2)));
    }

    public void onClickAddActivity(View view)
    {
        Intent i=new Intent(Dashboard.this, AddData.class);
        startActivity(i);
    }

    public void onClickGainedActivity(View view)
    {
        Intent i=new Intent(Dashboard.this, Gained.class);
        startActivity(i);
    }

    public void onClickViewData(View view)
    {
        Intent i=new Intent(Dashboard.this, ViewData.class);
        startActivity(i);
    }
}
