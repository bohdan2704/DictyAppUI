package com.example.dictyappui;

import com.example.dictyappui.back.command.AllWordsByLetter;
import com.example.dictyappui.back.command.Command;
import com.example.dictyappui.back.command.TranslateCommand;
import com.example.dictyappui.back.command.WordOfTheDayCommand;
import com.example.dictyappui.back.db.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerVocabulary implements Initializable {
    private static final int hashTableSize = 37501; // 30323

    @FXML
    private Text labelWithTranslation;
    @FXML
    private ComboBox<String> alphabetComboBox;
    private final DatabaseConnection databaseConnection = new DatabaseConnection(hashTableSize);

    public void switchToTranslate(ActionEvent event) throws IOException {
        databaseConnection.closeConnection();
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Translator");

        String styleCss = getClass().getResource("style.css").toExternalForm();
        Scene scene = new Scene(root);
        // ADD STRICTLY AFTER, SO NO GLITCH WILL BE WHEN LOADING STYLES
        scene.getStylesheets().add(styleCss);

        stage.setScene(scene);
        stage.show();
    }

    // INITIALIZING THE CONTENT OF CHECK BOX
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        String[] choices = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ");
        for (int i = 0; i < choices.length; i++) {
            choices[i] = "Letter " + choices[i];
        }
        alphabetComboBox.getItems().addAll(choices);
        alphabetComboBox.setOnAction(this::itemOfChoiceBoxWasSelected);

    }

    private void itemOfChoiceBoxWasSelected(ActionEvent event) {
        final int indexOfLetterInString = 1;
        String chosenLetter = alphabetComboBox.getValue().split(" ")[indexOfLetterInString];
        Command allWordsByLetter = new AllWordsByLetter();
        String listOfWordsWithShortTranslation = allWordsByLetter.gui(databaseConnection, chosenLetter);
        labelWithTranslation.setText(listOfWordsWithShortTranslation);
    }
}
