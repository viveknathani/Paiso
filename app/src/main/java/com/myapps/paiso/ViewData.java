package com.myapps.paiso;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
        LinearLayout ll_main=findViewById(R.id.main_view_data);
        String multilineText="";

        for(Map.Entry<Date, LinkedList<ExpenseData>> entry : hashTable.entrySet())
        {
            String dateE=entry.getValue().get(0).getDate();
            multilineText=multilineText+dateE+"\n";
            Log.d("DATE", dateE);
            for(int i=0; i<entry.getValue().size(); i++)
            {
                String nameE=entry.getValue().get(i).getExpenseName();
                float amount=entry.getValue().get(i).getAmount();
                String paymentMode=entry.getValue().get(i).getPaymentMode();
                multilineText=multilineText+nameE+"\n"+amount+"\n"+paymentMode+"\n";
            }
        }

        addTextView(ll_main, multilineText);
    }

    protected void addTextView(LinearLayout ll_main, String text){
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        ll_main.setOrientation(LinearLayout.VERTICAL);
        textView.setLayoutParams(params);
        textView.setText(text);
        ll_main.addView(textView);
    }
}
