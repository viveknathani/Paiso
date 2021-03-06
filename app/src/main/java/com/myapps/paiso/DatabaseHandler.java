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

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final String DB_NAME="PaisoDB";
    private static final int DB_VERSION=1;
    DatabaseHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sqlCreateUserInfo=("CREATE TABLE USER_INFO(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                          + "NAME TEXT,"
                                                          +"DETAILS_BANK REAL,"
                                                          +"DETAILS_CASH REAL,"
                                                          +"DETAILS_EWALLET REAL)");

        String sqlCreateCreditDebitInfo=("CREATE TABLE CREDIT_DEBIT_INFO(_id INTEGER,"
                                                             + "EXPENSE_NAME TEXT,"
                                                             +"AMOUNT REAL,"
                                                             +"EXPENSE_DATE TEXT,"
                                                             +"PAYMENT_MODE TEXT,"
                                                             +"PAYMENT_TYPE)");

        db.execSQL(sqlCreateUserInfo);
        db.execSQL(sqlCreateCreditDebitInfo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion==1)
        {
            db.execSQL("DROP TABLE USER_INFO");
            db.execSQL("DROP TABLE CREDIT_DEBIT_INFO");
            onCreate(db);
        }
    }

    void addDataToUserInfo(String username, float bank_d, float cash_d, float ewallet_d)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues rowValues=new ContentValues();

        if(bank_d<0)
            bank_d=0;

        if(cash_d<0)
            cash_d=0;

        if(ewallet_d<0)
            ewallet_d=0;

        rowValues.put("NAME", username);
        rowValues.put("DETAILS_BANK", bank_d);
        rowValues.put("DETAILS_CASH", cash_d);
        rowValues.put("DETAILS_EWALLET", ewallet_d);

        db.insert("USER_INFO", null, rowValues);
        db.close();
    }

    public String getUserNameFromDB()
    {
        String stringToReturn="";
        SQLiteDatabase db=this.getWritableDatabase();
        String query="SELECT NAME FROM USER_INFO";
        Cursor cursor=db.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            do {
                stringToReturn=cursor.getString(0);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return stringToReturn;
    }

    public List<Float> getMoneyDetailsFromDB()
    {
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
        cursor.close();
        db.close();

        return detailsList;
    }

    void addDataToCreditDebitInfoAndModifyUserInfo(String expense_name, float amount, String expense_date, String payment_mode, String payment_type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues rowValues=new ContentValues();

        rowValues.put("_id", 1);
        rowValues.put("EXPENSE_NAME", expense_name);
        rowValues.put("AMOUNT", amount);
        rowValues.put("EXPENSE_DATE", expense_date);
        rowValues.put("PAYMENT_MODE", payment_mode);
        rowValues.put("PAYMENT_TYPE", payment_type);

        db.insert("CREDIT_DEBIT_INFO", null, rowValues);

        String option;
        if(payment_mode.equals("Cash"))      { option="DETAILS_CASH"; }
        else if(payment_mode.equals("Card")) { option="DETAILS_BANK"; }
        else                                 { option="DETAILS_EWALLET"; }

        String query="SELECT "+option+" FROM USER_INFO";

        float currentValue=0, newValue=0;

        Cursor cursor=db.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            do{
                currentValue=cursor.getFloat(0);
            }while(cursor.moveToNext());
        }

        if(payment_type.equals("DEBIT"))
        {
            newValue=currentValue-amount;
        }
        else
        {
            newValue=currentValue+amount;
        }

        if(newValue<0)
        {
            newValue=0;
        }
        
        String updateTable="UPDATE USER_INFO SET "+option+" = "+newValue;
        cursor.close();
        db.execSQL(updateTable);
        db.close();
    }

    public void deleteRecordFromDatabase(String expense_name, float amount, String expense_date, String payment_mode, String payment_type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery="DELETE FROM CREDIT_DEBIT_INFO WHERE "
                            +"EXPENSE_NAME=\""+expense_name+"\" AND "
                            +"AMOUNT="+amount+" AND "
                            +"EXPENSE_DATE=\""+expense_date+"\" AND "
                            +"PAYMENT_MODE=\""+payment_mode+"\" AND "
                            +"PAYMENT_TYPE=\""+payment_type+"\"";
        db.execSQL(deleteQuery);
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

    public Map<Date, LinkedList<ExpenseData>> returnHashTable()
    {
        Map<Date, LinkedList<ExpenseData>> hashTable=new TreeMap<Date, LinkedList<ExpenseData>>();
        String query="SELECT EXPENSE_NAME, AMOUNT, EXPENSE_DATE, PAYMENT_MODE, PAYMENT_TYPE FROM CREDIT_DEBIT_INFO";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            do{
                ExpenseData obj=new ExpenseData();
                obj.setExpenseName(cursor.getString(0));
                obj.setAmount(cursor.getFloat(1));
                obj.setDate(cursor.getString(2));
                obj.setPaymentMode(cursor.getString(3));
                obj.setPaymentType(cursor.getString(4));
                addObjectToHashTable(hashTable, obj);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return hashTable;
    }
}
