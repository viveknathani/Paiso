package com.myapps.paiso;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

//activity that adds data to the database

public class AddData extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        Spinner spinner = findViewById(R.id.payment_mode);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.payment_modes, R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }

    public void onClickAdd(View view)
    {
        EditText expense_name=(EditText)findViewById(R.id.expense_name);
        EditText amount=(EditText)findViewById(R.id.expense_amount);
        EditText expense_date=(EditText)findViewById(R.id.expense_d);
        Spinner payment_mode=(Spinner) findViewById(R.id.payment_mode);

        String db_e_name="";
        float db_amount=0;
        String db_e_date="";
        String db_mode="";

        try
        {
            db_e_name=expense_name.getText().toString();
            db_amount=Float.valueOf(amount.getText().toString());
            db_e_date=expense_date.getText().toString();
            db_mode=payment_mode.getSelectedItem().toString();
        }
        catch(Exception e)
        {
            Log.d("SpentException", "While taking value from EditText");
        }

        DatabaseHandler db=new DatabaseHandler(this);
        try
        {
            db.addDataToCreditDebitInfoAndModifyUserInfo(db_e_name, db_amount, db_e_date, db_mode, "DEBIT");
        }
        catch(Exception e)
        {
            Log.d("SpentException", "While adding to DB");
        }
        Intent i=new Intent(AddData.this, Dashboard.class);
        startActivity(i);
    }
}
