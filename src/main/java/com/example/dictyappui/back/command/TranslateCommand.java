package com.example.dictyappui.back.command;

import com.example.dictyappui.back.algorithms.RefactorManager;
import com.example.dictyappui.back.db.DatabaseConnection;
import com.example.dictyappui.back.db.Word;
import com.example.dictyappui.back.hashing.HashTableManager;

import java.util.List;
import java.util.Scanner;

public class TranslateCommand implements Command {

    public TranslateCommand(Scanner scanner) {
        // DO NOTHING WITH THIS SCANNER
    }
    public TranslateCommand() {
        // DO NOTHING
    }

    @Override
    public void execute(DatabaseConnection dbConn) {
        final Scanner scanner = new Scanner(System.in);

        System.out.print("Enter word that should be translated: ");
        String wordToTranslate = scanner.nextLine().toLowerCase();

        HashTableManager hashTableManager = new HashTableManager(dbConn);
//        hashTableManager.fillHashTable();
        Word foundWord = hashTableManager.getWordFromHashTable(wordToTranslate);

        if (foundWord == null) {
            System.out.print("Could not find exactly this word :(\nDo mean these words: ");
            RefactorManager refactorManager = new RefactorManager(dbConn);
            List<String> mostSimilarWords = refactorManager.findMostSimilar(wordToTranslate);
            for (String similarWord : mostSimilarWords) {
                System.out.print(similarWord + " ");
            }
            System.out.println(System.lineSeparator());
            execute(dbConn);
        } else {
            System.out.println(foundWord);
        }
    }

    public String gui(DatabaseConnection dbConn, String wordToTranslate) {
        StringBuilder b = new StringBuilder();

        HashTableManager hashTableManager = new HashTableManager(dbConn);
//        hashTableManager.fillHashTable();
        Word foundWord = hashTableManager.getWordFromHashTable(wordToTranslate);

        if (foundWord == null) {
            b.append("Could not find exactly this word :(\nDo mean these words: ");
            RefactorManager refactorManager = new RefactorManager(dbConn);
            List<String> mostSimilarWords = refactorManager.findMostSimilar(wordToTranslate);
            for (String similarWord : mostSimilarWords) {
                b.append(similarWord).append(" ");
            }
            b.append(System.lineSeparator());
            return b.toString();
//            gui(dbConn, wordToTranslate);
        } else {
            b.append(foundWord);
        }
        return b.toString();
    }
}
