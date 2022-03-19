/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author Hp
 */
public class ClientConnection extends Thread{
    public String serverResponse;
    public Socket socket;
    public BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;
    
    public ClientConnection(Socket socket) throws IOException{
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //start();
        }catch(IOException ex){
            System.out.println("Error in connecting");
            ex.printStackTrace();
            closeAll(socket, bufferedReader, bufferedWriter); 
        }
    }
    
    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
        if(bufferedReader != null){
            bufferedReader.close();
        }
        if(bufferedWriter != null){
            bufferedWriter.close();
        }
        if(socket != null){
            socket.close();
        }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public void sendRequestToServer(String req){
        try{
            bufferedWriter.write(req);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch(IOException ex){
            ex.printStackTrace();
            System.out.println("Error in sending");
            closeAll(socket, bufferedReader,bufferedWriter);
        }
    }
    
//    public void run(){
//        while(socket.isConnected()){
//                try{
//                    String res = bufferedReader.readLine();
//                    System.out.println(res);
//                   
//                }catch(IOException ex){
//                    ex.printStackTrace();
//                    System.out.println("Error in receving");
//                    closeAll(socket, bufferedReader,bufferedWriter);
//                    break;
//                }
//            }
//    }
    
    public String getResponseFromServer(){
                serverResponse = null;
                while(serverResponse == null && !(socket.isClosed())){
                    try{
                        serverResponse = bufferedReader.readLine();
                        System.out.println(serverResponse + "Hellow ");
                        break;
                    }catch(IOException ex){
                        System.out.println("Error in Listenening");
                        ex.printStackTrace();
                        closeAll(socket, bufferedReader, bufferedWriter); 
                    }
                    
                }
                  return serverResponse;
    }
    
    
   
}
