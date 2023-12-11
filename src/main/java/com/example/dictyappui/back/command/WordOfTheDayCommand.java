package com.example.dictyappui.back.command;

import com.example.dictyappui.back.db.DatabaseConnection;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Random;

public class WordOfTheDayCommand implements Command {
    @Override
    public void execute(DatabaseConnection dbConn) {
        // Get the display name of the day
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        // Get the day of the week as a string
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        String dayOfWeekString = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        System.out.println("Wonderful word of this " + dayOfWeekString + ": ");
        int wordOfTheDayIndex = randomize(currentDate, dbConn);
        System.out.println(dbConn.selectFromWordsById(wordOfTheDayIndex));
    }

    @Override
    public String gui(DatabaseConnection dbConn, String stringWithNoting) {
        // Get the display name of the day
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        // Get the day of the week as a string
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        String dayOfWeekString = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        StringBuilder b = new StringBuilder();
        b.append("Wonderful word of this ").append(dayOfWeekString).append(": ");
        int wordOfTheDayIndex = randomize(currentDate, dbConn);
        b.append(dbConn.selectFromWordsById(wordOfTheDayIndex));
        return b.toString();
    }

    private int randomize(LocalDate currentDate, DatabaseConnection dbConn) {
        // Get the current day of the month
        int dayOfYear = currentDate.getDayOfYear();
        // Get the year
        int year = currentDate.getYear();

        // Create a random number generator with the day of the month as the seed
        Random random = new Random((long) dayOfYear * year);

        // Generate a random number
        int randomNumber = random.nextInt(dbConn.getDbSize()); // Adjust the bound as needed
        while (dbConn.checkIfRowIsEmptyInWordsTable(randomNumber)) {
            randomNumber++;
            // To maintain acceptable index
            randomNumber = randomNumber % (dbConn.getDbSize());
        }
        return randomNumber;
    }
}
