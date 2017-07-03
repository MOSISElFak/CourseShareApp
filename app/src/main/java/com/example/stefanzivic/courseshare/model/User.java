package com.example.stefanzivic.courseshare.model;

import java.util.HashMap;
import java.util.Map;

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


    Map<String, Boolean> _favouriteTeachers = new HashMap<>();
    Map<String, Boolean> _requestedLectures = new HashMap<>();
    Map<String, Boolean> _attendedLectures = new HashMap<>();


    private Map<String, Boolean> _myLectures = new HashMap<>();
    private Map<String, Boolean> _myPastLectures = new HashMap<>();


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

    public Map<String, Boolean> get_favouriteTeachers() {
        return _favouriteTeachers;
    }

    public void set_favouriteTeachers(Map<String, Boolean> _favouriteTeachers) {
        this._favouriteTeachers = _favouriteTeachers;
    }

    public Map<String, Boolean> get_requestedLectures() {
        return _requestedLectures;
    }

    public void set_requestedLectures(Map<String, Boolean> _requestedLectures) {
        this._requestedLectures = _requestedLectures;
    }

    public Map<String, Boolean> get_attendedLectures() {
        return _attendedLectures;
    }

    public void set_attendedLectures(Map<String, Boolean> _attendedLectures) {
        this._attendedLectures = _attendedLectures;
    }

    public Map<String, Boolean> get_myLectures() {
        return _myLectures;
    }

    public void set_myLectures(Map<String, Boolean> _myLectures) {
        this._myLectures = _myLectures;
    }

    public Map<String, Boolean> get_myPastLectures() {
        return _myPastLectures;
    }

    public void set_myPastLectures(Map<String, Boolean> _myPastLectures) {
        this._myPastLectures = _myPastLectures;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
