package com.example.rxrt8.dietanabol;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ShoppingActivity extends AppCompatActivity {

    private Button goForNewShopping;
    private Button endShopping;
    private Button goFinishTheLastShopping;
    private ListView productsListView;
    private int sizeOfProductsList = 0;
    private List<String> productsToBuy = new ArrayList<>();
    private List<String> productsName = new ArrayList<>();
    private final ProductsBaseManager productsBaseManager = new ProductsBaseManager(this);
    private final ArrayList<Integer> coloredItems = new ArrayList<Integer>(100);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        goForNewShopping = (Button) findViewById(R.id.goForNewShoppingBtn);
        endShopping = (Button) findViewById(R.id.endShoppingBtn);
        goFinishTheLastShopping = (Button) findViewById(R.id.goFinishTheLastShoppingBtn);
        productsListView = (ListView) findViewById(R.id.productsLV);
        productsListView.setEnabled(FALSE);
        endShopping.setEnabled(FALSE);
        goFinishTheLastShopping.setEnabled(FALSE);
        goFinishTheLastShopping.setVisibility(View.GONE);
        productsListView.setVisibility(View.GONE);
        endShopping.setVisibility(View.GONE);


        checkIfThereAreAnyProducts();
        checkIfThereAreAnyOldPurchases();
        goForNewShoppingOnClickListener();
        goFinishTheLastShoppingOnClickListener();
        endShoppingOnClickListener();

    }




    private void checkIfThereAreAnyProducts() {
        Boolean thereAreNoProducts = TRUE;
        for (FoodProduct p : productsBaseManager.giveAll()) {
            if (p.getQuantity() > 0 && p.isRegularlyPurchased() == TRUE) {
                thereAreNoProducts = FALSE;
                break;
            }
        }

        if (thereAreNoProducts){
            showMessageBecauseThereAreNoProducts(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ShoppingActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void showMessageBecauseThereAreNoProducts(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ShoppingActivity.this)
                .setMessage(getResources().getString(R.string.exit_because_there_is_no_product_which_is_regularly_purchased))
                .setPositiveButton(getResources().getString(R.string.OK), okListener)
                .create()
                .show();
    }

    private void checkIfThereAreAnyOldPurchases() {
        Boolean thereAreOldPurchases = FALSE;
        for (FoodProduct p : productsBaseManager.giveAll()) {
            if (p.getQuantity()>0&&p.isRegularlyPurchased()==TRUE&&p.getTimesWhenProductWasNotPurchased()>0 ) {
                thereAreOldPurchases = TRUE;
                break;
            }
        }
        if(thereAreOldPurchases){
            goFinishTheLastShopping.setEnabled(TRUE);
            goFinishTheLastShopping.setVisibility(View.VISIBLE);
        }
        else{
            goForNewShopping();
        }
    }

    private void fillProductsListView() {
        for (FoodProduct p : productsBaseManager.giveAll()) {
            if(p.getQuantity()>0&&p.isRegularlyPurchased()==TRUE&&p.getTimesWhenProductWasNotPurchased()>0){
                int quantity = p.getTimesWhenProductWasNotPurchased()*p.getQuantity();
                if(p.isGramsOrPieces())
                    productsToBuy.add(p.getProductName() + "   " + quantity + getResources().getString(R.string.pieces));
                else
                    productsToBuy.add(p.getProductName() + "   " + quantity + getResources().getString(R.string.grams));
                sizeOfProductsList++;
                productsName.add(p.getProductName());
            }
        }

        listViewSetAdapter();
        listViewOnItemClickListener();

    }

    private void listViewSetAdapter() {
        productsListView.setAdapter(new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                productsToBuy){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                if (coloredItems.contains(position)) {
                    v.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentLight));
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
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
                else {
                    coloredItems.add(i);
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentLight));;
                }
            }
        });
    }

    private void goForNewShoppingOnClickListener() {
        goForNewShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goForNewShopping();
            }
        });
    }

    private void goForNewShopping(){
        for (FoodProduct p : productsBaseManager.giveAll()) {
            productsBaseManager.updateTimesWhenProductWasNotPurchased(p,p.getTimesWhenProductWasNotPurchased() + 1);
        }
        fillProductsListView();
        fillActivityDuringShopping();

    }


    private void fillActivityDuringShopping() {
        productsListView.setEnabled(TRUE);
        productsListView.setVisibility(View.VISIBLE);

        goForNewShopping.setEnabled(FALSE);
        goForNewShopping.setVisibility(View.GONE);
        goFinishTheLastShopping.setEnabled(FALSE);
        goFinishTheLastShopping.setVisibility(View.GONE);
        endShopping.setEnabled(TRUE);
        endShopping.setVisibility(View.VISIBLE);
    }

    private void goFinishTheLastShoppingOnClickListener() {
        goFinishTheLastShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillProductsListView();
                fillActivityDuringShopping();
            }
        });
    }

    private void endShoppingOnClickListener() {
        endShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<sizeOfProductsList; i++){
                    if (coloredItems.contains(i)) {
                        coloredItems.remove(coloredItems.indexOf(i));
                        for(FoodProduct f:productsBaseManager.giveByName(productsName.get(i))){
                            productsBaseManager.updateTimesWhenProductWasNotPurchased(f, 0);
                        }
                    }
                }
                Intent intent = new Intent(ShoppingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
