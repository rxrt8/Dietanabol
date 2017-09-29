package com.example.rxrt8.dietanabol;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;



public class ProductsActivity extends AppCompatActivity {

    private TextView productsID;
    private TextView productsName;
    private TextView productsGramsOrPieces;
    private TextView productsIsRegularlyPurchased;
    private TextView productsQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fillTheTable();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void fillTheTable() {
        BaseManager baseManager = new BaseManager(this);
        productsID = (TextView)findViewById(R.id.productsIDTV);
        productsName = (TextView)findViewById(R.id.productsNameTV);
        productsGramsOrPieces = (TextView)findViewById(R.id.productsGramsOrPiecesTV);
        productsIsRegularlyPurchased = (TextView)findViewById(R.id.productsIsRegularlyPurchasedTV);
        productsQuantity = (TextView)findViewById(R.id.productsQuantityTV);

        for(FoodProduct k:baseManager.giveAll()){
            productsID.setText(productsID.getText()+"\n"+ k.getNr());
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


    public void click(View view){
        Intent intent = null;
        switch(view.getId()){
            case R.id.addProductBtn:
                intent = new Intent(ProductsActivity.this, AddProductActivity.class);
                break;

        }
        startActivity(intent);
    }
}
