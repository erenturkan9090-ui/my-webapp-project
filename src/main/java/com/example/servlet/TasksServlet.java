package com.example.servlet;

import com.example.dao.TaskDAO;
import com.example.model.Task;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/tasks/*") 
public class TasksServlet extends HttpServlet {
    private final transient TaskDAO taskDAO = new TaskDAO(); 
    private final transient Gson gson = new Gson(); 
    private static final String APP_JSON = "application/json"; 

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException { 
        try {
            final List<Task> tasks = taskDAO.getAllTasks();
            resp.setContentType(APP_JSON);
            resp.getWriter().write(this.gson.toJson(tasks));
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException { 
        final String title = req.getParameter("title");
        if (title != null && !title.trim().isEmpty()) {
            try {
                taskDAO.addTask(title);
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws IOException { 
        final String pathInfo = req.getPathInfo();
        final String newTitle = req.getParameter("title");
        if (pathInfo != null && pathInfo.length() > 1 && newTitle != null) {
            try {
                final int id = Integer.parseInt(pathInfo.substring(1));
                taskDAO.updateTaskTitle(id, newTitle);
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException | NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        }
    }

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws IOException { 
        final String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                final int id = Integer.parseInt(pathInfo.substring(1));
                taskDAO.deleteTask(id);
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (SQLException | NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        }
    }
}