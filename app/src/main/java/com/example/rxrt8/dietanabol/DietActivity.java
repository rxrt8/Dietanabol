package com.example.rxrt8.dietanabol;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.Calendar;

public class DietActivity extends AppCompatActivity {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = (Spinner) findViewById(R.id.dietSpinner);
        setCurrentDay();

    }

    private void setCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        switch(calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:
                spinner.setSelection(6);
                break;
            case 2:
                spinner.setSelection(0);
                break;
            case 3:
                spinner.setSelection(1);
                break;
            case 4:
                spinner.setSelection(2);
                break;
            case 5:
                spinner.setSelection(3);
                break;
            case 6:
                spinner.setSelection(4);
                break;
            case 7:
                spinner.setSelection(5);
                break;
        }
    }

    public void click(View view){
        Intent intent = null;
        switch(view.getId()){
            case R.id.addMealBtn:
                intent = new Intent(DietActivity.this, AddMealActivity.class);
                break;
        }
        startActivity(intent);
    }

}
