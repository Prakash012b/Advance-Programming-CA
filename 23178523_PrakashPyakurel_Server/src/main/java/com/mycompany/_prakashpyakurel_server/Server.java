/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._prakashpyakurel_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author pyaku
 */

//Refrences Moodle
//TCP-Multithreaded server 3rd year Advance programming 


public class Server {
    
    //setup TCP socket for client connection
        private static ServerSocket servSock;
        private static final int PORT = 5000;//port number the server listen on
        private static int clientConnections = 0;//to assign unique IDs to each connectedclient
        
    public static void main(String[] args) {
        
        System.out.println("Opening port\n");
        try {
            //create a serverSocket at specific port
              servSock = new ServerSocket(PORT);  
              System.out.println("Server started succesfully on port: "+ PORT);
        }    
        catch(IOException e) 
        {
            //Handle error if the port is already in use or not available
             System.out.println("Unable to attach to port" +PORT+ "!");
             System.exit(1);
        }

        //keeps server running continuously to accept multiple clients
        do 
        {
             run();//Accept and process a new client connection
        }while (true);
    }

    
    //Run method waits for new client to connect and assign them a unique ID, and start a new thread for communication
  private static void run(){
    Socket link = null;                       
    
    try 
    {
        link = servSock.accept(); //blocks until a client connects
        clientConnections++;  //increment connection counter and assign new unique client ID
        String client_ID = "Client "+ clientConnections;
       
        //create a new thread to handle this client
        Runnable resource = new ClientConnectionRun(link, client_ID);
        Thread t = new Thread (resource);
        t.start(); // start the thread
    }
    
    catch(IOException e1){
        //prints the error message if the connection fails
        e1.printStackTrace();
        
        //attempt to close the socket if connection fails
        try {
            link.close();				   
	}
        
        catch(IOException e2)
        {
            System.out.println("Unable to disconnect!");
	    System.exit(1);
        }
    }
  }
}
 

