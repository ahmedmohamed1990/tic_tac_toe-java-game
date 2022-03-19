/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import Request.Request;
import client.model.User;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * FXML Controller class
 *
 * @author Hp
 */
public class Signin implements Initializable {
    public ClientConnection clientConn;
    public JSONObject request = new JSONObject();
    
    public Stage stage;
    public Scene scene;
    public Parent root;
    
    @FXML
    public Button signin_btn;
    
    @FXML
    public Hyperlink signup_link;
    
    @FXML
    public PasswordField password_field;
    
    @FXML
    public TextField username_field;
    
    @FXML
    public Label password_label;
    
    @FXML
    public Label username_label;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            clientConn = new ClientConnection(new Socket("localhost", 3030)){
            @Override
            public void run(){
                while(true){
                    try{
                        String response = bufferedReader.readLine();
                        if(response == null){
                        throw new IOException();
                        }
                        handleRes(response);
                    }catch(IOException ex){
                        System.out.println("Error in Listenening");
                        showServerError();
                        closeAll(socket, bufferedReader, bufferedWriter); 
                        break;
                    }
                }
                
            }
            };
        } catch (IOException ex) {
            System.out.println(ex);
        }
        clientConn.start();
    }    

    @FXML
    public void onLoginHandler(ActionEvent event) {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        String username = username_field.getText();
        String password = password_field.getText();
        password_label.setText("processing ....");
        if (username.trim().equals("")){
            username_label.setText("username is required");
            password_label.setText("");
            return;
        }
        
        else if (password.trim().equals("")){
            password_label.setText("password is required");
            username_label.setText("");
            return;
        }
        
        else if (password.trim().length()<= 8)
        {
            password_label.setText("password must be large than 8");
            username_label.setText("");
            return;
        }
        request.put("username", username);
        request.put("password", password);
        request.put("header", "LOGIN");
        String reqAsString = stringifyRequest(request);

        clientConn.sendRequestToServer(reqAsString);
        signin_btn.setDisable(true);
    }

    public void handleRes(String res){
        JSONObject jsonRes = parseRequest(res);
        if(jsonRes.get("header").equals("LOGIN")){
        System.out.println("response received");
        new Thread(new Runnable() {
                            @Override public void run() {
                            Platform.runLater(new Runnable() {
                            @Override public void run() {
                            signin_btn.setDisable(false);
                            if(jsonRes.get("status").equals("failed_password")){
                            password_label.setText("" + jsonRes.get("msg"));
                            username_label.setText("");
                            return;
                }
                
                            if(jsonRes.get("status").equals("failed_username")){
                                username_label.setText("" + jsonRes.get("msg"));
                                password_label.setText("");
                                return;
                            }
                
                //Switch to main scene with name data
                User.username = "" + jsonRes.get("username");
                User.score = "0";//temp
                                try {
                                    switchToMain();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                
                                    }
                                });
                            }
                        }).start();
        }
    }

    public void showServerError(){
    new Thread(new Runnable() {
                            @Override public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override public void run() {
                                        password_label.setText("Server is disconnected");
                                        username_label.setText("");
                                        signin_btn.setDisable(true);
                                        signup_link.setDisable(true);
                                    }
                                });
                            }
                        }).start();
    }
    
    public String stringifyRequest(JSONObject res){
        String resAsString = res.toString();
        return resAsString;
    }
    
    public JSONObject parseRequest(String req){
    Object json = JSONValue.parse(req);  
    JSONObject jsonObject = (JSONObject) json;  
    return jsonObject;
    };
    
    @FXML
    public void switchToSignup(ActionEvent event) throws IOException {
      root = FXMLLoader.load(getClass().getResource("signup.fxml"));
      stage = (Stage)((Node)event.getSource()).getScene().getWindow();
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    
    public void switchToMain() throws IOException {
      root = FXMLLoader.load(getClass().getResource("home.fxml"));
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
}
