package com.example.smartserviceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBPrecedents extends SQLiteOpenHelper {
    private final String table_name_precedents = "Precedents";
    private final String table_name_services = "Services";
    private Context context;
    SQLiteDatabase db;
    public DBPrecedents(Context context) {
        super(context, "myDB",null,1);
        this.context = context;
        this.db = this.getWritableDatabase();

        try {
            Cursor c = db.query(table_name_services, null, null, null, null, null, null);
            c.close();
        }
        catch (Exception e)
        {
            createTableServices();
        }
        try {
            Cursor c = db.query(table_name_precedents, null, null, null, null, null, null);
            c.close();
        }
        catch (Exception e)
        {
            createTablePrecedents();
        }


    }
    public ArrayList<InfoPrecedent> loadPrecedents(int serviceId)
    {
        db = this.getReadableDatabase();
        ArrayList<InfoPrecedent> tmp = new ArrayList<>();
        Cursor c = db.query(table_name_precedents, null, null, null, null, null, null);
        if (c.moveToFirst())
        {
//            int idColIndex = c.getColumnIndex("id");
            int labelColIndex = c.getColumnIndex("label");
            int idServiceIndex = c.getColumnIndex("service_id");
            int v1ColIndex = c.getColumnIndex("v1");
            int v2ColIndex = c.getColumnIndex("v2");
            int v3ColIndex = c.getColumnIndex("v3");
            int v4ColIndex = c.getColumnIndex("v4");
            int v5ColIndex = c.getColumnIndex("v5");

            do {
                if (c.getInt(idServiceIndex) == serviceId) {
                    InfoPrecedent cur = new InfoPrecedent();
                    cur.label = c.getString(labelColIndex);
                    cur.time = c.getDouble(v1ColIndex);
                    cur.lastLat = c.getDouble(v2ColIndex);
                    cur.lastLong = c.getDouble(v3ColIndex);
                    cur.curLat = c.getDouble(v4ColIndex);
                    cur.curLong = c.getDouble(v5ColIndex);
                    tmp.add(cur);
                }
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return tmp;
    }
    public void addService(SmartService smartService) {
        db = this.getWritableDatabase();
        db.beginTransaction();
        db.insert(table_name_services, null, smartService.curTask.fillCV);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    public ArrayList<SmartService> loadServices()
    {
        db = this.getReadableDatabase();
        ArrayList<SmartService> tmp = new ArrayList<>();
        Cursor c = db.query(table_name_services, null, null, null, null, null, null);
        if (c.moveToFirst())
        {
            int cnt = 0;
            int idColIndex = c.getColumnIndex("id");
            int nameIndex = c.getColumnIndex("name");
            int typeIndex = c.getColumnIndex("type");
            int phoneIndex = c.getColumnIndex("phone");
            int messageIndex = c.getColumnIndex("message");


            do {

                String type = c.getString(typeIndex);
                SmartService smartService = new SmartService(c.getString(nameIndex),null,cnt);;
                ServiceTask stask;
                switch (type)
                {
                    case "PHONE_CALL":
                        stask = new ServicePhoneTask(context,smartService,c.getString(phoneIndex));
                        break;
                    default:
                        stask = null;
                }
                smartService.curTask = stask;
                tmp.add(smartService);
                cnt++;
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return tmp;
    }
    public int howMuch()
    {
        db = this.getReadableDatabase();
        int c1 = 0;
        Cursor c = db.query(table_name_precedents, null, null, null, null, null, null);
        if (c.moveToFirst())
        {
//            int idColIndex = c.getColumnIndex("id");
            int labelCntIndex = c.getColumnIndex("cnt");
            int idServiceIndex = c.getColumnIndex("service_id");
            int v1ColIndex = c.getColumnIndex("v1");
            int v2ColIndex = c.getColumnIndex("v2");
            int v3ColIndex = c.getColumnIndex("v3");
            int v4ColIndex = c.getColumnIndex("v4");
            int v5ColIndex = c.getColumnIndex("v5");
            do {
                    c1++;
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return c1;
//        int numRows = DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM " + table_name_precedents, null);

    }
    public void addPrecedent(InfoPrecedent infoPrecedent,int serviceId)
    {

        db = this.getWritableDatabase();

        db.beginTransaction();

        ContentValues cv = new ContentValues();
        cv.put("label", infoPrecedent.label);
        cv.put("service_id", serviceId);
        cv.put("v1", infoPrecedent.time);
        cv.put("v2", infoPrecedent.lastLat);
        cv.put("v3", infoPrecedent.lastLong);
        cv.put("v4", infoPrecedent.curLat);
        cv.put("v5", infoPrecedent.curLong);
        db.insert(table_name_precedents, null, cv);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    public void createTableServices()
    {
        db = this.getWritableDatabase();

        db.execSQL("create table " +table_name_services+ "("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "type text,"
                + "phone text,"
                + "message text"
                + ");");
        SmartService s = new SmartService("SHLAGBAUM",null,0);
        ServicePhoneTask servicePhoneTask = new ServicePhoneTask(context,s,"+79501320841");
        s.curTask = servicePhoneTask;
        db.insert(table_name_services,null,servicePhoneTask.fillCV);
        db.close();
    }
    public void createTablePrecedents()
    {
        db = this.getWritableDatabase();
        db.execSQL("create table Precedents ("
                + "id integer primary key autoincrement,"
                + "service_id integer,"
                + "label text,"
                + "v1 real,"
                + "v2 real,"
                + "v3 real,"
                + "v4 real,"
                + "v5 real" + ");");
        db.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        createTablePrecedents();
        createTableServices();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void clearPrecedents()
    {
        db = this.getWritableDatabase();
        db.execSQL("drop table "+table_name_precedents);
        createTablePrecedents();
        db.close();
    }
}
