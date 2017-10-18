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

    private Context myContext;

    public ProdMealBaseManager(Context context) {
        super(context, "prodmeal.db", null, 1);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table ProdMeal(" +
                        "id integer primary key autoincrement," +
                        "prodID integer," +
                        "mealID integer," +
                        "quantity integer)");
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
        String[] columns = getColumns();
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


    private void deleteTheAmountOfFood(int id){
        ProductsBaseManager productsBaseManager = new ProductsBaseManager(myContext);

        String[] columns = getColumns();
        SQLiteDatabase db = getReadableDatabase();
        String args[] = {id+""};
        Cursor cursor = db.query("ProdMeal",columns," id=?",args,null,null,null,null);

        if(cursor.moveToFirst()) {
            cursor.moveToFirst();
            ProdMeal prodMeal = new ProdMeal();
            prodMeal.setProdId(cursor.getInt(1));
            prodMeal.setQuantity(cursor.getInt(3));
            FoodProduct foodProduct = productsBaseManager.giveFoodProduct(prodMeal.getProdId());
            productsBaseManager.changeTheAmountOfFood(foodProduct, -prodMeal.getQuantity());
        }
    }

    public void deleteKey(int id){
        deleteTheAmountOfFood(id);
        SQLiteDatabase db = getWritableDatabase();
        String[] arguments = {""+id};
        db.delete("ProdMeal", "id=?", arguments);
    }


    public int getLastId(){
        String[] columns = getColumns();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("ProdMeal",columns,null,null,null,null,null);
        int id = 0;
        while(cursor.moveToNext()){
            id = cursor.getInt(0);
        }
        return id;
    }

    public List<ProdMeal> giveByProdID(int productID){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select id,prodID,mealID,quantity from ProdMeal where prodID='"
                +productID+
                "' order by id asc", null);
        return fillProdMealsList(cursor);
    }

    public List<ProdMeal> giveByMealID(int mealID){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select id,prodID,mealID,quantity from ProdMeal where mealID='"
                +mealID+
                "' order by id asc", null);
        return fillProdMealsList(cursor);
    }

    private List<ProdMeal> fillProdMealsList(Cursor cursor){
        List<ProdMeal> keys = new LinkedList<ProdMeal>();
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
    private String[] getColumns(){
        String[] columns = {"id","prodID","mealID","quantity"};
        return columns;
    }
}
