package com.example.dictyappui.back;

import com.example.dictyappui.back.command.AllWordsByLetter;
import com.example.dictyappui.back.command.Command;
import com.example.dictyappui.back.command.TranslateCommand;
import com.example.dictyappui.back.command.WordOfTheDayCommand;
import com.example.dictyappui.back.db.DatabaseConnection;
import com.example.dictyappui.back.hashing.HashTableManager;
import com.example.dictyappui.back.hashing.robin.HashingDouble;
import com.example.dictyappui.back.hashing.robin.HashingRobin;
import com.example.dictyappui.back.redundant.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    public static final int hashTableSize = 37501; //  37501 -- 50%   30323 -- 65%   75% -- 25013 85% -- 22307 95% -- 20011
    private static final int N = 5_000;
    private Command command;
    private final DatabaseConnection dbConn = new DatabaseConnection(hashTableSize);

    public void start() {
        HashTableManager hashTableManager = new HashTableManager(dbConn);
        hashTableManager.fillHashTable();

        List<String> enWords = new ArrayList<>();
        // Fetch all words that will be saved to the hash table
        dbConn.selectFromWordsForHashTable(enWords, null, null);

//        Timer timer = new Timer();
//        timer.startTimer();
//        HashTableManager hashTableManager = new HashTableManager(dbConn);
//        hashTableManager.fillHashTable();
//        timer.stopTimer();
//

        HashingRobin hashingRobin = new HashingRobin(dbConn, hashTableSize);
        hashingRobin.fill();
//        Timer timer1 = new Timer();
//        timer1.startTimer();
//        for (int i = 0; i < N; i++) {
//            for (String enWord : enWords) {
////            System.out.println(hashTableManager.get(enWord));
//                hashingRobin.get(enWord);
//            }
//        }

//        timer1.stopTimer();

//        List<String> list = new ArrayList<>();
//        dbConn.selectFromWordsForHashTable(list, null, null);
//        for (String w : list) {
//            if (!hashingRobin.get(w).getKey().equals(w)) {
//                System.out.println("Bad !!!");
//            }
//        }


        HashingDouble hashingDouble = new HashingDouble(dbConn, hashTableSize);
        hashingDouble.fill();
//        Timer timer2 = new Timer();
//        timer2.startTimer();
//        for (int i = 0; i < N; i++) {
//            for (String enWord : enWords) {
////            System.out.println(hashTableManager.getWordFromHashTable(enWord));
//                hashingDouble.get(enWord);
//            }
//        }
//        timer2.stopTimer();
//        List<String>
//        list = new ArrayList<>();
//        dbConn.selectFromWordsForHashTable(list, null, null);
//
//        for (String w : list) {
//            if (!hashingDouble.get(w).getKey().equals(w)) {
//                System.out.println("Bad !!!");
//            }
//        }

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
