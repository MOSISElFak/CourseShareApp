package com.example.stefanzivic.courseshare.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ivan on 7/3/2017.
 */

public class Lecture {
    private String id;
    private String name;
    private String description;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private String picture;

    private Boolean running;
    private Boolean past;

    private String address;
    private double lat;
    private double lng;

    private String _user;
    //private Map<String, Boolean> _studentsPending = new HashMap<>();
    //private Map<String, Boolean> _studentsApproved = new HashMap<>();
    private Map<String, Boolean> _studentsAttending = new HashMap<>();



    public Lecture() {}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String get_user() {
        return _user;
    }

    public void set_user(String _user) {
        this._user = _user;
    }

//    public Map<String, Boolean> get_studentsPending() {
//        return _studentsPending;
//    }
//
//    public void set_studentsPending(Map<String, Boolean> _studentsPending) {
//        this._studentsPending = _studentsPending;
//    }
//
//    public Map<String, Boolean> get_studentsApproved() {
//        return _studentsApproved;
//    }
//
//    public void set_studentsApproved(Map<String, Boolean> _studentsApproved) {
//        this._studentsApproved = _studentsApproved;
//    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public Map<String, Boolean> get_studentsAttending() {
        return _studentsAttending;
    }

    public void set_studentsAttending(Map<String, Boolean> _studentsAttending) {
        this._studentsAttending = _studentsAttending;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getPast() {
        return past;
    }

    public void setPast(Boolean past) {
        this.past = past;
    }
}
