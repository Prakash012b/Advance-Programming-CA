/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany._prakashpyakurel_server;

/**
 *
 * @author pyaku
 */

// https://www.geeksforgeeks.org/java-classes-and-objects/
// https://www.baeldung.com/java-classes-objects

// Represents one event with a date, time, and short description.
public class EventRecord {
    private   String date;
    private   String time;
    private   String description;

    public EventRecord(String date, String time, String description) {
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getTime() {
        return time; 
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public String getDescription() {
        return description; 
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return time + ", " + description;
    }
}