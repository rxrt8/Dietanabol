package com.example.rxrt8.dietanabol;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class AddMealActivity extends AppCompatActivity {

    private Spinner dayOfTheWeek;
    private Spinner typeOfMeal;
    private EditText hour;
    private EditText quantity;
    private ArrayList<String> productsName;
    private ArrayList<Integer> prodMealKeys = new ArrayList<>();
    private int numberOfIngredients = 0;
    private Spinner ingredient;
    private Switch gramsOrPieces;
    private Button addNextIngredient;
    private boolean createdMeal = FALSE;
    private final MealsBaseManager mealsBaseManager = new MealsBaseManager(this);
    private final ProdMealBaseManager prodMealBaseManager = new ProdMealBaseManager(this);
    private final ProductsBaseManager productsBaseManager = new ProductsBaseManager(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dayOfTheWeek = (Spinner) findViewById(R.id.dayOfTheWeekSpinner);
        hour = (EditText) findViewById(R.id.timeET);
        typeOfMeal = (Spinner) findViewById(R.id.typeOfMealSpinner);
        ingredient = (Spinner) findViewById(R.id.ingredientSpinner);
        gramsOrPieces = (Switch) findViewById(R.id.gramsOrPiecesSwitch);
        quantity = (EditText) findViewById(R.id.quantityET);
        addNextIngredient = (Button) findViewById(R.id.addNextIngredientBtn);


        productsName = new ArrayList<>();
        for(FoodProduct k:productsBaseManager.giveAll()){
            productsName.add(k.getProductName());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, productsName);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredient.setAdapter(spinnerArrayAdapter);
        gramsOrPieces.setEnabled(FALSE);

        ingredient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean isGramsOrPieces = FALSE;
                for(FoodProduct f:productsBaseManager.giveByName(ingredient.getSelectedItem().toString())){
                    isGramsOrPieces = f.isGramsOrPieces();
                    break;
                }
                gramsOrPieces.setChecked(isGramsOrPieces);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numberOfIngredients!=0 || hour.getText().length()!=0&&quantity.getText().length()!=0) {
                    if(!createdMeal) {
                        Meal meal = new Meal();
                        meal.setMealName(typeOfMeal.getSelectedItem().toString());
                        meal.setDay(dayOfTheWeek.getSelectedItem().toString());
                        meal.setHour(hour.getText().toString());
                        mealsBaseManager.addMeal(meal);
                        createdMeal = TRUE;
                    }
                    addNextIngredient(view);
                    Intent intent = new Intent(AddMealActivity.this, DietActivity.class);
                    startActivity(intent);
                }
                else{
                    Snackbar.make(view, getResources().getString(R.string.lack_of_meal), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d("dane","nowe wejscie");
        for(Meal m:mealsBaseManager.giveAll()){
            Log.d("dane z bazy posiłków",String.valueOf(m.getNr()) + " " + m.getDay() + " " + m.getMealName() + " " + m.getHour());
        }
        for(ProdMeal m:prodMealBaseManager.giveAll()){
            Log.d("dane z bazy kluczy",String.valueOf(m.getId()) + " prod " + m.getProdId() + " meal " + m.getMealId() + " ilosc " + m.getQuantity());
        }

    }


    public void addNextIngredient(View view){
        if(numberOfIngredients==0 && !createdMeal){
                Meal meal = new Meal();
                meal.setMealName(typeOfMeal.getSelectedItem().toString());
                meal.setDay(dayOfTheWeek.getSelectedItem().toString());
                meal.setHour(hour.getText().toString());
                mealsBaseManager.addMeal(meal);
                createdMeal = TRUE;
        }
        if(hour.getText().length()!=0&&quantity.getText().length()!=0) {
            dayOfTheWeek.setEnabled(FALSE);
            typeOfMeal.setEnabled(FALSE);
            hour.setEnabled(FALSE);
            numberOfIngredients++;
            for(FoodProduct f:productsBaseManager.giveByName(ingredient.getSelectedItem().toString())){
                ProdMeal prodMeal = new ProdMeal();
                prodMeal.setMealId(mealsBaseManager.getLastId());
                prodMeal.setProdId(f.getNr());
                prodMeal.setQuantity(Integer.parseInt(quantity.getText().toString()));
                productsBaseManager.addTheAmountOfFood(f, prodMeal.getQuantity());
                prodMealBaseManager.addKey(prodMeal);
                prodMealKeys.add(prodMealBaseManager.getLastId());
                break;
            }
            quantity.setText("");
        }
        else {
            Snackbar.make(view, getResources().getString(R.string.lack_of_quantity_and_hour), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && numberOfIngredients != 0){
            showMessageOKCancel(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for(int k:prodMealKeys)
                        prodMealBaseManager.deleteKey(k);
                    if(createdMeal)
                        mealsBaseManager.deleteMeal(mealsBaseManager.getLastId());


                    Intent intent = new Intent(AddMealActivity.this, DietActivity.class);
                    startActivity(intent);
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);


    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddMealActivity.this)
                .setMessage(getResources().getString(R.string.exit_without_saving))
                .setPositiveButton(getResources().getString(R.string.i_want_to_exit), okListener)
                .setNegativeButton(getResources().getString(R.string.i_want_to_stay), null)
                .create()
                .show();
    }


}
