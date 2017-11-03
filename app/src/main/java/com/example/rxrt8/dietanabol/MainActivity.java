package com.example.rxrt8.dietanabol;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity{

    private TextView positionOfHands;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessageInfo();
            }
        });

        checkPositionOfHands();
        setSupportActionBar(toolbar);
        handleNotification();

    }

    private void checkPositionOfHands() {
        positionOfHands = (TextView) findViewById(R.id.positionOfHandsTV);
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.WEEK_OF_YEAR)%2==0)
            positionOfHands.setText(positionOfHands.getText().toString()+ " " + getResources().getString(R.string.reverse_close_grip));
        else
            positionOfHands.setText(positionOfHands.getText().toString()+ " " + getResources().getString(R.string.close_reverse_grip));
    }

    private void showMessageInfo() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(getResources().getString(R.string.app_info))
                .setPositiveButton(getResources().getString(R.string.OK), null)
                .create()
                .show();
    }

    /**
     * A method using the AlarmReceiver class creates notifications on Android.
     */
    private void handleNotification() {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pendingIntent);
    }
    /**
     * This method is called when any button is clicked.
     */
    public void click(View view){
        Intent intent = null;
        switch(view.getId()){
            case R.id.dietBtn:
                intent = new Intent(MainActivity.this, DietActivity.class);
                break;
            case R.id.shoppingBtn:
                intent = new Intent(MainActivity.this, ShoppingActivity.class);
                break;
            case R.id.notificationsBtn:
                intent = new Intent(MainActivity.this, NotificationsActivity.class);
                break;
            case R.id.productsBtn:
                intent = new Intent(MainActivity.this, ProductsActivity.class);
        }
        startActivity(intent);
    }

}
