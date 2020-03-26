package edu.saddleback.cs4b;

import edu.saddleback.cs4b.Backend.Client;
import edu.saddleback.cs4b.UI.ClientChatController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UI/ClientChat.fxml"));
        Parent root = (Parent) loader.load();
        ClientChatController ctrl = loader.getController();

        new Thread(()-> { Client client = new Client(ctrl);}).start();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
