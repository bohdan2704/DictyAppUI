package com.example.dictyappui.back.redundant;

public class Timer {

    private long startTime;

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        long endTime = System.nanoTime();
        long elapsedTimeInNanos = endTime - startTime;

        // Convert nanoseconds to milliseconds for display
        double elapsedTimeInMilliseconds = (double) elapsedTimeInNanos / 1_000_000.0;

        System.out.println("Execution time: " + elapsedTimeInMilliseconds + " ms");
    }
}
