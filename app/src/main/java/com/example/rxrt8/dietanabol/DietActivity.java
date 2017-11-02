package com.example.rxrt8.dietanabol;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class DietActivity extends AppCompatActivity {

    private Spinner day;
    private Button addMeal;
    private Button deleteMeal;
    private ListView mealsListView;
    private List<String> mealsArrayList = new ArrayList<>();
    private List<Integer> mealsId = new ArrayList<>();
    private int sizeOfMealsList = 0;
    private int numberOfChosenNotifications = 0;
    private final ArrayList<Integer> coloredItems = new ArrayList<Integer>();
    private final MealsBaseManager mealsBaseManager = new MealsBaseManager(this);
    private final ProdMealBaseManager prodMealBaseManager = new ProdMealBaseManager(this);
    private final ProductsBaseManager productsBaseManager = new ProductsBaseManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fillTheActivity();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);






    }

    private void fillTheActivity() {
        day = (Spinner) findViewById(R.id.dietSpinner);
        addMeal = (Button) findViewById(R.id.addMealBtn);
        deleteMeal = (Button) findViewById(R.id.deleteMealBtn);
        mealsListView = (ListView) findViewById(R.id.mealsLV);

        deleteMeal.setEnabled(FALSE);
        deleteMeal.setVisibility(View.GONE);

        setCurrentDay();
        daySpinnerListener();
        addMealOnClickListener();
        fillMealsListView();
        deleteMealOnClickListener();
    }



    private void daySpinnerListener() {
        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                fillMealsListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    private void setCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        switch(calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:
                day.setSelection(6);
                break;
            case 2:
                day.setSelection(0);
                break;
            case 3:
                day.setSelection(1);
                break;
            case 4:
                day.setSelection(2);
                break;
            case 5:
                day.setSelection(3);
                break;
            case 6:
                day.setSelection(4);
                break;
            case 7:
                day.setSelection(5);
                break;
        }
    }


    private void addMealOnClickListener() {
        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DietActivity.this, AddMealActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fillMealsListView() {
        mealsArrayList.removeAll(mealsArrayList);

        for (Meal m : mealsBaseManager.giveByDay(day.getSelectedItem().toString())) {
            StringBuilder tmp = new StringBuilder();
            tmp.append(m.getHour() + "  " + m.getMealName()+ "\n");
            for(ProdMeal p:prodMealBaseManager.giveByMealID(m.getId())){
                FoodProduct foodProduct = productsBaseManager.giveFoodProduct(p.getProdId());
                if(foodProduct.isGramsOrPieces())
                    tmp.append("             " + foodProduct.getProductName() + " " + p.getQuantity() + getResources().getString(R.string.pieces) + "\n");
                else
                    tmp.append("             " + foodProduct.getProductName() + " " + p.getQuantity() + getResources().getString(R.string.grams) + "\n");
            }
            String value = new String(tmp);
            mealsArrayList.add(value);
            sizeOfMealsList++;
            mealsId.add(m.getId());
        }

        listViewSetAdapter();
        listViewOnItemClickListener();
    }


    private void listViewSetAdapter() {
        mealsListView.setAdapter(new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                mealsArrayList){
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
        mealsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (coloredItems.contains(i)) {
                    coloredItems.remove(coloredItems.indexOf(i));
                    numberOfChosenNotifications--;
                    view.setBackgroundColor(Color.TRANSPARENT);
                    if(numberOfChosenNotifications==0){
                        deleteMeal.setEnabled(FALSE);
                        deleteMeal.setVisibility(View.GONE);
                    }
                }
                else {
                    coloredItems.add(i);
                    numberOfChosenNotifications++;
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentLightDelete));
                    if(numberOfChosenNotifications>0){
                        deleteMeal.setEnabled(TRUE);
                        deleteMeal.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void deleteMealOnClickListener() {
        deleteMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<sizeOfMealsList; i++) {
                    if (coloredItems.contains(i)) {
                        mealsBaseManager.deleteMeal(mealsId.get(i));
                        Log.d("Log ", "Deleted meal " + String.valueOf(mealsId.get(i)));

                        for (ProdMeal p : prodMealBaseManager.giveByMealID(mealsId.get(i))) {
                            Log.d("Log ", "Deleted key " + String.valueOf(p.getId()));
                            prodMealBaseManager.deleteKey(p.getId());
                        }

                    }
                }

                Intent intent = new Intent(DietActivity.this, DietActivity.class);
                startActivity(intent);
            }
        });
    }

}
