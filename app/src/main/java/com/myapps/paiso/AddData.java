package com.myapps.paiso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AddData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
    }

    public void onClickAdd(View view){
        EditText expense_name=(EditText)findViewById(R.id.expense_name);
        EditText amount=(EditText)findViewById(R.id.expense_amount);
        EditText expense_date=(EditText)findViewById(R.id.expense_d);
        Spinner payment_mode=(Spinner) findViewById(R.id.payment_mode);

        String db_e_name=expense_name.getText().toString();
        float db_amount=Float.valueOf(amount.getText().toString());
        String db_e_date=expense_date.getText().toString();
        String db_mode=payment_mode.getSelectedItem().toString();

        DatabaseHandler db=new DatabaseHandler(this);
        db.addDataToDebitInfoAndModifyUserInfo(db_e_name, db_amount, db_e_date, db_mode);
        Intent i=new Intent(AddData.this, Dashboard.class);
        startActivity(i);
    }
}
