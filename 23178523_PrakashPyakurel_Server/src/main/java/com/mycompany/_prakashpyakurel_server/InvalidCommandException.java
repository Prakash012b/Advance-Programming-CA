/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._prakashpyakurel_server;

/**
 *
 * @author pyaku
 */

// Custom exception for malformed client commands.
 
public class InvalidCommandException extends Exception{

    public InvalidCommandException(String message) {
        super(message);
    }
}

