package com.example.dictyappui.back.hashing;

import com.example.dictyappui.back.db.DatabaseConnection;
import com.example.dictyappui.back.hashing.robin.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollisionsManager {
    private final HashManager hashing;
    public int collisionCounter = 0;
    private final HashMap<Integer, Integer> collisionsByIndex = new HashMap<>();

    public CollisionsManager(HashManager hashing) {
        this.hashing = hashing;
    }

    public void checkIfCollision(String word1, int indexToInsert, Entity[] map) {
        String wordInOccupiedCell = map[indexToInsert].getKey();
        if (hashing.googleHash(wordInOccupiedCell) == hashing.googleHash(word1)) {
//            System.out.println(word1 + " -- " + indexToInsert + "( " + hashing.googleHash(word1) + " )" + " ~~ " + wordInOccupiedCell + " -- " + hashing.googleHash(wordInOccupiedCell));
            collisionCounter++;
            // Update the collision
            collisionsByIndex.put(indexToInsert, collisionsByIndex.getOrDefault(indexToInsert, 0) + 1);
            return;
        }
    }

    public void showCollisionStat() {
        System.out.println("Collisions: " + collisionCounter);

        // Print collisions by index
//        printCollisionsByIndex();
        countValuesByCollisions();
//        printMap(collisionsByIndex, "Name");
//        System.out.println(collisionsByIndex.size());
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

    public static <K, V> void printMap(Map<K, V> map, String mapName) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();
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

}
