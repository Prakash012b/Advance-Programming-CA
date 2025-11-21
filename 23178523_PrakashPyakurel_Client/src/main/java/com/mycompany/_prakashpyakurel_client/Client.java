/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany._prakashpyakurel_client;

import java.io.*;
import java.net.*;


/**
 *
 * @author pyaku
 */
public class Client {
    private static InetAddress host;
    private static final int PORT = 5000; //must match the server port number
    
    
    public static void main(String[] args) {
        try 
       {
          host = InetAddress.getLocalHost(); //get IP for localhost
       } 
       catch(UnknownHostException e) 
       {
          System.out.println("Host not found!");
          System.exit(1);
       }
       run();
     }
    
    //Connect the client to the server and processes all user commands.
    private static void run() {
     Socket link = null;			
        try 
        {
            //Connect to the server through a TCP socket
            link = new Socket(host,PORT);
            System.out.println("Connected to event on port" + PORT +"\n");


            //setup I/O streams for communication
            BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
            PrintWriter out = new PrintWriter(link.getOutputStream(),true);

            //Set up stream for keyboard entry
            BufferedReader userEntry =new BufferedReader(new InputStreamReader(System.in));
            String message, response;

            System.out.println("Enter message to be sent to server: ");
            System.out.println("COMMAND FORMAT:");
            System.out.println("add; date; time; description");
            System.out.println("remove; date; time; description");
            System.out.println("list; date; -; -");
            System.out.println("import; <public .txt URL>");
            System.out.println("stop");
            
            
            //continuous loop for user input
            while(true) {
                System.out.print("\nEnter command: ");
                message = userEntry.readLine().trim();
                
                
                //HTTP to import message from event.txt file and use eventRecord to put them in arraylist
                if (message.toLowerCase().startsWith("import;")) {
                    String urlString = message.substring(7).trim(); // Extract link after import
                    int imported = 0;
                    int skipped = 0;

                    try{
                        URL url = new URL(urlString);
                        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                        httpCon.setRequestMethod("GET");

                        BufferedReader urlIn = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                        String line;
                    
                        // Read every line from the URL
                        while ((line = urlIn.readLine()) != null) {
                            // Each line should have date; time; description as add action in clientconnect
                            String[] parts = line.split(";", 3);
                            int countSemi = 0;

                            // this for loop count semicolons to ensure valid format
                            for (int i = 0; i < line.length(); i++) {
                                if (line.charAt(i) == ';') countSemi++;
                            }
                            
                             // Skip malformed lines
                            if (parts.length != 3 || countSemi != 2) {
                                System.out.println("Skipped malformed line (expected 3 fields): " + line);
                                skipped++;
                                continue;
                            }

                            String date = parts[0].trim();
                            String time = parts[1].trim();
                            String desc = parts[2].trim();

                             // Validate time format (eg: 1.30 pm or 10 am)
                            if (!time.matches("^([1-9]|1[0-2])(\\.[0-5][0-9])?\\s*([AaPp][Mm])$")) {
                                System.out.println("Skipped invalid time format: " + line);
                                skipped++;
                                continue;
                            }

                            // Validate date format (eg: 2 November 2025)
                            if (!date.matches("(?i)^([1-9]|[12][0-9]|3[01])\\s+(January|February|March|April|May|June|July|August|September|October|November|December)\\s+\\d{4}$")) {
                                System.out.println("Skipped invalid date format: " + line);
                                skipped++;
                                continue;
                            }
                            
                             // Send valid event to server
                            out.println("add; " + date + "; " + time + "; " + desc);
                            response = in.readLine();
                            System.out.println("<SERVER RESPONSE> " + response);
                            imported++;
                        }

                        urlIn.close(); // Close HTTP URL reader
                        
                    } catch (IOException e) {
                        System.out.println("Error fetching .txt file: " + e.getMessage());
                    System.out.println("\nImported: " + imported + " | Skipped: " + skipped);
                    
                    } 
                }
                
                else {
                    out.println(message);          // Send message to server
                    response = in.readLine();      // Receive response
                    System.out.println("\n<SERVER RESPONSE> " + response);
                }

                // Stop command – exit the client loop
                if (message.equalsIgnoreCase("stop")) {
                    break;
                
                }
            }
            
        } catch(IOException e) {
            
            System.out.println("Error communicating with the server: " + e.getMessage());
        } 
        
        
        
        //Close socket safely
        finally 
        {
            try 
            {
                System.out.println("\n* Closing connection... *");
                link.close();				
            }catch(IOException e)
            {
                System.out.println("Unable to disconnect/close!");
                System.exit(1);
            }

        }
    }
} 

