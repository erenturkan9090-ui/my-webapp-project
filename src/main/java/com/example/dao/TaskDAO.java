package com.example.dao;

import com.example.model.Task;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class TaskDAO {
    private static final String DB_URL = "jdbc:sqlite:todo.db";

   
    public List<Task> getAllTasks() throws SQLException {
        final List<Task> tasks = new ArrayList<>();
        final String sql = "SELECT id, title, completed FROM tasks";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(new Task(
                    rs.getInt("id"), 
                    rs.getString("title"), 
                    rs.getBoolean("completed")
                ));
            }
        }
        return tasks;
    }

    
    public void addTask(final String title) throws SQLException {
        final String sql = "INSERT INTO tasks (title, completed) VALUES (?, 0)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        }
    }

    
    public void updateTaskTitle(final int id, final String newTitle) throws SQLException {
        final String sql = "UPDATE tasks SET title = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newTitle);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        }
    }

    
    public void deleteTask(final int id) throws SQLException {
        final String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}