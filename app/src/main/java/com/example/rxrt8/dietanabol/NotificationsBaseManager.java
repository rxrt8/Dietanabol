package com.example.rxrt8.dietanabol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by rxrt8 on 2017-10-30.
 */

public class NotificationsBaseManager extends SQLiteOpenHelper {
    public NotificationsBaseManager(Context context) {
        super(context, "notifications.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table Notifications(" +
                        "id integer primary key autoincrement," +
                        "name text," +
                        "day text," +
                        "dayAsInteger integer," +
                        "hour text);" +
                        "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addNotification(Notification notification){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", notification.getNotificationTitle());
        contentValues.put("day", notification.getDay());
        contentValues.put("dayAsInteger",notification.getDayAsInteger());
        contentValues.put("hour", notification.getTime());

        db.insertOrThrow("Notifications", null, contentValues);
    }

    public void deleteNotification(int id){
        SQLiteDatabase db = getWritableDatabase();
        String[] arguments={""+id};
        db.delete("Notifications", "id=?", arguments);
    }

    public List<Notification> giveAll(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select id,name,day,dayAsInteger,hour from Notifications order by day,hour asc", null);
        List<Notification> notifications = new LinkedList<>();
        while(cursor.moveToNext()){
            Notification notification = new Notification();
            notification.setId(cursor.getInt(0));
            notification.setNotificationTitle(cursor.getString(1));
            notification.setDay(cursor.getString(2));
            notification.setDayAsInteger(cursor.getInt(3));
            notification.setTime(cursor.getString(4));
            notifications.add(notification);
        }
        cursor.close();
        return notifications;
    }

    public Notification giveNotification(int id){
        Notification notification = new Notification();
        String[] columns = getColumns();
        SQLiteDatabase db = getReadableDatabase();
        String args[] = {id+""};
        Cursor cursor = db.query("Notifications",columns," id=?",args,null,null,null,null);
        if(cursor.moveToFirst()) {
            cursor.moveToFirst();
            notification.setId(cursor.getInt(0));
            notification.setNotificationTitle(cursor.getString(1));
            notification.setDay(cursor.getString(2));
            notification.setDayAsInteger(cursor.getInt(3));
            notification.setTime(cursor.getString(4));
        }
        cursor.close();
        return notification;
    }

    private String[] getColumns(){
        String[] columns = {"id","name","day","dayAsInteger","hour"};
        return columns;
    }

    public int getLastId(){
        String[] columns = getColumns();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =  db.query("Notifications",columns,null,null,null,null,null);
        int id = 0;
        while(cursor.moveToNext()){
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }
}
