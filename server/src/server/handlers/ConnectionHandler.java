/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static server.handlers.ClientHandler.clearAll;

/**
 *
 * @author Hp
 */
public class ConnectionHandler extends Thread{
    private ServerSocket serverSocket;
    
    public ConnectionHandler(){
        try{
            serverSocket = new ServerSocket(3030);    
        }
    catch (IOException e) {
            System.out.println("Server Stopped");
        }
    }
	
    @Override
    public void run() {

            
            System.out.println("Server is running on port 3030");
            while (true) {
                try{
                    System.out.println("Waiting for a new client to connect... ");
                    Socket socket = serverSocket.accept();
                    System.out.println("Connected Successfully");
                    new ClientHandler(socket);
                }catch (IOException e) {
                    System.out.println("Server Stopped");
                    break;
            }
        } 
    }

    
    @Override
    public void interrupt(){
        super.interrupt();
         System.out.println("GGGGGG");

        try {
            serverSocket.close();
            ClientHandler.clearAll();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
