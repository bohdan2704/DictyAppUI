package com.example.dictyappui.back.command;

import com.example.dictyappui.back.db.DatabaseConnection;
import com.example.dictyappui.back.db.Word;

import java.util.List;
import java.util.Scanner;

public class AllWordsByLetter implements Command {

    public AllWordsByLetter(Scanner scanner) {
//        this.scanner = scanner;
    }
    public AllWordsByLetter() {
        // DEFAULT CONSTRUCTOR
    }

    @Override
    public void execute(DatabaseConnection dbConn) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Provide letter you are interested in: ");
        String stringWithOneLetter = scanner.nextLine();
        char ourLetter = stringWithOneLetter.charAt(0);

        List<Word> wordWithSameStartingLetter = dbConn.selectWordsStartingWithLetter(ourLetter);
        for (Word word : wordWithSameStartingLetter) {
            System.out.println(word.getWords());
        }
    }

    @Override
    public String gui(DatabaseConnection dbConn, String stringWithOneLetter) {
        char ourLetter = stringWithOneLetter.charAt(0);
        StringBuilder b = new StringBuilder();
        List<Word> wordWithSameStartingLetter = dbConn.selectWordsStartingWithLetter(ourLetter);
        for (Word word : wordWithSameStartingLetter) {
            b.append(word.getWords()).append(System.lineSeparator());
        }
        return b.toString();
    }
}
