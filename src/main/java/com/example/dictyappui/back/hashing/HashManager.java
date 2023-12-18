package com.example.dictyappui.back.hashing;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashManager {
    private final int tableSize;

    public HashManager(int tableSize) {
        this.tableSize = tableSize;
    }

    public int primaryHashing(String word) {
        int intHash = 0;
        for (int i = 0; i < word.length(); i++) {
            intHash = (intHash << 5) - intHash + word.charAt(i);
//            if (intHash < 0) {
//                System.out.println("Overflow " + intHash);
//            }
        }
        return Math.abs(intHash) % tableSize;
//        Old hashing algo
    }

    public int hashMultiplicative(String key) {
        int hash = 5381; // Staring value
        final int m = 33;

        for (int i = 0; i < key.length(); ++i) {
            hash = m * hash + key.charAt(i);
        }

        return Math.abs(hash) % tableSize;
    }

    public int googleHash(String str) {
        int hashValue = Hashing.murmur3_32().hashString(str, StandardCharsets.US_ASCII).asInt();
        return Math.abs((int) hashValue) % tableSize;
    }


    public int primaryHashingWithPow(String word) {
        int intHash = 0;
        for (int i = 0; i < word.length(); i++) {
            intHash = (int)Math.pow((intHash << 5) - intHash + word.charAt(i), i);
        }
        return Math.abs(intHash) % tableSize;
//        Old hashing algo
    }

    public int primaryHashingJavaHash(String word) {
        int hashCode = Math.abs(word.hashCode());
        return hashCode % tableSize;
    }

    public int secondaryHashingNN1(int intHash) {
        return 1 + intHash % (tableSize - 1);
    }

    public int secondaryHashingLinear(int intHash) {
        return 1;
    }

    public int doubleHashing(String word, int step) {
        int primaryHashingFunctionRes = googleHash(word);
//        int primaryHashingFunctionRes = primaryHashingJavaHash(word);
        int secondaryHashingFunctionRes = secondaryHashingNN1(primaryHashingFunctionRes);
//        int secondaryHashingFunctionRes =  secondaryHashingLinear(primaryHashingFunctionRes);
        // Double hashing formula
        return (primaryHashingFunctionRes + step * secondaryHashingFunctionRes) % tableSize;
    }

    public int jenkinsHash(String str) {
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash += str.charAt(i);
            hash += (hash << 10);
            hash ^= (hash >>> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >>> 11);
        hash += (hash << 15);
        return Math.abs(hash) % tableSize;
    }

    private String calcHashSHA(String word) {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hash = messageDigest.digest(word.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder hexHash = new StringBuilder();
            for (byte byteFromArray : hash) {
                // This will leave 2 digits for each byte and fill them with 0 if needed
                hexHash.append(String.format("%02x", byteFromArray));
            }
            // Print the original word and its hash code
//            System.out.println("Word: " + word);
//            System.out.println("Hashed code: " + hexHash.toString());

            // Return the hash code as a string
            return hexHash.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("DeveloperExceptionCaption: JVM wasn't able to find this string hashing algo", e);
        }
    }

}
