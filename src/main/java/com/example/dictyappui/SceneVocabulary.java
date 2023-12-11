//package com.example.dictyappui;
//
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class SceneVocabulary extends Application {
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(SceneVocabulary.class.getResource("vocabulary-view.fxml"));
//        String styleCss = getClass().getResource("style.css").toExternalForm();
//        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
//        stage.setTitle("Vocabulary!");
//        stage.setResizable(false);
//        stage.setScene(scene);
//        scene.getStylesheets().add(styleCss);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}