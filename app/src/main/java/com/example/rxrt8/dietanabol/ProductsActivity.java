package com.example.rxrt8.dietanabol;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class ProductsActivity extends AppCompatActivity {


    private Button addProduct;
    private Button deleteProduct;
    private ListView productsListView;
    private List<String> productsArrayList = new ArrayList<>();
    private List<Integer> productsId = new ArrayList<>();
    private int sizeOfProductsList = 0;
    private int numberOfChosenNotifications = 0;
    private final MealsBaseManager mealsBaseManager = new MealsBaseManager(this);
    private final ProdMealBaseManager prodMealBaseManager = new ProdMealBaseManager(this);
    private final ProductsBaseManager productsBaseManager = new ProductsBaseManager(this);
    private final ArrayList<Integer> coloredItems = new ArrayList<Integer>();

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
        addProduct = (Button) findViewById(R.id.addProductBtn);
        deleteProduct = (Button) findViewById(R.id.deleteProductBtn);
        productsListView = (ListView) findViewById(R.id.productsLV);

        addProductOnClickListener();
        fillProductsListView();
        deleteProductOnClickListener();

        deleteProduct.setEnabled(FALSE);
        deleteProduct.setVisibility(View.GONE);


    }

    private void addProductOnClickListener() {
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fillProductsListView() {
        for(FoodProduct p:productsBaseManager.giveAll()){
            StringBuilder tmp = new StringBuilder();
            tmp.append(p.getProductName() + "\n");
            if(p.isRegularlyPurchased())
                tmp.append("  " + getResources().getString(R.string.regularly_purchased)+ "\n");
            else
                tmp.append("  " + getResources().getString(R.string.is_not_regularly_purchased)+ "\n");
            tmp.append("  " + p.getQuantity() + " ");
            if(p.isGramsOrPieces())
                tmp.append(getResources().getString(R.string.pieces) + "\n");
            else
                tmp.append(getResources().getString(R.string.grams) + "\n");
            String value = new String(tmp);
            productsArrayList.add(value);
            sizeOfProductsList++;
            productsId.add(p.getId());
        }

        listViewSetAdapter();
        listViewOnItemClickListener();



    }
    private void listViewSetAdapter() {
        productsListView.setAdapter(new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                productsArrayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                if (coloredItems.contains(position)) {
                    v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentLightDelete));
                } else {
                    v.setBackgroundColor(Color.TRANSPARENT);
                }

                return v;
            }
        });
    }



    private void listViewOnItemClickListener() {
        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (coloredItems.contains(i)) {
                    coloredItems.remove(coloredItems.indexOf(i));
                    numberOfChosenNotifications--;
                    view.setBackgroundColor(Color.TRANSPARENT);
                    if(numberOfChosenNotifications==0){
                        deleteProduct.setEnabled(FALSE);
                        deleteProduct.setVisibility(View.GONE);
                    }
                }
                else {
                    coloredItems.add(i);
                    numberOfChosenNotifications++;
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentLightDelete));
                    if(numberOfChosenNotifications>0){
                        deleteProduct.setEnabled(TRUE);
                        deleteProduct.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


    private void deleteProductOnClickListener() {
        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageOKCancel(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProductsAndMealsWhichHaveThisProduct();
                    }
                });

            }

            private void deleteProductsAndMealsWhichHaveThisProduct() {
                for (int i = 0; i < sizeOfProductsList; i++) {
                    if (coloredItems.contains(i)) {
                        ArrayList<Integer> keysToDelete = new ArrayList<Integer>();
                        productsBaseManager.deleteFoodProduct(productsId.get(i));

                        for (ProdMeal k : prodMealBaseManager.giveByProdID(productsId.get(i))) {
                            for(ProdMeal m: prodMealBaseManager.giveByMealID(k.getMealId())){
                                keysToDelete.add(m.getId());
                            }
                            Log.d("Log ","Deleted meal " + String.valueOf(k.getMealId()));
                            mealsBaseManager.deleteMeal(k.getMealId());
                        }
                        for(Integer k:keysToDelete){
                            Log.d("Log ","Deleted key " + String.valueOf(k));
                            prodMealBaseManager.deleteKey(k);
                        }
                    }
                }

                Intent intent = new Intent(ProductsActivity.this, ProductsActivity.class);
                startActivity(intent);

            }
        });
    }


    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ProductsActivity.this)
                .setMessage(getResources().getString(R.string.are_you_sure_you_want_to_delete_the_products))
                .setPositiveButton(getResources().getString(R.string.cancel), null)
                .setNegativeButton(getResources().getString(R.string.delete_products), okListener)
                .create()
                .show();
    }



/*




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
                break;
        }
    }

    private void deleteProductAndMealsWhichHaveThisProduct() {
        ArrayList<Integer> keysToDelete = new ArrayList<Integer>();
        int prodID = 0;
        FoodProduct f = productsBaseManager.giveByName(productToDelete.getSelectedItem().toString());
            prodID = f.getId();
            productsBaseManager.deleteFoodProduct(f.getId());

        if(prodID!=0) {
            for (ProdMeal k : prodMealBaseManager.giveByProdID(prodID)) {
                for(ProdMeal m: prodMealBaseManager.giveByMealID(k.getMealId())){
                    keysToDelete.add(m.getId());
                }
                Log.d("Log ","Deleted meal " + String.valueOf(k.getMealId()));
                mealsBaseManager.deleteMeal(k.getMealId());
            }
            for(Integer k:keysToDelete){
                Log.d("Log ","Deleted key " + String.valueOf(k));
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
    */
}
