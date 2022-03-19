/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import Request.Request;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
/**
 *
 * @author Hp
 */
public class ClientViewController implements Initializable {
    /*ClientConnection clientConn;*/
    Request authRequest;
    @FXML
    public Label user_label;
    
    @FXML
    public Label password_label;
    
    @FXML
    public Button button;
    
    @FXML
    public TextField username;
    
    @FXML
    public TextField password;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

     
        
    button.setOnAction((ActionEvent event) -> {
        
    }
  );
}
}
