package com.example.rxrt8.dietanabol;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Spinner;

public class AddMealActivity extends AppCompatActivity {

    private Spinner dayOfTheWeek;
    private Spinner typeOfMeal;
    private EditText hour;
    int numberOfIgredients = 0;
    private Spinner igredient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dayOfTheWeek = (Spinner) findViewById(R.id.dayOfTheWeekSpinner);
        hour = (EditText) findViewById(R.id.timeET);
        typeOfMeal = (Spinner) findViewById(R.id.typeOfMealSpinner);
        igredient = (Spinner) findViewById(R.id.ingredientSpinner);

        final MealsBaseManager mealsBaseManager = new MealsBaseManager(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numberOfIgredients!=0) {
                    Meal meal = new Meal();
                    meal.setMealName(typeOfMeal.getSelectedItem().toString());
                    meal.setDay(dayOfTheWeek.getSelectedItem().toString());
                    meal.setHour(hour.getText().toString());
                    mealsBaseManager.addMeal(meal);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && numberOfIgredients != 0){
            showMessageOKCancel(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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
