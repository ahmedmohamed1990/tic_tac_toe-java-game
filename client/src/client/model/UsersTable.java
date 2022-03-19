/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.model;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class UsersTable {
    
    private SimpleStringProperty playerName;
    private SimpleIntegerProperty points;
    private SimpleStringProperty status;
    
    public UsersTable(String _playerName,String _status,int _points){
        
        playerName=new SimpleStringProperty(_playerName);
        status=new SimpleStringProperty(_status);
        points=new SimpleIntegerProperty(_points);
        
    }

    public UsersTable(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getPlayerName() {
        return playerName.get();
    }

   
    public void setPlayerName(String playerName) {
     this.playerName.set(playerName);

    }

  
    public int getPoints() {
        return points.get();
    }

 
    public void setPoints(int points) {
        this.points.set(points);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
   


    
}

