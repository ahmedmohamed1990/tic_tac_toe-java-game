/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.clienthandlers;

import client.model.User;
import java.io.IOException;
import javafx.application.Platform;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Hp
 */
public class ResponseHandler {
//        public void handleRes(String res){
//        JSONObject jsonRes = parseRequest(res);
//        
//        if(jsonRes.get("header").equals("LOGIN")){
//        System.out.println("response received");
//        new Thread(new Runnable() {
//                            @Override public void run() {
//                            Platform.runLater(new Runnable() {
//                            @Override public void run() {
//                            signin_btn.setDisable(false);
//                            if(jsonRes.get("status").equals("failed_password")){
//                            password_label.setText("" + jsonRes.get("msg"));
//                            username_label.setText("");
//                            return;
//                }
//                
//                            if(jsonRes.get("status").equals("failed_username")){
//                                username_label.setText("" + jsonRes.get("msg"));
//                                password_label.setText("");
//                                return;
//                            }
//                
//                //Switch to main scene with name data
//                User.username = "" + jsonRes.get("username");
//                User.score = "0";//temp
//                                try {
//                                    switchToMain();
//                                } catch (IOException ex) {
//                                    System.out.println("Error in scene");
//                                }
//                
//                                    }
//                                });
//                            }
//                        }).start();
//        }
//    }
//        
//    public String stringifyRequest(JSONObject res){
//    String resAsString = res.toString();
//    return resAsString;
//    }
//    
//    public JSONObject parseRequest(String req){
//    Object json = JSONValue.parse(req);  
//    JSONObject jsonObject = (JSONObject) json;  
//    return jsonObject;
//    };
}
