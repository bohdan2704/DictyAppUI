package com.example.dictyappui.back.command;

import com.example.dictyappui.back.db.DatabaseConnection;

public interface Command {
    void execute(DatabaseConnection dbConn);
    String gui(DatabaseConnection dbConn, String wordToTranslate);
}
