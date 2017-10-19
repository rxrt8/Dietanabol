package com.example.rxrt8.dietanabol;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;


import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class AddMealActivity extends AppCompatActivity {

    private Spinner dayOfTheWeek;
    private Spinner typeOfMeal;
    private TextView hourTV;
    private TextView preview;
    private EditText quantity;
    private ArrayList<String> productsName;
    private ArrayList<Integer> prodMealKeys = new ArrayList<>();
    private int numberOfIngredients = 0;
    private Spinner ingredient;
    private Switch gramsOrPieces;
    private Button hourButton;
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

        fillTheActivity();
        floatingActionButtonListener();
        databaseLogs();
        checkIfThereIsAnyProducts();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void fillTheActivity() {
        dayOfTheWeek = (Spinner) findViewById(R.id.dayOfTheWeekSpinner);
        hourTV = (TextView) findViewById(R.id.hourHintTV);
        hourButton = (Button) findViewById(R.id.hourButton);
        typeOfMeal = (Spinner) findViewById(R.id.typeOfMealSpinner);
        ingredient = (Spinner) findViewById(R.id.ingredientSpinner);
        gramsOrPieces = (Switch) findViewById(R.id.gramsOrPiecesSwitch);
        quantity = (EditText) findViewById(R.id.quantityET);
        preview = (TextView) findViewById(R.id.previewTV);

        fillIngredientSpinner();
    }



    private void fillIngredientSpinner() {
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
    }

    private void floatingActionButtonListener() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numberOfIngredients!=0 || hourTV.getText().length()!=0&&quantity.getText().length()!=0&&Integer.parseInt(quantity.getText().toString())>0) {
                    if(!createdMeal) {
                        Meal meal = new Meal();
                        meal.setMealName(typeOfMeal.getSelectedItem().toString());
                        meal.setDay(dayOfTheWeek.getSelectedItem().toString());
                        meal.setHour(hourTV.getText().toString());
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
    }




    public void addNextIngredient(View view){
        if(numberOfIngredients==0 && !createdMeal && Integer.parseInt(quantity.getText().toString())>0){
            Meal meal = new Meal();
            meal.setMealName(typeOfMeal.getSelectedItem().toString());
            meal.setDay(dayOfTheWeek.getSelectedItem().toString());
            meal.setHour(hourTV.getText().toString());
            mealsBaseManager.addMeal(meal);
            createdMeal = TRUE;
        }
        if(hourTV.getText().length()!=0&&quantity.getText().length()!=0 && Integer.parseInt(quantity.getText().toString())>0) {
            dayOfTheWeek.setEnabled(FALSE);
            typeOfMeal.setEnabled(FALSE);
            hourButton.setEnabled(FALSE);
            numberOfIngredients++;
            for(FoodProduct f:productsBaseManager.giveByName(ingredient.getSelectedItem().toString())){
                ProdMeal prodMeal = new ProdMeal();
                prodMeal.setMealId(mealsBaseManager.getLastId());
                prodMeal.setProdId(f.getId());
                prodMeal.setQuantity(Integer.parseInt(quantity.getText().toString()));
                productsBaseManager.changeTheAmountOfFood(f, prodMeal.getQuantity());
                prodMealBaseManager.addKey(prodMeal);
                prodMealKeys.add(prodMealBaseManager.getLastId());
                break;
            }
            quantity.setText("");
            fillPreview();
        }
        else {
            Snackbar.make(view, getResources().getString(R.string.lack_of_quantity_and_hour), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

    private void fillPreview(){
        preview.setText(getResources().getString(R.string.preview) + "\n" + hourTV.getText().toString() + "  "
                + typeOfMeal.getSelectedItem().toString() +"\n");
        for(ProdMeal p:prodMealBaseManager.giveByMealID(mealsBaseManager.getLastId())){
            FoodProduct foodProduct = productsBaseManager.giveFoodProduct(p.getProdId());
            if(foodProduct.isGramsOrPieces())
                preview.setText(preview.getText() + "             " + foodProduct.getProductName() + " " + p.getQuantity() + getResources().getString(R.string.pieces) + "\n");
            else
                preview.setText(preview.getText() + "             " + foodProduct.getProductName() + " " + p.getQuantity() + getResources().getString(R.string.grams) + "\n");
        }
    }

    public void setAnHour(View view) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddMealActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(selectedHour<10 && selectedMinute<10)
                    hourTV.setText( "0" + selectedHour + ":" + "0" + selectedMinute);
                else if(selectedHour<10)
                    hourTV.setText( "0" + selectedHour + ":" + selectedMinute);
                else if(selectedMinute<10)
                    hourTV.setText( selectedHour + ":" + "0" + selectedMinute);
                else
                    hourTV.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle(getResources().getString(R.string.select_time));
        mTimePicker.show();
    }



    private void checkIfThereIsAnyProducts() {
        if (productsBaseManager.getLastId()==0){
            showMessageBecauseThereIsNoProduct(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AddMealActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });


        }
    }




    private void databaseLogs() {
        Log.d("Log","Database");
        for(Meal m:mealsBaseManager.giveAll()){
            Log.d("Log mealsTable",String.valueOf(m.getId()) + " " + m.getDay() + " " + m.getMealName() + " " + m.getHour());
        }
        for(ProdMeal m:prodMealBaseManager.giveAll()){
            Log.d("Log prodMealTable",String.valueOf(m.getId()) + " prod " + m.getProdId() + " meal " + m.getMealId() + " quantity " + m.getQuantity());
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


    private void showMessageBecauseThereIsNoProduct(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddMealActivity.this)
                .setMessage(getResources().getString(R.string.exit_because_there_is_no_product))
                .setPositiveButton(getResources().getString(R.string.OK), okListener)
                .create()
                .show();
    }

}
