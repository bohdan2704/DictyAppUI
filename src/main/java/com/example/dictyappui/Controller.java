package com.example.dictyappui;

import com.example.dictyappui.back.command.Command;
import com.example.dictyappui.back.command.TranslateCommand;
import com.example.dictyappui.back.command.WordOfTheDayCommand;
import com.example.dictyappui.back.db.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    private static final int hashTableSize = 37501; // 30323

    @FXML
    private Text labelWithTranslation;
    @FXML
    private TextField textFieldWithWord;
    @FXML
    private Button btnTranslate;
    @FXML
    private Button btnGetWordOfTheDay;
    private final DatabaseConnection databaseConnection = new DatabaseConnection(hashTableSize);

    public void translate() {
        String wordToTranslate = textFieldWithWord.getText();
        Command command = new TranslateCommand();
        String resultOfTranslatingTheWord = command.gui(databaseConnection, wordToTranslate);
        labelWithTranslation.setText(resultOfTranslatingTheWord);
    }

    public void getWordOfTheDay() {
        Command command = new WordOfTheDayCommand();
        String wordOfTheDay = command.gui(databaseConnection, null);
        labelWithTranslation.setText(wordOfTheDay);
    }

    public void switchToVocabulary(ActionEvent event) throws IOException {
        databaseConnection.closeConnection();
        Parent root = FXMLLoader.load(getClass().getResource("vocabulary-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Vocabulary");

        String styleCss = getClass().getResource("style.css").toExternalForm();
        Scene scene = new Scene(root);
        // ADD STRICTLY AFTER, SO NO GLITCH WILL BE WHEN LOADING STYLES
        scene.getStylesheets().add(styleCss);

        stage.setScene(scene);
        stage.show();
    }
}
