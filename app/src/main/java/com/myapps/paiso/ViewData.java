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
            Log.d("dateArray", datesArray[i]);
        }
    }

    public void onClickDisplay(View view)
    {
        //do something
    }
}
