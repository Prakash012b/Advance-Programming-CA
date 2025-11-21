/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._prakashpyakurel_server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;


/**
 *
 * @author pyaku
 */
//Refrences moodle
//TCP- Multithreading server from 3rd year ADvance programming moodle

//Refrences online
//https://www.geeksforgeeks.org/java-multithreaded-servers-in-java/
//https://www.baeldung.com/a-guide-to-java-sockets
//https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html
//https://stackoverflow.com/questions/31463243/regex-for-validating-date-in-dd-mmm-yyyy-format
//https://stackoverflow.com/questions/33906033/regex-for-time-in-hhmm-am-pm-format

public class ClientConnectionRun implements Runnable {
    private Socket client_link = null;  
    private String clientID;
    
    public static  ArrayList<EventRecord> eventRecord = new ArrayList<>();;
    
    //Constructor that accepts a socket connection and a unique clientID
    public ClientConnectionRun(Socket connection, String cID) {
        this.client_link = connection;
        this.clientID = cID;     
    } 
    
    
    @Override
    public void run() {
        try(
            
            //create I/O streams for client
            BufferedReader in = new BufferedReader( new InputStreamReader(client_link.getInputStream())); 
            PrintWriter out = new PrintWriter(client_link.getOutputStream(),true);
        ){
            String message;

           // keep reading messages from client
            while ((message = in.readLine()) != null) {

                // STOP command – client disconnects
                if (message.equalsIgnoreCase("stop")) {
                    out.println("TERMINATE");
                    break;
                }

                try{
                    // EVENT BOARD SECTION
                    // Splits the message by semicolons into 4 parts: action, date, time, description
                    String[] sections = message.split(";", 4);

                    // Count the number of semicolons in message to ensure exactly 3
                    int countSemi = 0;
                    for (int i = 0; i < message.length(); i++) {
                        if (message.charAt(i) == ';')
                            countSemi++;
                    }
                   

                    //ensure correct number of fields
                    if (sections.length != 4 || countSemi != 3) {
                        throw new InvalidCommandException("Wrong number of fields or semicolons. Use this format: action; date; time; description");
                    }

                    // Assign values
                    String action = sections[0].trim().toLowerCase();
                    String date = sections[1].trim();
                    String time = sections[2].trim();
                    String description = sections[3].trim();

                    // INPUT VALIDATION SECTION
                    // Only allow specific actions
                    if (!action.equals("add") && !action.equals("remove") && !action.equals("list")) {
                        throw new InvalidCommandException("Action must be add / remove / list");
                    }

                    // Validate time format using regex (eg: 1 pm, 10.30 am, 12.45 PM)
                    if (!action.equals("list") &&
                            !time.matches("^([1-9]|1[0-2])(\\.[0-5][0-9])?\\s*([AaPp][Mm])$")) {
                        throw new InvalidCommandException("Incorrect Time Format (1-12 pm/am or 1-12.xy pm/am)");
                    }

                    // Validate date format (eg: 20 November 2025)
                    if (!action.equals("list") &&
                            !date.matches("(?i)^([1-9]|[12][0-9]|3[01])\\s+(January|February|March|April|May|June|July|August|September|October|November|December)\\s+\\d{4}$")) {
                        throw new InvalidCommandException("Incorrect Date Format (eg: 2 November 2024)");
                    }

                    // Validate that list command uses "-" for unused fields
                    if (action.equals("list") && (!time.equals("-") || !description.equals("-"))) {
                        throw new InvalidCommandException("Please use '-' for time and description fields when listing.");
                    }
                    
                    // SYNCHRONIZED section - prevents multiple threads from modifying the list
                   
                    synchronized (eventRecord) {
                        
                    //Add new event
                    if (action.equals("add")) {
                        eventRecord.add(new EventRecord(date, time, description));
                    }

                        //Remove an event (if it exist)
                        else if (action.equals("remove")) {
                            boolean removed = false;
                            for (int i = 0; i < eventRecord.size(); i++) {
                                EventRecord e = eventRecord.get(i);
                                if (e.getDate().equalsIgnoreCase(date) 
                                        && e.getTime().equalsIgnoreCase(time) 
                                        && e.getDescription().equalsIgnoreCase(description)) {
                                    eventRecord.remove(i);
                                    removed = true;
                                    break;
                                }
                            }
                            if (removed == false)
                                throw new InvalidCommandException("Event does not exist"); //custom exception
                        }

                        
                        
                       //List Sorting
                       //It filters, sorts and displays the event for selected dates
                        ArrayList<EventRecord> eventsDate = new ArrayList<>();
                        for (EventRecord e : eventRecord) {
                            if (e.getDate().equalsIgnoreCase(date)){
                                eventsDate.add(e);
                            }
                        }
                        
                        //Sort the list by time (compareTo from EventRecord)
                        Collections.sort(eventsDate);
                        
                        //Display result to client
                        if (eventsDate.isEmpty()) {
                            out.println("There are no events on this date: " + date);
                        } else {
                            StringBuilder list = new StringBuilder(date + "; "); 
                            
                            //loops through the arraylist and displays each event with the current date
                            for (int i = 0; i < eventsDate.size(); i++) {
                                EventRecord e = eventsDate.get(i); 
                                list.append(e.getTime()).append(", ").append(e.getDescription());
                                if (i < eventsDate.size() - 1){
                                    list.append ("; ");
                                }
                            }
                           out.println(list.toString());
                        }
                    }
                } 

                    
                // Catch custom exceptions
                catch (InvalidCommandException e) {
                        out.println("InvalidCommandException: " + e.getMessage()); //send custom error back to client
                    }
                }

            } catch (IOException e) {
                System.out.println("Connection error with " + clientID);
            }
            
            //close connection safely
            finally {
                try {
                    System.out.println("* Closing connection with " + clientID + " *");
                    client_link.close();
                } catch (IOException e) {
                    System.out.println("Unable to close socket properly.");
                }
            }
        }
    }


