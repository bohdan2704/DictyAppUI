package com.example.dictyappui.back.hashing.robin;

import com.example.dictyappui.back.db.DatabaseConnection;
import com.example.dictyappui.back.hashing.CollisionsManager;
import com.example.dictyappui.back.hashing.HashManager;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashingDouble {
    private CollisionsManager collisionsManager;
    private DatabaseConnection databaseConnection;
    private CollisionsManager totalIterationsManager;
    private HashManager hashManager;
    private Entity[] map;
    private final int tableSize;


    public HashingDouble(DatabaseConnection databaseConnection, int size) {
        this.tableSize = size;
        this.databaseConnection = databaseConnection;
        map = new Entity[tableSize];
        hashManager = new HashManager(tableSize);
        collisionsManager = new CollisionsManager(hashManager);
        totalIterationsManager = new CollisionsManager(hashManager);
    }

    public int insert(String key, String value, String description) {
        int index = doubleHashing(key, 0);
        Entity entity = new Entity(key, value, description);
        int step = 0;

        if (map[index] != null) {
            collisionsManager.checkIfCollision(key, index, map);
        }

        while (map[index] != null) {
            totalIterationsManager.checkIfCollision(key, index, map);
            index = doubleHashing(key, step);
            step++;
        }
        entity.setId(index);
        map[index] = entity;
        return index;
    }

    public int secondaryHashingNN1(int intHash) {
//        if (1 + intHash % (tableSize - 1) < 0) {
//            System.out.println("Overflow " + intHash);
//        }
        return 1 + intHash % (tableSize - 1);
//        return 1;
    }

    public int doubleHashing(String word, int step) {
        int primaryHashingFunctionRes = hashManager.googleHash(word);
        int secondaryHashingFunctionRes = secondaryHashingNN1(primaryHashingFunctionRes);
        // Double hashing formula
        return (primaryHashingFunctionRes + step * secondaryHashingFunctionRes) % tableSize;
    }

    public void fill() {
        List<String> enWords = new ArrayList<>();
        List<String> ukWords = new ArrayList<>();
        List<String> sentences = new ArrayList<>();
        int indx;
        // Fetch all words that will be saved to the hash table
        databaseConnection.selectFromWordsForHashTable(enWords, ukWords, sentences);
        for (int i = 0; i < enWords.size(); i++) {
            indx = insert(enWords.get(i), ukWords.get(i), sentences.get(i));
        }

        collisionsManager.showCollisionStat();
        totalIterationsManager.showCollisionStat();
    }

    public Entity get(String searchByThisKey) {
        int index;
        int stepCoef = 0;
        Entity currentEntity;
        do {
            index = doubleHashing(searchByThisKey, stepCoef);
            currentEntity = map[index];
            if (currentEntity == null) {
                return null;
            }
            stepCoef++;
        }
        while (!currentEntity.getKey().equals(searchByThisKey));
        return currentEntity;
    }
}
