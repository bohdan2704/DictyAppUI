package com.example.dictyappui.back.api;

import com.example.dictyappui.back.db.DatabaseConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SentencesFetcher {
    private static final String basicUrl = "https://www.merriam-webster.com/dictionary/%s";
    private final DatabaseConnection dbConn;

    public SentencesFetcher(DatabaseConnection dbConn) {
        this.dbConn = dbConn;
    }

    //
    public static String getSentenceAndExplanation(Element partOfSpeech) {
        StringBuilder concatenatedValue = new StringBuilder();

        // Select the definition elements within the provided div
        Elements definitions = partOfSpeech.select("div.vg-sseq-entry-item");

        // Loop through the definitions
        for (Element definition : definitions) {
            // Get the label and text of the definition
            Element labelElement = definition.selectFirst("div.vg-sseq-entry-item-label");
            Element textElement = definition.selectFirst("span.dtText");

            if (labelElement == null || textElement == null) {
                continue;
            }

            // Extract label and text
            String labelText = labelElement.text();
            String text = textElement.text();

            // Print the label and text
            concatenatedValue.append("Definition ").append(labelText).append(" ").append(text).append(System.lineSeparator());
            // Select and print the corresponding sentences
            Elements sentences = definition.select("div.sub-content-thread span.ex-sent");

            // Loop through the sentences
            for (Element sentence : sentences) {
                // Print the sentence text
                String textFromDiv = sentence.text();
                if (textFromDiv.charAt(0) != '—') {
                    concatenatedValue.append(" -- ").append(textFromDiv).append(System.lineSeparator());
                }
            }
        }
        return concatenatedValue.toString();
    }

    private static String getWordType(Element partOfSpeech) {
        Element partsOfSpeechElement = partOfSpeech.selectFirst("h2.parts-of-speech");

        // Extract the word type
        String wordType = partsOfSpeechElement.text();
        wordType = wordType.split(" ")[0].toUpperCase();
        return wordType;
    }

    public String parseAllSentenceAndExplanation(String word) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String finalUrl = String.format(basicUrl, word);
            List<String> list = new ArrayList<>();
            Document document = Jsoup.connect(finalUrl).get();

            // Select all div elements with the class name "entry-word-section-container"
            Elements divElements = document.select("div.entry-word-section-container");

            // Loop through the selected elements
            for (Element div : divElements) {
                // Do something with each div element
                String sentenceAndExplanation = getSentenceAndExplanation(div);
                if (!sentenceAndExplanation.isBlank())
                    stringBuilder.append(getWordType(div))
                            .append(System.lineSeparator())
                            .append(sentenceAndExplanation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while connecting", e);
        }
        return stringBuilder.toString();
    }

    public void fillWordsWithSentencesTable() {
        List<String> enWords = new ArrayList<>();
        List<String> ukWords = new ArrayList<>();

        dbConn.selectFromWordsForHashTable(enWords, ukWords, null);

        for (int i = 0; i < enWords.size(); i++) {
            try {
                String sentence = parseAllSentenceAndExplanation(enWords.get(i));
                if (sentence != null) {
                    dbConn.insertToWordsForHashTable(enWords.get(i), ukWords.get(i), sentence);
//                System.out.println(sentence);
                }
            } catch (RuntimeException e) {
                // This word is absent in dictionary
                System.out.println(enWords.get(i));
            }
        }
    }
}
