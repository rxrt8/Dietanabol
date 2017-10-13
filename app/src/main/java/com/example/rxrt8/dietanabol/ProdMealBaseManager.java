package com.example.rxrt8.dietanabol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by rxrt8 on 2017-10-12.
 */

class ProdMealBaseManager extends SQLiteOpenHelper {

    public ProdMealBaseManager(Context context) {
        super(context, "prodmeal.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table ProdMeal(" +
                        "nr integer primary key autoincrement," +
                        "prodID integer," +
                        "mealID integer," +
                        "quantity ineger);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addKey(ProdMeal prodMeal){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("prodID", prodMeal.getProdId());
        contentValues.put("mealID", prodMeal.getMealId());
        contentValues.put("quantity", prodMeal.getQuantity());
        db.insertOrThrow("ProdMeal", null, contentValues);
    }

    public List<ProdMeal> giveAll(){
        List<ProdMeal> keys = new LinkedList<ProdMeal>();
        String[] columns={"nr","prodID","mealID","quantity"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =db.query("ProdMeal",columns,null,null,null,null,null);
        while(cursor.moveToNext()){
           ProdMeal prodMeal = new ProdMeal();
            prodMeal.setId(cursor.getInt(0));
            prodMeal.setProdId(cursor.getInt(1));
            prodMeal.setMealId(cursor.getInt(2));
            prodMeal.setQuantity(cursor.getInt(3));
            keys.add(prodMeal);
        }
        return keys;
    }

    public void deleteMeal(int id){
        SQLiteDatabase db = getWritableDatabase();
        String[] arguments={""+id};
        db.delete("ProdMeal", "nr=?", arguments);
    }

    public int getLastId(){
        String[] columns={"nr","prodID","mealID","quantity"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =db.query("ProdMeal",columns,null,null,null,null,null);
        int id = 0;
        while(cursor.moveToNext()){
            id = cursor.getInt(0);
        }
        return id;
    }
}
