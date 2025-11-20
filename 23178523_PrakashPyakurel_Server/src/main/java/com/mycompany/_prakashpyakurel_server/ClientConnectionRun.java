/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._prakashpyakurel_server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;


/**
 *
 * @author pyaku
 */
public class ClientConnectionRun implements Runnable {
    private Socket client_link = null;  
    private String clientID;
    
    public static  ArrayList<EventRecord> eventBoard = new ArrayList<>();;
    
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

                    // Validate date format (eg: 2 November 2024)
                    if (!action.equals("list") &&
                            !date.matches("(?i)^([1-9]|[12][0-9]|3[01])\\s+(January|February|March|April|May|June|July|August|September|October|November|December)\\s+\\d{4}$")) {
                        throw new InvalidCommandException("Incorrect Date Format (eg: 2 November 2024)");
                    }

                    // Validate that list command uses "-" for unused fields
                    if (action.equals("list") && (!time.equals("-") || !description.equals("-"))) {
                        throw new InvalidCommandException("Please use '-' for time and description fields when listing.");
                    }
                    
                    // Catch custom exceptions
                } catch (InvalidCommandException e) {
                        out.println("InvalidCommandException: " + e.getMessage()); //send custom error back to client
                    }
                }

            } catch (IOException e) {
                System.out.println("Connection error with " + clientID);
            }
        }
    }


