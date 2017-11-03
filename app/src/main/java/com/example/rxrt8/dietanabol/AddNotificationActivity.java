package com.example.rxrt8.dietanabol;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class AddNotificationActivity extends AppCompatActivity {

    private EditText notificationTitle;
    private Spinner dayOfTheWeek;
    private TextView hourTV;
    private final NotificationsBaseManager notificationBaseManager = new NotificationsBaseManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notificationTitle = (EditText) findViewById(R.id.notificationTitleET);
        dayOfTheWeek = (Spinner) findViewById(R.id.dayNotifySpinner);
        hourTV = (TextView) findViewById(R.id.hourHintTV);

        floatingActionButtonListener();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    /**
     * This method is called when fab is clicked.
     * Method is responsible for adding a notification  to the base.
     * At the end change intent to NotificationsActivity.
     */
    private void floatingActionButtonListener() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notificationTitle.getText().length()!=0 && hourTV.getText().length()!=0) {
                    Notification notification = new Notification();
                    notification.setNotificationTitle(notificationTitle.getText().toString());
                    notification.setDay(dayOfTheWeek.getSelectedItem().toString());
                    notification.setDayAsInteger(getSelectedDay());
                    notification.setTime(hourTV.getText().toString());
                    notificationBaseManager.addNotification(notification);

                    Intent intent = new Intent(AddNotificationActivity.this, NotificationsActivity.class);
                    startActivity(intent);
                }
                else{
                    Snackbar.make(view, getResources().getString(R.string.lack_of_notification), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            private int getSelectedDay() {
                int day = 0;
                switch(dayOfTheWeek.getSelectedItemPosition()){
                    case 0:
                        day = 2;
                        break;
                    case 1:
                        day = 3;
                        break;
                    case 2:
                        day = 4;
                        break;
                    case 3:
                        day = 5;
                        break;
                    case 4:
                        day = 6;
                        break;
                    case 5:
                        day = 7;
                        break;
                    case 6:
                        day = 1;
                        break;
                }
                return day;
            }
        });
    }

    /**
     * This method is called when hourButton is clicked.
     */
    public void setAnHour(View view) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddNotificationActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

    /**
     * This method is called when android.R.id.home is clicked.
     * Ask the user if he wants to exit without saving changes.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && notificationTitle.getText().length()!=0 && hourTV.getText().length()!=0){
            showMessageOKCancel(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AddNotificationActivity.this, NotificationsActivity.class);
                    startActivity(intent);
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddNotificationActivity.this)
                .setMessage(getResources().getString(R.string.exit_without_saving))
                .setPositiveButton(getResources().getString(R.string.i_want_to_exit), okListener)
                .setNegativeButton(getResources().getString(R.string.i_want_to_stay), null)
                .create()
                .show();
    }
}
