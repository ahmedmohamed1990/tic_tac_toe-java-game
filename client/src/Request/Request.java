/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Request;

import client.ClientConnection;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import static jdk.nashorn.internal.objects.NativeArray.map;

/**
 *
 * @author Hp
 */
public abstract class Request {
    ClientConnection clientConn;
    public String header;
    public Map<String, String> data;
    public Request(String header, Map<String, String> data){
        this.header = header;
        this.data = data;
    }
    
    public void connectToServer(){
    try{
            clientConn = new ClientConnection(new Socket("localhost", 3030));
            System.out.println("Connected to server");
        }catch(IOException ex){
            System.out.println("Connection failed to the server");
            //override
            return;
        }
    }
    
    public void sendRequest() throws IOException{
     
     Task<String> getResponse;
        getResponse = new Task<String>() {
            @Override
            public String call() throws Exception {
                connectToServer();
                clientConn.sendRequestToServer(stringifyRequest());
                String res = clientConn.getResponseFromServer();
                return res;
            }
        };
     
     getResponse.setOnSucceeded((WorkerStateEvent e) -> {
        String result = getResponse.getValue();
        Map<String, String>  response = parseMap(result);
        System.out.println(result);
        doNext(response);
    });
     
    getResponse.setOnRunning(e -> {
         doBefore();
    });
     
    getResponse.setOnFailed(e -> {
         reportError();
    });
     
     new Thread(getResponse).start();
    }
    
    public String stringifyRequest(){
        String reqAsString;
        data.put("header", header);
        reqAsString = data.keySet().stream()
                .map(key -> key + "=" + data.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    return reqAsString;
} 

        
    public Map<String, String> parseMap(String req){
    Map<String, String> map = new HashMap<>();
    req = req.substring(1, req.length()-1);           //remove curly brackets
    String[] keyValuePairs = req.split(",");              //split the string to creat key-value pairs
    for(String pair : keyValuePairs)                        //iterate over the pairs
    {
        String[] entry = pair.split("=");                   //split the pairs to get key and value 
        map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
    }
    return map;
    };
    abstract public void doNext(Map<String, String> result);
    
    abstract public void doBefore();

    abstract public void reportError();
}
