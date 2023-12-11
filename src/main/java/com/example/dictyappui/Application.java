package com.example.dictyappui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        String styleCss = getClass().getResource("style.css").toExternalForm();

        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        stage.setTitle("Translator ");
        stage.setResizable(false);
        stage.setScene(scene);
        scene.getStylesheets().add(styleCss);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}