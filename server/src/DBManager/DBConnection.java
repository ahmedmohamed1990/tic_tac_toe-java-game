/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBManager;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.lang.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class DBConnection {
    Connection con;
//   public static Player player;
    ResultSet rs;
    PreparedStatement pst;
    String dbUser="root";
    String dbPassword="m.zohery1998@gmail.com";
    String dbName="tictac";

   public DBConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/"+dbName,
                    dbUser,dbPassword);
            System.out.println("Connrcted to db");
        }catch(Exception ex){
            System.out.println("Error in Connection....");
            ex.printStackTrace();
            return;
        }
    }

    public Boolean isUserNameFound(String username) {
        try {
            pst = con.prepareStatement("select UserName from users where UserName = ?");
            pst.setString(1, username);
            rs= pst.executeQuery();
            return rs.first();

        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return false;
    }

    public JSONObject signUp ( JSONObject userData) {
        String userName = (String)userData.get("username");
        String password = (String)userData.get("password");
        JSONObject responseData = new JSONObject();

        Boolean isFound = isUserNameFound(userName);

        System.out.println(isUserNameFound(userName));
        if (!(isFound)) {
            try {
                pst=con.prepareStatement("insert into  users (Points,Games,UserName,Password,PlayerName) values(?,?,?,?,?)");
                pst.setInt(1, 0);
                pst.setInt(2, 0);
                pst.setString(3,userName);
                pst.setString(4, password);
                pst.setString(5,userName);

               if(pst.executeUpdate() != 0) {
               responseData.put("status","created");
               responseData.put("username", userName);
               //Adding Score to response
               }

               else {
                   responseData.put("status","failed");
                   responseData.put("msg","Error in database");
               };
               
               

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(" Create New one");

        } else {
            responseData.put("status","failed");
            responseData.put("msg","User "  + userName + " is already registered");
        }
        return responseData;
    }

    public JSONObject login (JSONObject userData){
        JSONObject responseData = new JSONObject();
        String userName = (String)userData.get("username");
        String password = (String)userData.get("password");
        Boolean isFound = isUserNameFound(userName);
        
        if(!isFound){
            responseData.put("status","failed_username");
            responseData.put("msg", "User is not registered");
            return responseData;
        }
        

        try {
            pst= con.prepareStatement("select UserName,Password from users where UserName= ? and Password = ? ");
            pst.setString(1,userName);
            pst.setString(2,password);
            rs= pst.executeQuery();
            System.out.println( rs.first());

            if(rs.first()){
                if(!(getStatus(userName).equals("Offline"))){
                    responseData.put("status","failed_password");
                    responseData.put("msg", "user is already in");
                }else{
                    responseData.put("status","success");
                    responseData.put("username", userName);
                }
//                player=new Player();
//                player.setStatus(Player.Status.Online);
            }else{
                responseData.put("status","failed_password");
                responseData.put("msg", "Wrong password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return responseData;
    }

    
    public String getStatus(String userName){
        try {
            pst=con.prepareStatement("select Status from users where UserName = ?");
            pst.setString(1, userName);
            rs=pst.executeQuery();
            if(rs.first()){
                return rs.getString("Status");
            }
        } catch (SQLException ex) {
            System.out.println("Exception in Get Status");

        }
        return null;
    }

    public void setStatus(String userName , String status){
        try {
            pst=con.prepareStatement("update users set Status = ? where UserName = ? ");
            pst.setString(1, status);
            pst.setString(2,userName);
           int updateRows=pst.executeUpdate();
            if(updateRows != 0){
                System.out.println("You update success");
            }else System.out.println("You update Failed");

        } catch (SQLException ex) {
            System.out.println("Exception in Get Status");

        }
    }
    public JSONArray getAllPlayers(){
        JSONObject obj =new JSONObject();
        JSONArray list=new JSONArray();
        try {
            pst =con.prepareStatement("select UserName ,Points,Status from users");
            rs=pst.executeQuery();
            while(rs.next()){
                obj.put("PlayerName",rs.getString(1));
                obj.put("Points",rs.getInt(2));
                obj.put("Status", rs.getString(3));
                list.add(obj);
                obj=new JSONObject();
            }
            
           System.out.println(list + "hereee");

            return list;
        } catch (SQLException ex) {
            System.out.println("In getAllPlayers ");
        }
        return null;
    }

    }







