package com.example.stefanzivic.courseshare.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Stefan Zivic on 7/3/2017.
 */

public class User {

    private String id;
    private String username;
    private String email;
    private String name;
    private String info;
    private String picture;


    Map<String, Object> _favouriteTeachers = new HashMap<>();
    Map<String, Object> _interestedLectures = new HashMap<>();
    Map<String, Object> _friendUsers = new HashMap<>();
    //Map<String, Object> _attendedLectures = new HashMap<>();


    private Map<String, Object> _myLectures = new HashMap<>();
    private Map<String, Object> _myPastLectures = new HashMap<>();


    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Map<String, Object> get_favouriteTeachers() {
        return _favouriteTeachers;
    }

    public void set_favouriteTeachers(Map<String, Object> _favouriteTeachers) {
        this._favouriteTeachers = _favouriteTeachers;
    }

    public Map<String, Object> get_interestedLectures() {
        return _interestedLectures;
    }

    public void set_interestedLectures(Map<String, Object> _interestedLectures) {
        this._interestedLectures = _interestedLectures;
    }

//    public Map<String, Object> get_attendedLectures() {
//        return _attendedLectures;
//    }
//
//    public void set_attendedLectures(Map<String, Object> _attendedLectures) {
//        this._attendedLectures = _attendedLectures;
//    }

    public Map<String, Object> get_myLectures() {
        return _myLectures;
    }

    public void set_myLectures(Map<String, Object> _myLectures) {
        this._myLectures = _myLectures;
    }

    public Map<String, Object> get_myPastLectures() {
        return _myPastLectures;
    }

    public void set_myPastLectures(Map<String, Object> _myPastLectures) {
        this._myPastLectures = _myPastLectures;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Map<String, Object> get_friendUsers() {
        return _friendUsers;
    }

    public void set_friendUsers(Map<String, Object> _friendUsers) {
        this._friendUsers = _friendUsers;
    }
}
