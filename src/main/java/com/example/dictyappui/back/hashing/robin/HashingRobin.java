package com.example.dictyappui.back.hashing.robin;

import com.example.dictyappui.back.db.DatabaseConnection;
import com.example.dictyappui.back.hashing.CollisionsManager;
import com.example.dictyappui.back.hashing.HashManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HashingRobin {
    private final DatabaseConnection dbConnection;
    private CollisionsManager collisionsManager;
    private CollisionsManager totalIterationsManager;
    private HashManager hashManager;
    private Entity[] map;
    private final int tableSize;

    public HashingRobin(DatabaseConnection databaseConnection, int size) {
        this.dbConnection = databaseConnection;
        this.tableSize = size;
        hashManager = new HashManager(tableSize);
        collisionsManager = new CollisionsManager(hashManager);
        totalIterationsManager = new CollisionsManager(hashManager);
        map = new Entity[tableSize];
    }

    public int insert(String key, String value, String description) {
        int index = hashManager.googleHash(key);
        Entity entity = new Entity(key, value, description);

        if (map[index] != null) {
            collisionsManager.checkIfCollision(key, index, map);
        }

        while (map[index]!=null) {
            totalIterationsManager.checkIfCollision(key, index, map);
            entity.setId(index);
            if (entity.getProbeLength() > map[index].getProbeLength()) {
                Entity temp = map[index];
                map[index] = entity;
                entity = temp;
            }
            entity.setProbeLength(entity.getProbeLength()+1);
            index = (index+1)%tableSize;
        }

        entity.setId(index);
        map[index] = entity;
        return index;
    }

    public void fill() {
            List<String> enWords = new ArrayList<>();
            List<String> ukWords = new ArrayList<>();
            List<String> sentences = new ArrayList<>();

            // Fetch all words that will be saved to the hash table
            dbConnection.selectFromWordsForHashTable(enWords, ukWords, sentences);
            for (int i = 0; i < enWords.size(); i++) {
                insert(enWords.get(i), ukWords.get(i), sentences.get(i));
            }
            collisionsManager.showCollisionStat();
            totalIterationsManager.showCollisionStat();
    }

    public Entity get(String key) {
        int index = findIndexByKey(key);

        if (index != -1) {
            return map[index];
        } else {
            return null; // Key not found
        }
    }

    private int findIndexByKey(String key) {
        int index = hashManager.googleHash(key);
        int originalIndex = index;

        while (map[index] != null) {
            if (map[index].getKey().equals(key)) {
                return index; // Key found at this index
            }

            // Move to the next slot
            index = (index + 1) % map.length;

            // Break if we have iterated through the entire array
            if (index == originalIndex) {
                break;
            }
        }

        return -1; // Key not found
    }
//
//    public Entity lookupSmartSearch(String key) {
//        int index = findIndexByKeySmartSearch(key);
//
//        if (index != -1) {
//            return map[index];
//        } else {
//            return null; // Key not found
//        }
//    }
//
//    private int findIndexByKeySmartSearch(String key) {
//        int index = hashManager.googleHash(key);
//        int originalIndex = index;
//
//        int mean = calculateMeanProbeLength();
//        int probe = 0;
//
//        while (map[index] != null) {
//            if (map[index].getKey().equals(key)) {
//                return index; // Key found at this index
//            }
//
//            // Probe up and down from the mean
//            probe = (probe % 2 == 0) ? -probe / 2 : (probe + 1) / 2;
//            index = (originalIndex + mean + probe) % map.length;
//
//            // Break if we have iterated through the entire array
//            if (index == originalIndex) {
//                break;
//            }
//        }
//
//        return -1; // Key not found
//    }
//
//    private int calculateMeanProbeLength() {
//        int totalProbeLength = 0;
//        int count = 0;
//
//        for (Entity entity : map) {
//            if (entity != null) {
//                totalProbeLength += entity.getProbeLength();
//                count++;
//            }
//        }
//
//        return (count != 0) ? totalProbeLength / count : 0;
//    }
}
