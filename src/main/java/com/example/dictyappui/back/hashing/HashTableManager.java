package com.example.dictyappui.back.hashing;

import com.example.dictyappui.back.db.DatabaseConnection;
import com.example.dictyappui.back.db.Word;

import java.util.*;

public class HashTableManager {
    private final HashManager hashing;
    private final DatabaseConnection dbConnection;

    public HashTableManager(DatabaseConnection databaseConnection) {
        this.dbConnection = databaseConnection;
        this.hashing = new HashManager(dbConnection.getDbSize());
    }

    public void fillHashTable() {
        List<String> enWords = new ArrayList<>();
        List<String> ukWords = new ArrayList<>();
        List<String> sentences = new ArrayList<>();

        // Fetch all words that will be saved to the hash table
        dbConnection.selectFromWordsForHashTable(enWords, ukWords, sentences);
        for (int i = 0; i < enWords.size(); i++) {
            String enWord = enWords.get(i);
            int stepCoef = 0;
            int index;
            do {
                index = hashing.doubleHashing(enWord, stepCoef);
//                checkIfCollision(enWord);
                stepCoef++;
            } while (!dbConnection.checkIfRowIsEmptyInWordsTable(index));
            dbConnection.insertToWords(index, enWord, ukWords.get(i), sentences.get(i));
        }
//        showCollisionStat();
    }

    public Word getWordFromHashTable(String searchByThisWord) {
        int index;
        int stepCoef = 0;
        Word lastWordFromTable;
        do {
            index = hashing.doubleHashing(searchByThisWord, stepCoef);
            lastWordFromTable = dbConnection.selectFromWordsById(index);
            if (lastWordFromTable == null) {
                return null;
            }
            stepCoef++;
        }
        while (!lastWordFromTable.enWord().equals(searchByThisWord));
        return lastWordFromTable;
    }
}
