/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._prakashpyakurel_server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author pyaku
 */
public class Server {
        private static ServerSocket servSock;
        private static final int PORT = 5000;
        
    public static void main(String[] args) {
        
        System.out.println("Opening port\n");
        try {
              servSock = new ServerSocket(PORT);      //Step 1.
        }    
        catch(IOException e) 
        {
             System.out.println("Unable to attach to port!");
             System.exit(1);
        }

        do 
        {
             run();
        }while (true);
    }

    private static void run(){
              
          }
      }
 

