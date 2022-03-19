/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import static server.handlers.RequestHandler.stringifyRequest;


/**
 *
 * @author Hp
 */
public class ClientHandler extends Thread{

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public static ArrayList<ClientHandler> connectionsList = new ArrayList<ClientHandler>();
    public RequestHandler reqHandler;
    JSONObject offResponse = new JSONObject();
    public String name;
    
    ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reqHandler = new RequestHandler(this);
            connectionsList.add(this);
            start();
        }catch(IOException ex){
            ex.printStackTrace();
        }   
    }
    
    

    public static void clearAll(){
        for(ClientHandler ch : connectionsList){
            ch.interrupt();
        }
        connectionsList.clear();
    }
    
        @Override
    public void run(){
                while(socket.isConnected()){
                try{
                    String req = bufferedReader.readLine();
                    System.out.println(req);
                    reqHandler.handleRequest(req);
                }catch(IOException ex){
                    System.out.println("A client logged out");
                    reqHandler.changeClientOffline(this.name);
                    closeAll(socket, bufferedReader, bufferedWriter);
                    break;
                }
            }
    }
    
   @Override
    public void interrupt() {
        super.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
public void sendToPlayer(String response, String playerName){
    for (int i = 0; i < connectionsList.size(); i++){
        if(connectionsList.get(i).name != null){
             if(connectionsList.get(i).name.equals(playerName)){
             try{
                    connectionsList.get(i).bufferedWriter.write(response);
                    connectionsList.get(i).bufferedWriter.newLine();
                    connectionsList.get(i).bufferedWriter.flush();
            }catch(IOException ex){
                    reqHandler.changeClientOffline(connectionsList.get(i).name);
                    offResponse.put("status", "no");
                    offResponse.put("header", "INVITATION");
                    offResponse.put("msg", "User is Offline" );
                    String res = stringifyRequest(offResponse);
                    sendResponseToClient(res);
                    System.out.println("player is absent");
                    connectionsList.remove(i);
            }
             }
    }
        }
}
    
    public void sendResponseToClient(String response){
        try{
            bufferedWriter.write(response);
            System.out.println(response);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch(IOException ex){
            ex.printStackTrace();
            reqHandler.changeClientOffline(this.name);
        }
    }
    
     public void sendToAll(String response){
         
         for (int i = 0; i < connectionsList.size(); i++){
             if(connectionsList.get(i).name != null){
             try{
                    connectionsList.get(i).bufferedWriter.write(response);
                    connectionsList.get(i).bufferedWriter.newLine();
                    connectionsList.get(i).bufferedWriter.flush();
            }catch(IOException ex){
                    reqHandler.changeClientOffline(connectionsList.get(i).name);
                    System.out.println( connectionsList.get(i).name + "player is off");
                    connectionsList.remove(i);
            }
             }
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
}
