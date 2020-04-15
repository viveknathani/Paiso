package com.myapps.paiso;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.*;

public class ViewData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        DatabaseHandler db=new DatabaseHandler(this);
        Map<Date, LinkedList<ExpenseData>> hashTable=db.returnHashTable();

        for(Map.Entry<Date, LinkedList<ExpenseData>> entry : hashTable.entrySet())
        {
            String dateE=entry.getValue().get(0).getDate();
            addTextView(dateE);
            for(int i=0; i<entry.getValue().size(); i++)
            {
                String nameE=entry.getValue().get(i).getExpenseName();
                float amount=entry.getValue().get(i).getAmount();
                String paymentMode=entry.getValue().get(i).getExpenseName();
                addTextView(nameE);
                addTextView(String.valueOf(amount));
                addTextView(paymentMode);
            }
        }
    }

    protected void addTextView(String text){
        LinearLayout ll_main=findViewById(R.id.main_view_data);
        TextView textView = new TextView(this);
        textView.setText(text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        textView.setLayoutParams(params);
        ll_main.addView(textView);
    }
}
