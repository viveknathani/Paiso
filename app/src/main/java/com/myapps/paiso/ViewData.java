package com.myapps.paiso;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.*;

public class ViewData extends AppCompatActivity
{
    LinearLayout linearLayout;
    LinearLayout.LayoutParams params;
    CardView.LayoutParams params_cardview;

    private Map<Date, LinkedList<ExpenseData>> hashTable=new TreeMap<Date, LinkedList<ExpenseData>>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        DatabaseHandler db=new DatabaseHandler(this);

        try
        {
            hashTable=db.returnHashTable();
            String[] datesArray=new String[hashTable.size()];
            ArrayList<String> tempDateHolder=new ArrayList<String>();

            for(Map.Entry<Date, LinkedList<ExpenseData>> entry : hashTable.entrySet())
            {
                tempDateHolder.add(entry.getValue().get(0).getDate());
            }

            for(int i=0; i<tempDateHolder.size(); i++)
            {
                String m=tempDateHolder.get(i);
                datesArray[i]=m;
            }

            Spinner start_spinner=findViewById(R.id.start_date);
            Spinner end_spinner=findViewById(R.id.end_date);
            ArrayAdapter<String> start_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datesArray);
            start_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ArrayAdapter<String> end_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datesArray);
            end_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            start_spinner.setAdapter(start_adapter);
            end_spinner.setAdapter(end_adapter);
        }
        catch(Exception e)
        {
            Log.d("Exceptions", "Something's wrong with the hash table.");
        }


    }

    protected Date parseMyDate(String val)
    {
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        try
        {
            return dateFormat.parse(val);
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public void onClickRefresh(View view)
    {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onClickDisplay(View view)
    {
        Spinner start_spinner=findViewById(R.id.start_date);
        Spinner end_spinner=findViewById(R.id.end_date);
        String startDate="";
        String endDate="";
        Date parsed_startDate=new Date();
        Date parsed_endDate=new Date();
        try
        {
            startDate=start_spinner.getSelectedItem().toString();
            endDate=end_spinner.getSelectedItem().toString();
            parsed_startDate=parseMyDate(startDate);
            parsed_endDate = parseMyDate(endDate);
        }
        catch(Exception e)
        {
            Log.d("Display", "Date variable exception");
        }
        linearLayout=findViewById(R.id.scroll_linear_layout);
        params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params_cardview=new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.WRAP_CONTENT);
        params_cardview.setMargins(30, 10, 30, 10);
        try
        {
            for(Map.Entry<Date, LinkedList<ExpenseData>> entry : hashTable.entrySet())
            {
                if(entry.getKey().compareTo(parsed_startDate)>=0 && entry.getKey().compareTo(parsed_endDate)<=0)
                {
                    //get the date as a string
                    String dateE="Date : "+entry.getValue().get(0).getDate();

                    //loop through the linked list
                    for(int i=0; i<entry.getValue().size(); i++)
                    {
                        //prepare and put content in a text view
                        String multiLineText="";
                        String fieldName="";
                        String amount="Amount : "+entry.getValue().get(i).getAmount();
                        String paymentMode="Payment Mode : "+entry.getValue().get(i).getPaymentMode();
                        String paymentType=entry.getValue().get(i).getPaymentType();
                        String paymentTypeString="Payment Type : "+paymentType;
                        if(paymentType.equals("CREDIT"))
                        {
                            fieldName="Credit Name : ";
                        }
                        else
                        {
                            fieldName="Expense Name : ";
                        }
                        String nameE=fieldName+entry.getValue().get(i).getExpenseName();
                        multiLineText=dateE+"\n"+nameE+"\n"+amount+"\n"+paymentMode+"\n"+paymentTypeString;
                        TextView textView=new TextView(this);
                        textView.setLayoutParams(params);
                        textView.setText(multiLineText);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        textView.setTypeface(null, Typeface.BOLD);
                        if(paymentType.equals("CREDIT"))
                        {
                            textView.setTextColor(Color.parseColor("#4CAF50"));
                        }
                        else
                        {
                            textView.setTextColor(Color.parseColor("#FFC107"));
                        }
                        textView.setPadding(10, 10, 10, 10);

                        //create a card view to hold the text view
                        CardView cardview=new CardView(this);
                        cardview.setLayoutParams(params_cardview);
                        cardview.setCardBackgroundColor(Color.WHITE);
                        cardview.setRadius(10);
                        cardview.setElevation(10);
                        cardview.addView(textView);

                        //put the card view into the linear layout inside the scroll view
                        linearLayout.addView(cardview);
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.d("Exceptions", "nullptr");
        }
    }
}
