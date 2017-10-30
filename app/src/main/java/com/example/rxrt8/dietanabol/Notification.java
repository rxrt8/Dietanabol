package com.example.rxrt8.dietanabol;

/**
 * Created by rxrt8 on 2017-10-30.
 */

public class Notification {
    private int id;
    private String notificationTitle;
    private String day;
    private int dayAsInteger;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getDayAsInteger() {
        return dayAsInteger;
    }

    public void setDayAsInteger(int dayAsInteger) {
        this.dayAsInteger = dayAsInteger;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHour(){
        if(time.length()!=0){
            int hour;
            hour = Character.getNumericValue(time.charAt(0))*10;
            hour += Character.getNumericValue(time.charAt(1));
            return hour;
        }
        return 0;
    }

    public int getMinute(){
        if(time.length()!=0){
            int minute;
            minute = Character.getNumericValue(time.charAt(3))*10;
            minute += Character.getNumericValue(time.charAt(4));
            return minute;
        }
        return 0;
    }



}
