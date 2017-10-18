package com.example.rxrt8.dietanabol;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class ProductsActivity extends AppCompatActivity {

    private TextView productsID;
    private TextView productsName;
    private TextView productsGramsOrPieces;
    private TextView productsIsRegularlyPurchased;
    private TextView productsQuantity;
    private TextView productToDeleteTV;
    private Spinner productToDelete;
    private Button addProduct;
    private Button deleteProduct;
    private Button cancel;
    private Button saveAndDelete;
    private final MealsBaseManager mealsBaseManager = new MealsBaseManager(this);
    private final ProdMealBaseManager prodMealBaseManager = new ProdMealBaseManager(this);
    private final ProductsBaseManager productsBaseManager = new ProductsBaseManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fillTheActivity();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void fillTheActivity() {

        productsID = (TextView)findViewById(R.id.productsIDTV);
        productsName = (TextView)findViewById(R.id.productsNameTV);
        productsGramsOrPieces = (TextView)findViewById(R.id.productsGramsOrPiecesTV);
        productsIsRegularlyPurchased = (TextView)findViewById(R.id.productsIsRegularlyPurchasedTV);
        productsQuantity = (TextView)findViewById(R.id.productsQuantityTV);
        productToDeleteTV = (TextView)findViewById(R.id.productToDeleteTV);
        productToDelete = (Spinner)findViewById(R.id.productToDeleteSpinner);
        addProduct = (Button) findViewById(R.id.addProductBtn);
        deleteProduct = (Button) findViewById(R.id.deleteProductBtn);
        cancel = (Button) findViewById(R.id.cancelProductDeletionBtn);
        saveAndDelete = (Button) findViewById(R.id.saveAndDeleteProductBtn);

        productToDeleteTV.setVisibility(View.GONE);
        productToDelete.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        saveAndDelete.setVisibility(View.GONE);
        productToDelete.setEnabled(FALSE);
        cancel.setEnabled(FALSE);
        saveAndDelete.setEnabled(FALSE);


        fillTextViews();


    }


    void fillTextViews(){
        for(FoodProduct k:productsBaseManager.giveAll()){
            productsID.setText(productsID.getText()+"\n"+ k.getId());
            productsName.setText(productsName.getText()+"\n"+k.getProductName());
            if(k.isGramsOrPieces())
                productsGramsOrPieces.setText(productsGramsOrPieces.getText()+"\n"+getResources().getString(R.string.pieces));
            else
                productsGramsOrPieces.setText(productsGramsOrPieces.getText()+"\n"+getResources().getString(R.string.grams));
            if(k.isRegularlyPurchased())
                productsIsRegularlyPurchased.setText(productsIsRegularlyPurchased.getText()+"\n"+getResources().getString(R.string.yes));
            else
                productsIsRegularlyPurchased.setText(productsIsRegularlyPurchased.getText()+"\n"+getResources().getString(R.string.no));
            productsQuantity.setText(productsQuantity.getText()+"\n"+k.getQuantity());
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ProductsActivity.this)
                .setMessage(getResources().getString(R.string.exit_without_deleting))
                .setPositiveButton(getResources().getString(R.string.i_want_to_exit), okListener)
                .setNegativeButton(getResources().getString(R.string.i_want_to_stay), null)
                .create()
                .show();
    }

    public void click(View view){
        switch(view.getId()){
            case R.id.addProductBtn:
                Intent intent = new Intent(ProductsActivity.this, AddProductActivity.class);
                startActivity(intent);
                break;
            case R.id.deleteProductBtn:
                deleteProductView();
                break;
            case R.id.cancelProductDeletionBtn:
                cancelProductDeletion();
                break;
            case R.id.saveAndDeleteProductBtn:
                deleteProductAndMealsWhichHaveThisProduct();

        }
    }

    private void deleteProductAndMealsWhichHaveThisProduct() {
        ArrayList<Integer> keysToDelete = new ArrayList<Integer>();
        int prodID = 0;
        for(FoodProduct f : productsBaseManager.giveByName(productToDelete.getSelectedItem().toString())){
            prodID = f.getId();
            productsBaseManager.deleteFoodProduct(f.getId());
            break;
        }
        if(prodID!=0) {
            for (ProdMeal k : prodMealBaseManager.giveByProdID(prodID)) {
                for(ProdMeal m: prodMealBaseManager.giveByMealID(k.getMealId())){
                    keysToDelete.add(m.getId());
                }
                Log.d("Log ","Deleted meal " + String.valueOf(k.getMealId()));
                mealsBaseManager.deleteMeal(k.getMealId());
            }
            for(Integer k:keysToDelete){
                Log.d("Log ","Deleted product " + String.valueOf(k));
                prodMealBaseManager.deleteKey(k);
            }
        }

        Intent intent = new Intent(ProductsActivity.this, ProductsActivity.class);
        startActivity(intent);
    }

    private void cancelProductDeletion() {
        productToDeleteTV.setVisibility(View.GONE);
        productToDelete.setVisibility(View.GONE);
        productsID.setVisibility(View.VISIBLE);
        productsName.setVisibility(View.VISIBLE);
        productsGramsOrPieces.setVisibility(View.VISIBLE);
        productsIsRegularlyPurchased.setVisibility(View.VISIBLE);
        productsQuantity.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);
        saveAndDelete.setVisibility(View.GONE);

        productToDelete.setEnabled(FALSE);
        addProduct.setEnabled(TRUE);
        deleteProduct.setEnabled(TRUE);
        cancel.setEnabled(FALSE);
        saveAndDelete.setEnabled(FALSE);
    }

    private void deleteProductView() {
        showMessageOKCancel(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelProductDeletion();
            }
        });
        productToDeleteTV.setVisibility(View.VISIBLE);
        productToDelete.setVisibility(View.VISIBLE);
        productsID.setVisibility(View.GONE);
        productsName.setVisibility(View.GONE);
        productsGramsOrPieces.setVisibility(View.GONE);
        productsIsRegularlyPurchased.setVisibility(View.GONE);
        productsQuantity.setVisibility(View.GONE);
        cancel.setVisibility(View.VISIBLE);
        saveAndDelete.setVisibility(View.VISIBLE);

        productToDelete.setEnabled(TRUE);
        addProduct.setEnabled(FALSE);
        deleteProduct.setEnabled(FALSE);
        cancel.setEnabled(TRUE);
        saveAndDelete.setEnabled(TRUE);

        ArrayList<String> productsNameArrayList = new ArrayList<>();
        for(FoodProduct k:productsBaseManager.giveAll()){
            productsNameArrayList.add(k.getProductName());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, productsNameArrayList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productToDelete.setAdapter(spinnerArrayAdapter);

    }

}
