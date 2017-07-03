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
    private String date;
    private String picture;

    private Boolean running;


    private String _user;
    private Map<String, Boolean> _studentsPending = new HashMap<>();
    private Map<String, Boolean> _studentsApproved = new HashMap<>();
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public Map<String, Boolean> get_studentsPending() {
        return _studentsPending;
    }

    public void set_studentsPending(Map<String, Boolean> _studentsPending) {
        this._studentsPending = _studentsPending;
    }

    public Map<String, Boolean> get_studentsApproved() {
        return _studentsApproved;
    }

    public void set_studentsApproved(Map<String, Boolean> _studentsApproved) {
        this._studentsApproved = _studentsApproved;
    }

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
}
