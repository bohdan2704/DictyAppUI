package com.example.dictyappui.back.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {
    private final int tableSize;

    public Hashing(int tableSize) {
        this.tableSize = tableSize;
    }

    public int primaryHashing(String word) {
        int intHash = 0;
        for (int i = 0; i < word.length(); i++) {
            intHash = (intHash << 5) - intHash + word.charAt(i);
        }
        return Math.abs(intHash) % tableSize;
//        Old hashing algo
//
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
        int primaryHashingFunctionRes = primaryHashing(word);
//        int primaryHashingFunctionRes = primaryHashingJavaHash(word);
        int secondaryHashingFunctionRes = secondaryHashingNN1(primaryHashingFunctionRes);
//        int secondaryHashingFunctionRes =  secondaryHashingLinear(primaryHashingFunctionRes);
        // Double hashing formula
        return (primaryHashingFunctionRes + step * secondaryHashingFunctionRes) % tableSize;
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
