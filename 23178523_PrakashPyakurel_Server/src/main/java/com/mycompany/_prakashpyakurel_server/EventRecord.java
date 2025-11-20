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

// Implements Comparable <EventRecord> so events can be sorted by time.
public class EventRecord implements Comparable<EventRecord>{
    
    //variable for each events
    private   String date;
    private   String time;
    private   String description;

    
    //Constructor 
    //this constructor is called  when new EventRecord object is created
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
    
    //compareTo() method allows to compare the EventRecord object by times and sort the list
    @Override
  public int compareTo(EventRecord event){
        return this.time.compareToIgnoreCase(event.time);
    }
}