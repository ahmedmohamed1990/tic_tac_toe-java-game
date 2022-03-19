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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * FXML Controller class
 *
 * @author Hp
 */
public class signup implements Initializable {
    public ClientConnection clientConn;
    private JSONObject request = new JSONObject();
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button signup_btn;
    @FXML
    private PasswordField password_field;
    @FXML
    private Label username_label;
    @FXML
    private Label password_label;
    @FXML
    private TextField username_field;
    @FXML
    private Hyperlink signin;
    @FXML
    private Label alerT_BTN;

    public void initialize(URL url, ResourceBundle rb) {
        try {
            clientConn = new ClientConnection(new Socket("localhost", 3030)){
            @Override
            public void run(){
                while(true){
                    try{
                        String response = bufferedReader.readLine();
                        System.out.println(response);
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
    public void onSignupHandler(ActionEvent event) {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        String username = username_field.getText().trim();
        String password = password_field.getText().trim();
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
        request.put("header", "SIGNUP");
        String reqAsString = stringifyRequest(request);

        clientConn.sendRequestToServer(reqAsString);
        signup_btn.setDisable(true);
    }

    public void handleRes(String res){
        JSONObject jsonRes = parseRequest(res);
        if(jsonRes.get("header").equals("SIGNUP")){
        System.out.println("response received");
        new Thread(new Runnable() {
                            @Override public void run() {
                            Platform.runLater(new Runnable() {
                            @Override public void run() {
                            signup_btn.setDisable(false);
                            if(jsonRes.get("status").equals("failed")){
                    password_label.setText("" + jsonRes.get("msg"));
                    username_label.setText("");
                    return;
                }
                User.username = "" + jsonRes.get("username");
                User.score = "0";
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
                                        signup_btn.setDisable(true);
                                        signin.setDisable(true);
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
    public void switchToSignin(ActionEvent event) throws IOException {
      root = FXMLLoader.load(getClass().getResource("signin.fxml"));
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
