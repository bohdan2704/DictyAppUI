package com.example.dictyappui.back;

import com.example.dictyappui.back.command.AllWordsByLetter;
import com.example.dictyappui.back.command.Command;
import com.example.dictyappui.back.command.TranslateCommand;
import com.example.dictyappui.back.command.WordOfTheDayCommand;
import com.example.dictyappui.back.db.DatabaseConnection;

import java.util.Scanner;

public class Menu {
    private static final int hashTableSize = 37501; // 30323
    private Command command;
    private final DatabaseConnection dbConn = new DatabaseConnection(hashTableSize);

    public void start() {
        System.out.println("List of commands:\n -- /tran\n -- /day\n -- /letter\n -- /exit");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Option: ");
            String option = scanner.nextLine();
            switch (option) {
                case "/tran":
                    command = new TranslateCommand(scanner);
                    break;
                case "/day":
                    command = new WordOfTheDayCommand();
                    break;
                case "/letter":
                    command = new AllWordsByLetter(scanner);
                    break;
                case "/exit":
                    System.out.println("Finishing the program");
                    dbConn.closeConnection();
                    return;
                default:
                    System.out.println("Not recognized command");
            }
            if (command != null) {
                command.execute(dbConn);
                command = null;
            }
        }
    }

}
