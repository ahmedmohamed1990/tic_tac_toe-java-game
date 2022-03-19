/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.model.User;
import client.model.UsersTable;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author OS
 */
public class HomeController implements Initializable {
    ObservableList<UsersTable> observableList = FXCollections.observableArrayList();
    public ClientConnection clientConn;
    private String response;
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    
    @FXML
    private TableView<UsersTable> tableView;

    @FXML
    private TableColumn<UsersTable, String> playerNameCol;

    @FXML
    private TableColumn<UsersTable, String> statusCol;

    @FXML
    private TableColumn<UsersTable, Integer> pointsCol;

    @FXML
    private Button InviteButton;
    @FXML
    private Button VsCompButton;
    @FXML
    private Label ChoosePlayers;
     @FXML
    private Label userSpace;
    @FXML
    private Label scoreSpace;
    @FXML
    private Label notfiSpace;
    @FXML
    private Button delBtn;
    @FXML
    private Button acceptBtn;
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            initiateGUIData();
        try {
            clientConn = new ClientConnection(new Socket("localhost", 3030)){
            @Override
            public void run(){
                while(true){
                    try{
                        System.out.println("Listening to response");
                        response = bufferedReader.readLine();
                        System.out.println(response);
                        handleRes(response);
                    }catch(IOException ex){
                        System.out.println("Error in Listenening");
                        ex.printStackTrace();
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
        System.out.println(" Listenening");
        
                updateStatus("Online");

                
        // to update all players data
        JSONObject jsonReqAll = new JSONObject();
        jsonReqAll.put("header", "UPDATE_ALL");
        jsonReqAll.put("username", User.username);
        String strRes = stringifyRequest(jsonReqAll);
        clientConn.sendRequestToServer(strRes);
        

        
        playerNameCol.setCellValueFactory(new PropertyValueFactory<UsersTable, String>("playerName"));
        statusCol.setCellValueFactory(new PropertyValueFactory<UsersTable, String>("status"));
        pointsCol.setCellValueFactory(new PropertyValueFactory<UsersTable, Integer>("points"));
        tableView.setItems(observableList);
    }    
    
    public void refuseInvitaion(String reason, String client){
        JSONObject jsonReq = new JSONObject();
        jsonReq.put("header", "REFUSE_INVITATION");
        jsonReq.put("competitor", client);
        jsonReq.put("username", User.username);
        jsonReq.put("msg", reason);
        String strRes = stringifyRequest(jsonReq);
        clientConn.sendRequestToServer(strRes);
        if(reason.equals("busy")){
            return;
        }
        User.hasInvitation = false;
        clientConn.sendRequestToServer(strRes);
        InviteButton.setDisable(false);
        VsCompButton.setDisable(false);
        delBtn.setVisible(false);
        acceptBtn.setVisible(false);
        notfiSpace.setText("No Invitaions");
        User.competitor = null;
    }
    
    @SuppressWarnings("empty-statement")
    public void handleRes(String res){
        JSONObject jsonRes = parseRequest(res);
        String header = "" + jsonRes.get("header");
        if(header.equals("INVITATION")){
        System.out.println("Invitation received");
        new Thread(() -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(jsonRes.get("status").equals("no")){
                        notfiSpace.setText("" + jsonRes.get("msg"));
                        delBtn.setVisible(false);
                        acceptBtn.setVisible(false);
                        InviteButton.setDisable(false);
                        VsCompButton.setDisable(false);
                    }else{
                        if(User.competitor != null){
                            refuseInvitaion("busy", "" + jsonRes.get("sender"));
                            System.out.println("refused with busy");
                        return;
                        }
                        User.hasInvitation = true;
                        User.competitor = (String)jsonRes.get("sender");
                        notfiSpace.setText(jsonRes.get("sender") + "invites you to a game");
                        delBtn.setVisible(true);
                        acceptBtn.setVisible(true);
                        InviteButton.setDisable(true);
                        VsCompButton.setDisable(true);
                    }
                }
            });
        }).start();
        }
        
                if (header.equals("UPDATE_ALL")) {
            System.out.println("get update All");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //ChoosePlayers.setText("" + jsonRes.get("users"));
                            tableView.getItems().clear();
                            JSONArray array = (JSONArray) jsonRes.get("users");
                            JSONObject res;
                             System.out.println(array + "hereee");

                            String pname = "";
                            String status = "";
                            int points = 0;
                            
                            int index=0;

                            Iterator<JSONObject> iterator = array.iterator();
                            while (iterator.hasNext()) {
                                res = iterator.next();
                                pname = res.get("PlayerName").toString();
                                status = res.get("Status").toString();
                                 points=Integer.parseInt(res.get("Points").toString());
                                 System.out.println(pname);                                 System.out.println(pname.equals(User.username));

                               if(!(pname.equals(User.username)))
                               observableList.add(new UsersTable(pname, status, points));

                            }  
                             System.out.println(observableList.size());
                                
                        }

                    });
                }
            }).start();
        }
        
        if(header.equals("REFUSE_INVITATION")){
        System.out.println("refuse received");
        new Thread(new Runnable() {
                            @Override public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override public void run() {
                                                InviteButton.setDisable(false);
                                                VsCompButton.setDisable(false);
                                                delBtn.setVisible(false);
                                                acceptBtn.setVisible(false);
                                                User.competitor = null;
                                                notfiSpace.setText("" + jsonRes.get("msg"));
                                    }
                                });
                            }
                        }).start();
        }
        
        if(header.equals("CANCEL_INVITATION")){
        System.out.println("cancel received");
        new Thread(new Runnable() {
                            @Override public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override public void run() {
                                                InviteButton.setDisable(false);
                                                VsCompButton.setDisable(false);
                                                delBtn.setVisible(false);
                                                acceptBtn.setVisible(false);
                                                User.competitor = null;
                                                User.hasInvitation = false;
                                                notfiSpace.setText("No Invitations");
                                    }
                                });
                            }
                        }).start();
        }
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
    
    public void initiateGUIData(){
        GUI gui = new GUI() {
            @Override
            void updateGUI() {
                userSpace.setText(User.username);
                scoreSpace.setText(User.score);
                delBtn.setVisible(false);
                acceptBtn.setVisible(false);
               
            }
        };
        gui.process();
    }
    
    public void switchToWaitPage() throws IOException{
      root = FXMLLoader.load(getClass().getResource("invitationWait.fxml"));
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    
    public void cancelInvitation(){
        InviteButton.setDisable(false);
        VsCompButton.setDisable(false);
        delBtn.setVisible(false);
        acceptBtn.setVisible(false);
        User.competitor = null;
        notfiSpace.setText("Invitaion canecelled");
        JSONObject jsonReq = new JSONObject();
        jsonReq.put("header", "CANCEL_INVITATION");
        jsonReq.put("competitor", User.competitor);
        jsonReq.put("username", User.username);
        String strRes = stringifyRequest(jsonReq);
        clientConn.sendRequestToServer(strRes);
    }
    
    public void switchToInvPage() throws IOException{
      root = FXMLLoader.load(getClass().getResource("invitation.fxml"));
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }

    @FXML
    private void onDeleteHandler(ActionEvent event) {
        if(User.hasInvitation){
            refuseInvitaion("none", User.competitor);
            return;
        }
        cancelInvitation();
    }

    @FXML
    private void onAcceptHandler(ActionEvent event) {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        //Switch to game scene
    }

    @FXML
    private void inviteBtn(ActionEvent event) {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        UsersTable competitor = tableView.getSelectionModel().getSelectedItem();
        if(competitor == null){
            notfiSpace.setText("Select user first");
            return;
        }
        
        InviteButton.setDisable(true);
        VsCompButton.setDisable(true);
        delBtn.setVisible(true);
//        acceptBtn.setVisible(false);
        //temp
        User.competitor = competitor.getPlayerName();
        User.hasInvitation = true;
        System.out.println(User.competitor);
        notfiSpace.setText("Waiting for " + User.competitor  + "to accept...");
        System.out.println("sending invitation");
        JSONObject jsonReq = new JSONObject();
        jsonReq.put("header", "INVITATION");
        jsonReq.put("username", User.username);
        jsonReq.put("competitor", User.competitor);
        String strRes = stringifyRequest(jsonReq);
        clientConn.sendRequestToServer(strRes);
    }

    @FXML
    private void playVsComputer(ActionEvent event) throws IOException {
        updateStatus("In game");
                User.playMode = "single";

        Stage myStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("game.fxml"));
        scene = new Scene(root);
        myStage.setScene(scene);
        myStage.show();
    }
    
    public void updateStatus(String state){
        JSONObject jsonReqState = new JSONObject();
        jsonReqState.put("header", "SET_STATE");
        jsonReqState.put("username", User.username);
        jsonReqState.put("state", state);
        String strRes = stringifyRequest(jsonReqState);
        clientConn.sendRequestToServer(strRes);
    }
    
}


abstract class GUI{
    public void process(){
    new Thread(new Runnable() {
                            @Override public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override public void run() {
                                        updateGUI();
                                    }
                                });
                            }
                        }).start();
    }
    
    abstract void updateGUI();
}