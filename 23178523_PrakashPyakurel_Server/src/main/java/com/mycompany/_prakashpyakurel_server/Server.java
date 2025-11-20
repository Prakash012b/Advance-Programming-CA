/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._prakashpyakurel_server;

/**
 *
 * @author pyaku
 */
public class Server {
        private static final int PORT = 5000;
        
    public static void main(String[] args) {
        
        System.out.println("Opening port\n");
        
        try (ServerSocket servSock = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            Socket link = servSock.accept();
            System.out.println("Client connected: " + link);
            
            link.close();
            
        } catch (IOException e) {
            System.out.println("Unable to catch port!");
        }
    }
}
