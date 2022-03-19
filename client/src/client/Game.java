package client;

import client.model.User;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Game implements Initializable {

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Button button4;

    @FXML
    private Button button5;

    @FXML
    private Button button6;

    @FXML
    private Button button7;

    @FXML
    private Button button8;

    @FXML
    private Button button9;
    
    
    private Button resetButton;
    @FXML
    private Label win;
    private Text winnerText;
    //flag for play ,if =0 ->user will play , else if =1 cpu will play 
    private int playerTurn = 0;
    ArrayList<Button> buttons;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));

        buttons.forEach(button ->{
            setupButton(button);
            button.setFocusTraversable(false);
        });
    }

    @FXML
    void restartGame(ActionEvent event) {
        playerTurn = 0;
        buttons.forEach(this::resetButton);
        win.setText("");
    }
    
     @FXML
void ExitGame(ActionEvent event) throws IOException{
        User.playMode = null;
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
}
    public void resetButton(Button button){
        button.setDisable(false);
        button.setText("");
    }
    private void setupButton(Button button) {
        button.setOnMousePressed(mouseEvent -> {
            setPlayerSymbol(button);
            button.setDisable(true);
            if(!checkIfGameIsOver()){
                        setCpuSymbol();
            }
        });
    }
public void setCpuSymbol(){
        Random rand = new Random();
        int cpuPos = rand.nextInt(9);
        if( (buttons.get(cpuPos)).getText().trim().equals("")) {
            buttons.get(cpuPos).setText("O");
            buttons.get(cpuPos).setDisable(true);
           
             playerTurn = 0;
        }
        else{
        setCpuSymbol();
        }
        checkIfGameIsOver();
}
    public void setPlayerSymbol(Button button){
       
        if(playerTurn  == 0 && button.getText().trim().equals("")){
            button.setText("X");
            playerTurn = 1;
        }
    }
    
    public boolean checkIfGameIsOver(){
        //states for win
        for (int a = 0; a < 8; a++) {
            String line = null;
         switch (a) {
                case 0 :
                     line=(button1.getText() + button2.getText() + button3.getText());
                     break;
                case 1 :
                    line= button4.getText() + button5.getText() + button6.getText();
                    break;
                case 2 :
                    line= button7.getText() + button8.getText() + button9.getText();
                    break;
                case 3 :
                    line= button1.getText() + button5.getText() + button9.getText();
                    break;
                case 4 :
                   line= button3.getText() + button5.getText() + button7.getText();
                   break;
                case 5 :
                    line= button1.getText() + button4.getText() + button7.getText();
                    break;
                case 6 :
                    line= button2.getText() + button5.getText() + button8.getText();
                    break;
                case 7 :
                    line= button3.getText() + button6.getText() + button9.getText();
                    break;
                default :
                     line=null;
                     break;
                    
            }

            //X winner
            if (line.equals("XXX")) {
                win.setText("Player won!");
                endGame();
                return true;
            }

            //O winner
            if (line.equals("OOO")) {
                win.setText("Computer won!");
                endGame();
                return true;
            }
            
            if(isDraw()){
                    System.out.println("TIEEEE");
                    win.setText("TIE");
                    endGame();
                    return true;
            }
        }
        return false;
            
    }
        public void endGame(){
        for(int i=0;i<9;i++){
                    buttons.get(i).setDisable(true);
                }
        }
        
        public boolean isDraw(){
         return buttons.stream().allMatch(t -> !(t.getText().trim().equals("")));
        }

}
    
