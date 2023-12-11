package com.example.dictyappui.back.redundant;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    private static final String filePath = "D:\\Java Programing\\DictionaryApp\\src\\main\\resources\\popular.txt";
    private static final int expectedWordsNumber = 26000;

    public List<String> readStringsFromFile() {
        List<String> stringList = new ArrayList<>(expectedWordsNumber);
        try {
            Scanner scanner = new Scanner(new File(filePath));
            // Check if there is a word in the file.
            while (scanner.hasNext()) {
                stringList.add(scanner.next());
            }

            // Close the scanner when done.
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return stringList;
    }

}
