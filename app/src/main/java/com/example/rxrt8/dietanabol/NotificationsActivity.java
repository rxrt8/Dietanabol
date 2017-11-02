package com.example.rxrt8.dietanabol;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class NotificationsActivity extends AppCompatActivity {

    private Button addNewNotification;
    private Button deleteNotification;
    private ListView notificationsListView;
    private List<String> notificationsArrayList = new ArrayList<>();
    private List<Integer> notificationsId = new ArrayList<>();
    private int sizeOfNotificationsList = 0;
    private int numberOfChosenNotifications = 0;
    private final NotificationsBaseManager notificationsBaseManager = new NotificationsBaseManager(this);
    private final ArrayList<Integer> coloredItems = new ArrayList<Integer>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fillTheActivity();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    private void fillTheActivity() {
        addNewNotification = (Button) findViewById(R.id.addNewNotificationBtn);
        deleteNotification = (Button) findViewById(R.id.deleteNotificationBtn);
        notificationsListView = (ListView) findViewById(R.id.notificationsLV);

        deleteNotification.setEnabled(FALSE);
        deleteNotification.setVisibility(View.GONE);

        addNewNotificationOnClickListener();
        fillNotificationsListView();
        deleteNotificationOnClickListener();



    }



    private void addNewNotificationOnClickListener() {
        addNewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationsActivity.this, AddNotificationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fillNotificationsListView() {
        for(Notification n : notificationsBaseManager.giveAll()){
            notificationsArrayList.add(n.getNotificationTitle() + "  " + n.getDay() + " " + n.getTime());
            sizeOfNotificationsList++;
            notificationsId.add(n.getId());
        }

        listViewSetAdapter();
        listViewOnItemClickListener();
    }

    private void listViewSetAdapter() {
        notificationsListView.setAdapter(new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
               notificationsArrayList){
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
        notificationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (coloredItems.contains(i)) {
                    coloredItems.remove(coloredItems.indexOf(i));
                    numberOfChosenNotifications--;
                    view.setBackgroundColor(Color.TRANSPARENT);
                    if(numberOfChosenNotifications==0){
                        deleteNotification.setEnabled(FALSE);
                        deleteNotification.setVisibility(View.GONE);
                    }
                }
                else {
                    coloredItems.add(i);
                    numberOfChosenNotifications++;
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentLightDelete));
                    if(numberOfChosenNotifications>0){
                        deleteNotification.setEnabled(TRUE);
                        deleteNotification.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void deleteNotificationOnClickListener() {
        deleteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<sizeOfNotificationsList; i++)
                    if (coloredItems.contains(i))
                        notificationsBaseManager.deleteNotification(notificationsId.get(i));

                Intent intent = new Intent(NotificationsActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }
        });
    }

}
