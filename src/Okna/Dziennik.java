/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Okna;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Dziennik extends Application {

       @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Logowanie.fxml"));  
        //Parent root = FXMLLoader.load(getClass().getResource("Dyrektor.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("NauczycielKlasy.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("Rodzic.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
