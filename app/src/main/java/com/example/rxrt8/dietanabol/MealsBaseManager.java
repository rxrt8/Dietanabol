package com.example.rxrt8.dietanabol;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}
