package com.example.dictyappui.back.algorithms;

import com.example.dictyappui.back.db.DatabaseConnection;

import java.util.ArrayList;
import java.util.List;

public class RefactorManager {
    private final List<String> words = new ArrayList<>();

    public RefactorManager(DatabaseConnection dbConn) {
        dbConn.selectFromWordsForHashTable(words, null, null);
    }

    public List<Integer> findMinValues(List<Integer> list) {
        // Check if the list is empty
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Input list is empty");
        }

        // Find the minimum value
        int minValue = Integer.MAX_VALUE;
        for (int value : list) {
            if (value < minValue) {
                minValue = value;
            }
        }

        // Collect all indices with the minimum value
        List<Integer> minIndices = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == minValue) {
                minIndices.add(i);
            }
        }

        return minIndices;
    }

    public List<String> findMostSimilar(String term) {
        if (term == null) {
            return null;
        }
        List<Integer> distances = new ArrayList<>(words.size());
        // It is bad to create object over and over in such a continuous loop
        for (String word : words) {
            distances.add(calculateLevenshteinDistance(word, term));
        }

        List<Integer> closestWordsIndexes = findMinValues(distances);
        List<String> closeWordsToProvided = new ArrayList<>();

        for (Integer index : closestWordsIndexes) {
            closeWordsToProvided.add(words.get(index));
        }
        return closeWordsToProvided;
    }

    public int calculateLevenshteinDistance(String termA, String termB) {
        int rows = termA.length() + 1;
        int columns = termB.length() + 1;
        int[][] matrix = new int[rows][columns];

        // Fill the first row and column
        for (int i = 0; i < columns; i++) {
            matrix[0][i] = i;
        }
        for (int i = 0; i < rows; i++) {
            matrix[i][0] = i;
        }

        // Fill the rest with the matrix
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < columns; j++) {
                int val = Math.min(Math.min(matrix[i - 1][j - 1], matrix[i][j - 1]), matrix[i - 1][j]);
                if (termA.charAt(i - 1) == termB.charAt(j - 1)) {
                    matrix[i][j] = val;
                } else {
                    matrix[i][j] = val + 1;
                }
            }
        }

        return matrix[rows - 1][columns - 1];
    }
}
