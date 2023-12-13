package com.example.dictyappui.back.hashing.robin;

import com.example.dictyappui.back.db.DatabaseConnection;
import com.example.dictyappui.back.db.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashTableManager {
    private final HashManager hashing;
    private final DatabaseConnection dbConnection;
    private int collisionCounter = 0;
    private HashMap<Integer, Integer> collisionsByIndex = new HashMap<>();

    public static <K, V> void printMap(Map<K, V> map, String mapName) {
        System.out.println("Printing " + mapName + ":");
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();
    }

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
                if (stepCoef > 0) {
                    collisionCounter++;
                    // Update the collision count for the current index
                    collisionsByIndex.put(index, collisionsByIndex.getOrDefault(index, 0) + 1);
                }
                stepCoef++;
            } while (!dbConnection.checkIfRowIsEmptyInWordsTable(index));
            dbConnection.insertToWords(index, enWord, ukWords.get(i), sentences.get(i));
        }
        System.out.println("Collisions: " + collisionCounter);

        // Print collisions by index
        System.out.println("Collisions by Index:");
//        printCollisionsByIndex();
        countValuesByCollisions();
    }

    // Function to count values based on the number of collisions
    public void countValuesByCollisions() {
        HashMap<Integer, Integer> staticsticHashMap = new HashMap<>();
        for (Integer value: collisionsByIndex.values()) {
            if (!staticsticHashMap.containsKey(value)) {
                staticsticHashMap.put(value, 0);
            } else {
                staticsticHashMap.replace(value, staticsticHashMap.get(value)+1);
            }
        }

        printMap(staticsticHashMap, "Collisions grouped");
    }

    private void printCollisionsByIndex() {
        // Meaning list contains entry with key and value
        List<Map.Entry<Integer, Integer>> listOfEntries = new ArrayList<>(collisionsByIndex.entrySet());

        // Sort the list by collisions in descending order
        listOfEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Print the sorted list
        for (Map.Entry<Integer, Integer> entry : listOfEntries) {
            System.out.println("Index " + entry.getKey() + ": " + entry.getValue() + " collisions");
        }
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
