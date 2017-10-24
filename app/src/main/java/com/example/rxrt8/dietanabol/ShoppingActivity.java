package com.example.rxrt8.dietanabol;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

public class ShoppingActivity extends AppCompatActivity {

    private Button goShopping;
    private Button endShopping;
    private ListView productsListView;
    private List<String> productsToBuy = new ArrayList<>();
    private final ProductsBaseManager productsBaseManager = new ProductsBaseManager(this);
    ArrayList<Integer> coloredItems = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        goShopping = (Button) findViewById(R.id.goShoppingBtn);
        endShopping = (Button) findViewById(R.id.endShoppingBtn);
        productsListView = (ListView) findViewById(R.id.productsLV);

        fillProductsListView();
    }


    private void fillProductsListView() {
        for (FoodProduct p : productsBaseManager.giveAll()) {
            if(p.getQuantity()>0&&p.isRegularlyPurchased()==TRUE){
                if(p.isGramsOrPieces())
                    productsToBuy.add(p.getProductName() + "   " + p.getQuantity()+getResources().getString(R.string.pieces));
                else
                    productsToBuy.add(p.getProductName() + "   " + p.getQuantity()+getResources().getString(R.string.grams));
            }
        }

        productsListView.setAdapter(new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                productsToBuy
        ){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                if (coloredItems.contains(position)) {
                    v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentLight));
                } else {
                    v.setBackgroundColor(Color.TRANSPARENT); //or whatever was original
                }

                return v;
            }
        });

        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (coloredItems.contains(i)) {
                    coloredItems.remove(i);
                    view.setBackgroundColor(Color.TRANSPARENT);//or whatever was original
                }
                else {
                    coloredItems.add(i);
                    //add position to coloredItems
                    Log.i("Log","nowe wejscie");
                    for(int j:coloredItems){
                        Log.i("Log",String.valueOf(j));
                    }
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentLight));;
                }
            }
        });
    }
}
