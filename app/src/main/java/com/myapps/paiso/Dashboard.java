package com.myapps.paiso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private String user_name="";
    private float bank_details=0;
    private float cash_details=0;
    private float ewallet_details=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){

            float[] storedValues=bundle.getFloatArray("values");
            assert storedValues != null;

            user_name=bundle.getString("name");
            bank_details=storedValues[0];
            cash_details=storedValues[1];
            ewallet_details=storedValues[2];

        }
        TextView db_name=findViewById(R.id.db_name);
        TextView db_details_0=findViewById(R.id.db_details_0);
        TextView db_details_1=findViewById(R.id.db_details_1);
        TextView db_details_2=findViewById(R.id.db_details_2);

        DatabaseHandler db=new DatabaseHandler(this);
        List<Float> detailsList=db.getMoneyDetailsFromDB();

        db_name.setText(db.getUserNameFromDB());
        db_details_0.setText(Float.toString(detailsList.get(0)));
        db_details_1.setText(Float.toString(detailsList.get(1)));
        db_details_2.setText(Float.toString(detailsList.get(2)));
    }

    public void onClickAddActivity(View view){
        Intent i=new Intent(Dashboard.this, AddData.class);
        startActivity(i);
    }

    public void onClickViewData(View view){
        Intent i=new Intent(Dashboard.this, ViewData.class);
        startActivity(i);
    }
}
