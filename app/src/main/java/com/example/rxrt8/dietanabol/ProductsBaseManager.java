package com.example.rxrt8.dietanabol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.LinkedList;
import java.util.List;

/**
 * Created by rxrt8 on 2017-09-13.
 */

public class ProductsBaseManager extends SQLiteOpenHelper{

    public ProductsBaseManager(Context context) {
        super(context, "products.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table Products(" +
                        "nr integer primary key autoincrement," +
                        "name text," +
                        "gramsOrPieces boolean," +
                        "isRegularlyPurchased boolean," +
                        "quantity integer);" +
                        "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addFoodProduct(FoodProduct foodProduct){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", foodProduct.getProductName());
        if(foodProduct.isGramsOrPieces())
            contentValues.put("gramsOrPieces", foodProduct.isGramsOrPieces());
        if(foodProduct.isRegularlyPurchased())
            contentValues.put("isRegularlyPurchased", foodProduct.isRegularlyPurchased());
        contentValues.put("quantity", foodProduct.getQuantity());
        db.insertOrThrow("Products", null, contentValues);

    }

    public void deleteFoodProduct(int id){
        SQLiteDatabase db = getWritableDatabase();
        String[] arguments={""+id};
        db.delete("Products", "nr=?", arguments);
    }

    public void addTheAmountOfFood(FoodProduct foodProduct, int quantity){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", foodProduct.getProductName());
        if(foodProduct.isGramsOrPieces())
            contentValues.put("gramsOrPieces", foodProduct.isGramsOrPieces());
        if(foodProduct.isRegularlyPurchased())
            contentValues.put("isRegularlyPurchased", foodProduct.isRegularlyPurchased());
        contentValues.put("quantity", foodProduct.getQuantity() + quantity);
        String args[]={foodProduct.getNr()+""};
        db.update("Products", contentValues,"nr=?",args);
    }

    public void deleteTheAmountOfFood(FoodProduct foodProduct, int quantity){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", foodProduct.getProductName());
        if(foodProduct.isGramsOrPieces())
            contentValues.put("gramsOrPieces", foodProduct.isGramsOrPieces());
        if(foodProduct.isRegularlyPurchased())
            contentValues.put("isRegularlyPurchased", foodProduct.isRegularlyPurchased());
        if(foodProduct.getQuantity() - quantity>0) {
            contentValues.put("quantity", foodProduct.getQuantity() - quantity);
        }
        else{
            contentValues.put("quantity", 0);
        }

        String args[]={foodProduct.getNr()+""};
        db.update("Products", contentValues,"nr=?",args);
    }

    public List<FoodProduct> giveAll(){
        List<FoodProduct> foodProducts = new LinkedList<FoodProduct>();
        String[] columns={"nr","name","gramsOrPieces","isRegularlyPurchased","quantity"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =db.query("Products",columns,null,null,null,null,null);
        while(cursor.moveToNext()){
            FoodProduct foodProduct = new FoodProduct();
            foodProduct.setNr(cursor.getInt(0));
            foodProduct.setProductName(cursor.getString(1));
            foodProduct.setGramsOrPieces(!cursor.isNull(2));
            foodProduct.setRegularlyPurchased(!cursor.isNull(3));
            foodProduct.setQuantity(cursor.getInt(4));
            foodProducts.add(foodProduct);
        }
        return foodProducts;
    }





    public FoodProduct giveFoodProduct(int nr){
        FoodProduct foodProduct = new FoodProduct();
        String[] columns={"nr","name","gramsOrPieces","isRegularlyPurchased","quantity"};
        SQLiteDatabase db = getReadableDatabase();
        String args[]={nr+""};
        Cursor cursor=db.query("Products",columns," nr=?",args,null,null,null,null);

        if(cursor.moveToFirst()) {
            cursor.moveToFirst();
            foodProduct.setNr(cursor.getInt(0));
            foodProduct.setProductName(cursor.getString(1));
            foodProduct.setGramsOrPieces(!cursor.isNull(2));
            foodProduct.setRegularlyPurchased(!cursor.isNull(3));
            foodProduct.setQuantity(cursor.getInt(4));
        }
        return foodProduct;
    }

    public List<FoodProduct> giveByName(String productName){
        List<FoodProduct> foodProducts = new LinkedList<FoodProduct>();
        String[] columns={"nr","name","gramsOrPieces","isRegularlyPurchased","quantity"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =db.rawQuery("select nr,name,gramsOrPieces,isRegularlyPurchased,quantity from Products where name='"
                +productName+
                "' order by nr asc", null);
        while(cursor.moveToNext()){
            FoodProduct foodProduct = new FoodProduct();
            foodProduct.setNr(cursor.getInt(0));
            foodProduct.setProductName(cursor.getString(1));
            foodProduct.setGramsOrPieces(!cursor.isNull(2));
            foodProduct.setRegularlyPurchased(!cursor.isNull(3));
            foodProduct.setQuantity(cursor.getInt(4));
            foodProducts.add(foodProduct);
        }
        return foodProducts;
    }


}
