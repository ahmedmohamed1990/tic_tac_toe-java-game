/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controllers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import server.handlers.ConnectionHandler;

/**
 * FXML Controller class
 *
 * @author Hp
 */
public class serverController implements Initializable {
    private boolean isServerOn = false;

    @FXML
    private Label OnOffLabel;
    @FXML
    private TableView<?> PlayerList;
    @FXML
    private TableColumn<?, ?> playerName;
    @FXML
    private TableColumn<?, ?> PlayerPoints;
    @FXML
    private TableColumn<?, ?> playerStatus;
    @FXML
    private ToggleButton switchBtn;
    
    private ConnectionHandler server;

    /**
     * Initializes the controller class.
     * @param url
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        PlayerPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
        playerStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }    
    
    public void onActionHandler(){
        if(isServerOn){
            OnOffLabel.setText("OFF");
            isServerOn = false;
            System.out.println("Server is down");
            server.interrupt();
            server = null;
            return;
        }
        server = new ConnectionHandler();
        server.start();
        OnOffLabel.setText("ON");
        isServerOn = true;
    }
    
    
}
