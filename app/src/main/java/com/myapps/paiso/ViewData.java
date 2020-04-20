package com.myapps.paiso;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.nio.channels.ScatteringByteChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ViewData extends AppCompatActivity
{

    private Map<Date, LinkedList<ExpenseData>> hashTable;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        DatabaseHandler db=new DatabaseHandler(this);
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

    public void onClickRefresh(View view)
    {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    public void onClickDisplay(View view)
    {
        Spinner start_spinner=findViewById(R.id.start_date);
        Spinner end_spinner=findViewById(R.id.end_date);
        String startDate=start_spinner.getSelectedItem().toString();
        String endDate=end_spinner.getSelectedItem().toString();
        Date parsed_startDate=parseMyDate(startDate);
        Date parsed_endDate=parseMyDate(endDate);
        String multiLineText="";
        LinearLayout ll_main=findViewById(R.id.main_view_data);
        try
        {
            for(Map.Entry<Date, LinkedList<ExpenseData>> entry : hashTable.entrySet())
            {
                if(entry.getKey().compareTo(parsed_startDate)>=0 && entry.getKey().compareTo(parsed_endDate)<=0)
                {
                    String dateE=entry.getValue().get(0).getDate();
                    multiLineText=multiLineText+dateE+"\n";
                    for(int i=0; i<entry.getValue().size(); i++)
                    {
                        String nameE=entry.getValue().get(i).getExpenseName();
                        float amount=entry.getValue().get(i).getAmount();
                        String paymentMode=entry.getValue().get(i).getPaymentMode();
                        String paymentType=entry.getValue().get(i).getPaymentType();
                        multiLineText=multiLineText+nameE+"\n"+amount+"\n"+paymentMode+"\n"+paymentType+"\n\n";
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.d("Exceptions", "nullptr");
        }
        addTextView(ll_main, multiLineText);
    }
}
