package com.example.dictyappui.back.db;

public record Word(String enWord, String ukWord, String explanationAndSentence) {
    public String getWords() {
        return String.format("%s -- %s", enWord, ukWord);
    }

    @Override
    public String toString() {
        return String.format("%s -- %s\nExplanation: %s", enWord, ukWord, explanationAndSentence);
    }
}
