package com.example.rxrt8.dietanabol;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handleNotification();


    }

    private void handleNotification() {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pendingIntent);
    }

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
