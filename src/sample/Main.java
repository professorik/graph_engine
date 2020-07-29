/*
 * Copyright (c) professorik  2019.
 * All rights reserved :)
 */

package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Scene scene = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("libaryInterface/sample.fxml")));
        Scene scene = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("libaryInterface/sample.fxml")));
        primaryStage.setTitle("Graphic Graph Editor (GGE)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
