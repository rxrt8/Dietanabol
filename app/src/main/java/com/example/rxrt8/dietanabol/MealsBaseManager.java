package com.example.rxrt8.dietanabol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by rxrt8 on 2017-09-29.
 */

public class MealsBaseManager extends SQLiteOpenHelper {

    public MealsBaseManager(Context context) {
        super(context, "meals.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table Meals(" +
                        "nr integer primary key autoincrement," +
                        "name text," +
                        "day text," +
                        "hour text);" +
                        "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addMeal(Meal meal){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", meal.getMealName());
        contentValues.put("day", meal.getDay());
        contentValues.put("hour", meal.getHour());
        db.insertOrThrow("Meals", null, contentValues);
    }

    public List<Meal> giveAll(){
        List<Meal> meals = new LinkedList<Meal>();
        String[] columns={"nr","name","day","hour"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =db.query("Meals",columns,null,null,null,null,null);
        while(cursor.moveToNext()){
            Meal meal= new Meal();
            meal.setNr(cursor.getInt(0));
            meal.setMealName(cursor.getString(1));
            meal.setDay(cursor.getString(2));
            meal.setHour(cursor.getString(3));
            meals.add(meal);
        }
        return meals;
    }

    public int getLastId(){
        String[] columns={"nr","name","day","hour"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =db.query("Meals",columns,null,null,null,null,null);
        int id = 0;
        while(cursor.moveToNext()){
            id = cursor.getInt(0);
        }
        return id;
    }
}