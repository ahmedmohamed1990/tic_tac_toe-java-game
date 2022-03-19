/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.handlers;

import DBManager.DBConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Hp
 */
public class RequestHandler {
    public static  JSONArray allPlayers;
    DBConnection DB;
    JSONObject response = new JSONObject();
    JSONObject request = new JSONObject();
    ClientHandler ch;
    
    RequestHandler(ClientHandler ch){
    DB = new DBConnection();
    this.ch = ch;
    }
    
    public void handleRequest(String req){  
    request = parseRequest(req);
    routeRequest(request);
    };
    
    public void routeRequest(JSONObject req){
        String header = (String)req.get("header");
        if(header.equals("SIGNUP")){
            signUpUser(req);
            return;
        }
        if(header.equals("LOGIN")){
            signInUser(req);
            return;
        }
        if(header.equals("INVITATION")){
            sendInvitaion(req);
            return;
        }
        if(header.equals("UPDATE_ALL")){
            updateAllUsers(req);
            return;
        }
        if(header.equals("REFUSE_INVITATION")){
            refuseInvitaion(req);
            return;
        }
        if(header.equals("SET_STATE")){
            changeUserState(req);
            return;
        }
        if(header.equals("CANCEL_INVITATION")){
            cancelInvitaion(req);
            return;
        }
        cannotFindRoute();
    };
    
    public static String stringifyRequest(JSONObject res){
        String resAsString = res.toString();
        return resAsString;
    }
    
    public static JSONObject parseRequest(String req){
    Object json = JSONValue.parse(req);  
    JSONObject jsonObject = (JSONObject) json;  
    return jsonObject;
    };
    
    
    //HEADER signup
    private void signUpUser(JSONObject req) {
        //Auth logic
        response = DB.signUp(req);
        response.put("header", "SIGNUP");
        String res = stringifyRequest(response);
        ch.sendResponseToClient(res);
    }
    
    
    //Header signin
    private void signInUser(JSONObject req) {
        //Auth logic
        response = DB.login(req);
        response.put("header", "LOGIN");
        String res = stringifyRequest(response);
        ch.sendResponseToClient(res);
    }
    
    public void changeClientOffline(String name){
        DB.setStatus(name, "Offline");
        System.out.println(name + " Became" + " as a Offline");
    }
    
    //Invitaion
    private void sendInvitaion(JSONObject req) {
        //Auth logic
        String username = (String) req.get("username");
        String competitorName = (String) req.get("competitor");
        response.put("header", "INVITATION");
        if(!(DB.isUserNameFound(competitorName))){
            response.put("status", "no");
            response.put("msg", "User not found");
            String res = stringifyRequest(response);
            ch.sendResponseToClient(res);
            return;
        }else{
            if(DB.getStatus(competitorName).equals("Online")){
            response.put("status", "ok");
            response.put("sender", username);
            
        }else{
            response.put("status", "no");
            response.put("msg", "User is " + DB.getStatus(competitorName));
            String res = stringifyRequest(response);
            ch.sendResponseToClient(res);
            return;
        }
        }
        
        String res = stringifyRequest(response);
        ch.sendToPlayer(res, competitorName);
    }

    private void cannotFindRoute() {
        response.put("header", "NOT_FOUND");
        response.put("status", "NOT_FOUND");
        String res = stringifyRequest(response);
        ch.sendResponseToClient(res);
    }

    private void updateAllUsers(JSONObject req) {
       allPlayers = DB.getAllPlayers();
        if(allPlayers != null){  
        ch.name = "" + req.get("username");
        response.put("users", allPlayers);
        response.put("header", "UPDATE_ALL");
        String res = stringifyRequest(response);
        System.out.println("Send List To All");
        ch.sendToAll(res);
      }else System.out.println("in UpdateAllUsers");
    }

    private void refuseInvitaion(JSONObject req) {
        String username = (String) req.get("username");
        String competitorName = (String) req.get("competitor");
        String msg = (String) req.get("msg");
System.out.println(username + msg);
        response.put("header", "REFUSE_INVITATION");
        if(msg.equals("busy")){
        response.put("msg", username + " has already an invitation");
        System.out.println(username + " has already an invitation");
        }else{
        response.put("msg", username + " refused the invitation.");
        }
        String res = stringifyRequest(response);
        ch.sendToPlayer(res, competitorName);
    }

    private void changeUserState(JSONObject req) {
        String username = (String) req.get("username");
        String state = (String) req.get("state");
        DB.setStatus(username, state);
        System.out.println(username + " Became" + " as a " + state);
    }

    private void cancelInvitaion(JSONObject req) {
        String competitorName = (String) req.get("competitor");
        response.put("header", "CANCEL_INVITATION");
        String res = stringifyRequest(response);
        ch.sendToPlayer(res, competitorName);
    }
        
}
