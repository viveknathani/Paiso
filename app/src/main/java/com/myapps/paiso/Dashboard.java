package com.myapps.paiso;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;
import java.util.List;

//Becomes the entry point of the application after the first run.

public class Dashboard extends AppCompatActivity
{
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

        try
        {
            helloText.setText("Hello, "+db.getUserNameFromDB()+"!");
            db_details_0.setText("Bank : "+Float.toString(detailsList.get(0)));
            db_details_1.setText("Cash : "+Float.toString(detailsList.get(1)));
            db_details_2.setText("E-Wallet : "+Float.toString(detailsList.get(2)));
        }
        catch(Exception e)
        {
            System.out.println("Out of bounds exception");
        }

        try
        {
            bank_details = detailsList.get(0);
            cash_details = detailsList.get(1);
            ewallet_details = detailsList.get(2);
        }
        catch(Exception e)
        {
            System.out.println("Out of bounds exception");
        }

        makePieChart(bank_details, cash_details, ewallet_details);
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

    public void onClickUpdate(View view)
    {
        Intent i=new Intent(Dashboard.this, Update.class);
        startActivity(i);
    }

    public void onClickViewData(View view)
    {
        Intent i=new Intent(Dashboard.this, ViewData.class);
        startActivity(i);
    }

    public void makePieChart(float bank_details, float cash_details, float ewallet_details)
    {
        //compute percentages
        float totalValue=bank_details+cash_details+ewallet_details;
        float percent_bank_details=bank_details*100/totalValue;
        float percent_cash_details=cash_details*100/totalValue;
        float percent_ewallet_details=ewallet_details*100/totalValue;

        //initialise pie chart
        PieChart chart=(PieChart)findViewById(R.id.pie_chart);

        //add values and labels to a list
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(percent_bank_details, "Bank"+String.format("%.1f", percent_bank_details)+"%"));
        entries.add(new PieEntry(percent_cash_details, "Cash"+String.format("%.1f", percent_cash_details)+"%"));
        entries.add(new PieEntry(percent_ewallet_details, "E-Wallets"+String.format("%.1f", percent_ewallet_details)+"%"));


        //create a dataset and choose colours
        PieDataSet set = new PieDataSet(entries, null);
        set.setColors(new int[]{Color.parseColor("#F3F56C"),
                Color.parseColor("#6CF57D"),
                Color.parseColor("#F56CA6")});
        PieData data = new PieData(set);
        data.setValueTextSize(30f);
        data.setDrawValues(false);

        //more styling and finally display the chart
        chart.setCenterText("Paiso Distribution");
        chart.setHoleColor(Color.WHITE);
        chart.setCenterTextColor(Color.BLACK);
        chart.setCenterTextSize(18);
        chart.setUsePercentValues(true);
        chart.setEntryLabelColor(R.color.mainDarkColor);
        chart.getDescription().setEnabled(false);
        chart.setDrawEntryLabels(false);
        chart.setData(data);
        chart.invalidate();
    }
}
