package com.example.rxrt8.dietanabol;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class AddProductActivity extends AppCompatActivity {

    private EditText productName;
    private Switch gramsOrPieces;
    private Switch isRegularlyPurchased;
    private final ProductsBaseManager productsBaseManager = new ProductsBaseManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        productName = (EditText) findViewById(R.id.productNameET);
        gramsOrPieces = (Switch) findViewById(R.id.gramsOrPiecesSwitch);
        isRegularlyPurchased = (Switch) findViewById(R.id.isRegularlyPurchasedSwitch);

        floatingActionButtonListener();



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * This method is called when fab is clicked.
     * Method is responsible for adding a product to the base.
     * At the end change intent to NotificationsActivity.
     */
    private void floatingActionButtonListener() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FoodProduct p = productsBaseManager.giveByName(productName.getText().toString());
                checkIfProductNameIsUnique(p, view);
                ifPossibleCreateAProduct();

                /**
                 * this if will called only if product can't be created
                 * */
                if(p.getId()==-1){
                    Snackbar.make(view, getResources().getString(R.string.lack_of_product), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            private void ifPossibleCreateAProduct() {
                if(productName.getText().length()!=0) {
                    FoodProduct foodProduct = new FoodProduct();
                    foodProduct.setProductName(productName.getText().toString());
                    foodProduct.setGramsOrPieces(gramsOrPieces.isChecked());
                    foodProduct.setRegularlyPurchased(isRegularlyPurchased.isChecked());
                    productsBaseManager.addFoodProduct(foodProduct);
                    Intent intent = new Intent(AddProductActivity.this, ProductsActivity.class);
                    startActivity(intent);
                }
            }

            private void checkIfProductNameIsUnique(FoodProduct foodProduct, View view) {
                if(foodProduct.getId()!=-1){
                    productName.setText("");
                    Snackbar.make(view, getResources().getString(R.string.product_name_is_not_unique), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    /**
     * This method is called when android.R.id.home is clicked.
     * Ask the user if he wants to exit without saving changes.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && productName.getText().length()!=0){
            showMessageOKCancel(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AddProductActivity.this, ProductsActivity.class);
                    startActivity(intent);
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);


    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddProductActivity.this)
                .setMessage(getResources().getString(R.string.exit_without_saving))
                .setPositiveButton(getResources().getString(R.string.i_want_to_exit), okListener)
                .setNegativeButton(getResources().getString(R.string.i_want_to_stay), null)
                .create()
                .show();
    }
}
