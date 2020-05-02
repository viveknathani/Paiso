package com.myapps.paiso;

import java.util.*;
import java.text.SimpleDateFormat;

//a class for handling our data
public class ExpenseData
{
    private String expenseName="";
    private float amount=0;
    private String date="";
    private String paymentMode="";
    private String paymentType="";

    public void setExpenseName(String val) { this.expenseName=val; }

    public void setAmount(float val) { this.amount=val; }

    public void setDate(String val) { this.date=val; }

    public void setPaymentMode(String val) { this.paymentMode=val; }

    public void setPaymentType(String val) { this.paymentType=val; }

    public String getExpenseName() { return this.expenseName; }

    public float getAmount() { return this.amount; }

    public String getDate() { return this.date; }

    public String getPaymentMode() { return this.paymentMode; }

    public String getPaymentType() { return this.paymentType; }

    public Date getParsedDate()
    {
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        try
        {
            return dateFormat.parse(this.getDate());
        }
        catch(Exception e)
        {
            return null;
        }
    }
}

