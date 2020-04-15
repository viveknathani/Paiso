package com.myapps.paiso;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DB_NAME="PaisoDB";
    private static final int DB_VERSION=1;
    DatabaseHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sqlCreateUserInfo=("CREATE TABLE USER_INFO(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                          + "NAME TEXT,"
                                                          +"DETAILS_BANK REAL,"
                                                          +"DETAILS_CASH REAL,"
                                                          +"DETAILS_EWALLET REAL)");

        String sqlCreateDebitInfo=("CREATE TABLE DEBIT_INFO(_id INTEGER,"
                                                             + "EXPENSE_NAME TEXT,"
                                                             +"AMOUNT REAL,"
                                                             +"EXPENSE_DATE TEXT,"
                                                             +"PAYMENT_MODE TEXT)");

        db.execSQL(sqlCreateUserInfo);
        db.execSQL(sqlCreateDebitInfo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion==1){
            db.execSQL("DROP TABLE USER_INFO");
            db.execSQL("DROP TABLE DEBIT_INFO");
            onCreate(db);
        }
    }

    void addDataToUserInfo(String username, float bank_d, float cash_d, float ewallet_d){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues rowValues=new ContentValues();

        rowValues.put("NAME", username);
        rowValues.put("DETAILS_BANK", bank_d);
        rowValues.put("DETAILS_CASH", cash_d);
        rowValues.put("DETAILS_EWALLET", ewallet_d);

        db.insert("USER_INFO", null, rowValues);
        db.close();
    }

    public String getUserNameFromDB(){
        String stringToReturn="";
        SQLiteDatabase db=this.getWritableDatabase();
        String query="SELECT NAME FROM USER_INFO";
        Cursor cursor=db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                stringToReturn=cursor.getString(0);
            }while(cursor.moveToNext());
        }
        db.close();

        return stringToReturn;
    }

    public List<Float> getMoneyDetailsFromDB(){
        List<Float> detailsList=new ArrayList<Float>();
        SQLiteDatabase db=this.getWritableDatabase();
        String query="SELECT DETAILS_BANK, DETAILS_CASH, DETAILS_EWALLET FROM USER_INFO";
        Cursor cursor=db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                detailsList.add(cursor.getFloat(0));
                detailsList.add(cursor.getFloat(1));
                detailsList.add(cursor.getFloat(2));
            }while(cursor.moveToNext());
        }
        db.close();

        return detailsList;
    }

    void addDataToDebitInfoAndModifyUserInfo(String expense_name, float amount, String expense_date, String payment_mode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues rowValues=new ContentValues();

        rowValues.put("_id", 1);
        rowValues.put("EXPENSE_NAME", expense_name);
        rowValues.put("AMOUNT", amount);
        rowValues.put("EXPENSE_DATE", expense_date);
        rowValues.put("PAYMENT_MODE", payment_mode);

        db.insert("DEBIT_INFO", null, rowValues);

        String option;
        if(payment_mode.equals("Cash"))      { option="DETAILS_CASH"; }
        else if(payment_mode.equals("Card")) { option="DETAILS_BANK"; }
        else                                 { option="DETAILS_EWALLET"; }

        String query="SELECT "+option+" FROM USER_INFO";

        float currentValue=0, newValue=0;

        Cursor cursor=db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                currentValue=cursor.getFloat(0);
            }while(cursor.moveToNext());
        }

        newValue=currentValue-amount;

        String updateTable="UPDATE USER_INFO SET "+option+" = "+newValue;
        db.execSQL(updateTable);
        db.close();
    }

    protected void addObjectToHashTable(Map<Date, LinkedList<ExpenseData>> hashTable, ExpenseData Obj)
    {
        if(hashTable.containsKey(Obj.getParsedDate()))
        {
            hashTable.get(Obj.getParsedDate()).add(Obj);
        }

        else
        {
            LinkedList<ExpenseData> ll=new LinkedList<ExpenseData>();
            ll.add(Obj);
            hashTable.put(Obj.getParsedDate(), ll);
        }
    }

    public Map<Date, LinkedList<ExpenseData>> returnHashTable(){
        Map<Date, LinkedList<ExpenseData>> hashTable=new TreeMap<Date, LinkedList<ExpenseData>>();
        String query="SELECT EXPENSE_NAME, AMOUNT, EXPENSE_DATE, PAYMENT_MODE FROM DEBIT_INFO";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                ExpenseData obj=new ExpenseData();
                obj.setExpenseName(cursor.getString(0));
                obj.setAmount(cursor.getFloat(1));
                obj.setDate(cursor.getString(2));
                obj.setPaymentMode(cursor.getString(3));
                addObjectToHashTable(hashTable, obj);
            }while(cursor.moveToNext());
        }
        return hashTable;
    }
}
