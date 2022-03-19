/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Hp
 */
public class InvitaionController implements Initializable {

    @FXML
    private Label sender_name;
    
    @FXML
    private Button refuse_btn;
        
    @FXML
    private Button accept_name;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onRefuseInvitation(ActionEvent event) {
    }

    @FXML
    private void onAcceptInvitation(ActionEvent event) {
    }
    
}
