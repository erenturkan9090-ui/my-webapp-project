package com.example;

import com.example.dao.TaskDAO;

public class DatabaseInitializer {
    public static void main(String[] args) {
        System.out.println("Initializing database...");
        new TaskDAO(); // constructor will create the table
        System.out.println("Database initialized (todo.db with tasks table).\n");
    }
}
