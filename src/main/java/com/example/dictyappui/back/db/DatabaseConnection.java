package com.example.dictyappui.back.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String jdbcUrl = "jdbc:sqlserver://localhost:1434;databaseName=dictionaryDb;integratedSecurity=true;" +
            "encrypt=true;trustServerCertificate=true";
    private static final String username = "loginTest";
    private static final String password = "qwerty";
    private final int dbSize;
    private final Connection conn;

    public DatabaseConnection(int dbSize) {
        this.dbSize = dbSize;
        conn = establishConnection();
    }

    public void insertToWords(int index, String word, String translation, String explanationAndSentence) {
        String query = "INSERT INTO dbo.Words (WordId, Word, Translation, ExplanationAndSentence) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, index);
            preparedStatement.setString(2, word);
            preparedStatement.setString(3, translation);
            preparedStatement.setString(4, explanationAndSentence);

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted <= 0) {
                throw new SQLException("Error while inserting the rows");
            }
        } catch (SQLException e) {
            throw new RuntimeException("DeveloperExceptionCaption: Exception while executing the query", e);
        }
    }

    public void selectFromWordsForHashTable(List<String> enWords, List<String> ukWords, List<String> explanationAndSentences) {
        String query = "SELECT [Word], [Translation], [ExplanationAndSentence] FROM dbo.WordsForHashTable";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String word = resultSet.getString("Word");
                String translation = resultSet.getString("Translation");
                String explanationAndSentence = resultSet.getString("ExplanationAndSentence");

                enWords.add(word);
                if (ukWords != null)
                    ukWords.add(translation);
                if (explanationAndSentences != null)
                    explanationAndSentences.add(explanationAndSentence);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DeveloperExceptionCaption: Exception while executing the query", e);
        }
    }

    public List<Word> selectFromWords() {
        List<Word> words = new ArrayList<>();
        String query = "SELECT [Word], [Translation], [ExplanationAndSentence] FROM dbo.Words";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String word = resultSet.getString("Word");
                String translation = resultSet.getString("Translation");
                String explanationAndSentence = resultSet.getString("ExplanationAndSentence");

                // We can omit some details
                words.add(new Word(word, translation, explanationAndSentence));
            }

        } catch (SQLException e) {
            throw new RuntimeException("DeveloperExceptionCaption: Exception while executing the query", e);
        }

        return words;
    }

    public List<Word> selectWordsStartingWithLetter(char letter) {
        List<Word> words = new ArrayList<>();
        String query = "SELECT [WordId], [Word], [Translation] FROM dbo.Words WHERE [Word] LIKE '%c%%'";
        query = String.format(query, letter);

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String word = resultSet.getString("Word");
                String translation = resultSet.getString("Translation");
                // We can omit some details
                words.add(new Word(word, translation, null));
            }

        } catch (SQLException e) {
            throw new RuntimeException("DeveloperExceptionCaption: Exception while executing the query", e);
        }

        return words;
    }


    // CHECK IF THIS RETURN SOME VALUABLE INFORMATION, RETURN ARRAY OF THREE COMPONENTS IF FOUND, NULL IF NOT
    public Word selectFromWordsById(int id) {
        String query = "SELECT [Word], [Translation], [ExplanationAndSentence] FROM dbo.Words WHERE WordId = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            // Execute this query
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Data found, retrieve and process it
                String word = resultSet.getString("Word");
                String translation = resultSet.getString("Translation");
                String explanationAndSentence = resultSet.getString("ExplanationAndSentence");

                return new Word(word, translation, explanationAndSentence);

            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("DeveloperExceptionCaption: Exception while executing the query", e);
        }
    }

    public boolean checkIfRowIsEmptyInWordsTable(int id) {
        String query = "SELECT [Word] FROM dbo.Words WHERE WordId = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            // Execute this query
            ResultSet resultSet = preparedStatement.executeQuery();
            // Check if there is some data by thi wordId
            return !resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("DeveloperExceptionCaption: Exception while executing the query", e);
        }
    }

    public void insertToWordsForHashTable(String word, String translation, String text) {
        String query = "INSERT INTO dbo.WordsForHashTable (Word, Translation, ExplanationAndSentence) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, word);
            preparedStatement.setString(2, translation);
            preparedStatement.setString(3, text);

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted <= 0) {
                throw new SQLException("Error while inserting the rows");
            }
        } catch (SQLException e) {
            throw new RuntimeException("DeveloperExceptionCaption: Exception while executing the query", e);
        }
    }

    private Connection establishConnection() {
        try {
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("DeveloperExceptionCaption: Cannot connect to database", e);
        }
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("DeveloperExceptionCaption: Error while closing connection before destroying the DB object", e);
            }
        }
    }

    public int getDbSize() {
        return dbSize;
    }
}
