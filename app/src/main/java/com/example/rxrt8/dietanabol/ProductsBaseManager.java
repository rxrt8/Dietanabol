package com.example.rxrt8.dietanabol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



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
                        "id integer primary key autoincrement," +
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
        db.delete("Products", "id=?", arguments);
    }

    public void changeTheAmountOfFood(FoodProduct foodProduct, int quantity){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", foodProduct.getQuantity() + quantity);

        String args[]={foodProduct.getId()+""};
        db.update("Products", contentValues,"id=?",args);
    }


    public List<FoodProduct> giveAll(){
        List<FoodProduct> foodProducts = new LinkedList<FoodProduct>();
        String[] columns = getColumns();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Products",columns,null,null,null,null,null);
        while(cursor.moveToNext()){
            FoodProduct foodProduct = new FoodProduct();
            foodProduct.setId(cursor.getInt(0));
            foodProduct.setProductName(cursor.getString(1));
            foodProduct.setGramsOrPieces(!cursor.isNull(2));
            foodProduct.setRegularlyPurchased(!cursor.isNull(3));
            foodProduct.setQuantity(cursor.getInt(4));
            foodProducts.add(foodProduct);
        }
        cursor.close();
        return foodProducts;
    }





    public FoodProduct giveFoodProduct(int id){
        FoodProduct foodProduct = new FoodProduct();
        String[] columns = getColumns();
        SQLiteDatabase db = getReadableDatabase();
        String args[] = {id+""};
        Cursor cursor = db.query("Products",columns," id=?",args,null,null,null,null);
        if(cursor.moveToFirst()) {
            cursor.moveToFirst();
            foodProduct.setId(cursor.getInt(0));
            foodProduct.setProductName(cursor.getString(1));
            foodProduct.setGramsOrPieces(!cursor.isNull(2));
            foodProduct.setRegularlyPurchased(!cursor.isNull(3));
            foodProduct.setQuantity(cursor.getInt(4));
        }
        cursor.close();
        return foodProduct;
    }

    public List<FoodProduct> giveByName(String productName){
        List<FoodProduct> foodProducts = new LinkedList<FoodProduct>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =db.rawQuery("select id,name,gramsOrPieces,isRegularlyPurchased,quantity from Products where name='"
                +productName+
                "' order by id asc", null);
        while(cursor.moveToNext()){
            FoodProduct foodProduct = new FoodProduct();
            foodProduct.setId(cursor.getInt(0));
            foodProduct.setProductName(cursor.getString(1));
            foodProduct.setGramsOrPieces(!cursor.isNull(2));
            foodProduct.setRegularlyPurchased(!cursor.isNull(3));
            foodProduct.setQuantity(cursor.getInt(4));
            foodProducts.add(foodProduct);
        }
        cursor.close();
        return foodProducts;
    }

    private String[] getColumns(){
        String[] columns = {"id","name","gramsOrPieces","isRegularlyPurchased","quantity"};
        return columns;
    }

    public int getLastId(){
        String[] columns = getColumns();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Products",columns,null,null,null,null,null);
        int id = 0;
        while(cursor.moveToNext()){
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

}
