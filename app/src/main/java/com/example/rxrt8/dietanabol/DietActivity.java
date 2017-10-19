package com.example.rxrt8.dietanabol;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class DietActivity extends AppCompatActivity {

    private Spinner day;
    private TextView dayDiet;
    private TextView mealToDeleteTV;
    private TextView deletedMealPreview;
    private Spinner mealToDelete;
    private Button addMeal;
    private Button deleteMeal;
    private Button cancel;
    private Button saveAndDelete;
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
        dayDiet = (TextView) findViewById(R.id.dayDietTV);
        mealToDeleteTV = (TextView) findViewById(R.id.mealToDeleteTV);
        mealToDelete = (Spinner) findViewById(R.id.mealToDeleteSpinner);
        addMeal = (Button) findViewById(R.id.addMealBtn);
        deleteMeal = (Button) findViewById(R.id.deleteMealBtn);
        cancel = (Button) findViewById(R.id.cancelMealDeletionBtn);
        saveAndDelete = (Button) findViewById(R.id.saveAndDeleteMealBtn);
        deletedMealPreview = (TextView) findViewById(R.id.deletedMealPreviewTV);

        mealToDeleteTV.setVisibility(View.GONE);
        mealToDelete.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        saveAndDelete.setVisibility(View.GONE);
        deletedMealPreview.setVisibility(View.GONE);
        mealToDelete.setEnabled(FALSE);
        cancel.setEnabled(FALSE);
        saveAndDelete.setEnabled(FALSE);


        setCurrentDay();
        daySpinnerListener();
        fillTextViews();
    }

    private void daySpinnerListener() {
        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                fillTextViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }


    void fillTextViews(){
        dayDiet.setText("");
        for(Meal m:mealsBaseManager.giveByDay(day.getSelectedItem().toString())){
            dayDiet.setText(dayDiet.getText() + "\n" + m.getHour() + "  " + m.getMealName()+ "\n");
            for(ProdMeal p:prodMealBaseManager.giveByMealID(m.getId())){
                FoodProduct foodProduct = productsBaseManager.giveFoodProduct(p.getProdId());
                if(foodProduct.isGramsOrPieces())
                    dayDiet.setText(dayDiet.getText() + "             " + foodProduct.getProductName() + " " + p.getQuantity() + getResources().getString(R.string.pieces) + "\n");
                else
                    dayDiet.setText(dayDiet.getText() + "             " + foodProduct.getProductName() + " " + p.getQuantity() + getResources().getString(R.string.grams) + "\n");
            }

        }
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

    public void click(View view){
        switch(view.getId()){
            case R.id.addMealBtn:
                Intent intent = new Intent(DietActivity.this, AddMealActivity.class);
                startActivity(intent);
                break;
            case R.id.deleteMealBtn:
                deleteMealView();
                break;
            case R.id.cancelMealDeletionBtn:
                cancelMealDeletion();
                break;
            case R.id.saveAndDeleteMealBtn:
                deleteMeal();
                break;
        }
    }

    private void deleteMeal() {
        int mealID = 0;
        for(Meal m:mealsBaseManager.giveByHour(mealToDelete.getSelectedItem().toString())){
            if(m.getDay().equals(day.getSelectedItem().toString())){
                mealID = m.getId();
                mealsBaseManager.deleteMeal(mealID);
                Log.d("Log ","Deleted meal " + String.valueOf(mealID));
                break;
            }
        }
        if(mealID!=0){
            for(ProdMeal p : prodMealBaseManager.giveByMealID(mealID)){
                Log.d("Log ","Deleted key " + String.valueOf(p.getId()));
                prodMealBaseManager.deleteKey(p.getId());
            }
        }

        Intent intent = new Intent(DietActivity.this, DietActivity.class);
        startActivity(intent);

    }

    private void cancelMealDeletion() {
        mealToDeleteTV.setVisibility(View.GONE);
        mealToDelete.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        saveAndDelete.setVisibility(View.GONE);
        deletedMealPreview.setVisibility(View.GONE);
        mealToDelete.setEnabled(FALSE);
        cancel.setEnabled(FALSE);
        saveAndDelete.setEnabled(FALSE);

        addMeal.setEnabled(TRUE);
        deleteMeal.setEnabled(TRUE);
        day.setEnabled(TRUE);
        dayDiet.setEnabled(TRUE);
        dayDiet.setVisibility(View.VISIBLE);
    }



    private void deleteMealView() {
        mealToDeleteTV.setVisibility(View.VISIBLE);
        mealToDelete.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
        saveAndDelete.setVisibility(View.VISIBLE);
        deletedMealPreview.setVisibility(View.VISIBLE);
        mealToDelete.setEnabled(TRUE);
        cancel.setEnabled(TRUE);
        saveAndDelete.setEnabled(TRUE);

        addMeal.setEnabled(FALSE);
        deleteMeal.setEnabled(FALSE);
        day.setEnabled(FALSE);
        dayDiet.setEnabled(FALSE);
        dayDiet.setVisibility(View.GONE);

        ArrayList<String> mealsHoursArrayList = new ArrayList<>();
        for(Meal m:mealsBaseManager.giveByDay(day.getSelectedItem().toString())){
            mealsHoursArrayList.add(m.getHour());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mealsHoursArrayList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealToDelete.setAdapter(spinnerArrayAdapter);

        mealToDeleteSpinnerListener();



    }

    private void mealToDeleteSpinnerListener() {
        mealToDelete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                fillPreviewForDeletedMeal();
            }

            private void fillPreviewForDeletedMeal() {
                for(Meal m:mealsBaseManager.giveByHour(mealToDelete.getSelectedItem().toString())) {
                    if (m.getDay().equals(day.getSelectedItem().toString())) {
                        deletedMealPreview.setText(getResources().getString(R.string.preview)+ "\n" + m.getHour() + "  " + m.getMealName()+ "\n");
                        for(ProdMeal p:prodMealBaseManager.giveByMealID(m.getId())){
                            FoodProduct foodProduct = productsBaseManager.giveFoodProduct(p.getProdId());
                            if(foodProduct.isGramsOrPieces())
                                deletedMealPreview.setText(deletedMealPreview.getText() + "             " + foodProduct.getProductName() + " " + p.getQuantity() + getResources().getString(R.string.pieces) + "\n");
                            else
                                deletedMealPreview.setText(deletedMealPreview.getText() + "             " + foodProduct.getProductName() + " " + p.getQuantity() + getResources().getString(R.string.grams) + "\n");
                        }
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

}
