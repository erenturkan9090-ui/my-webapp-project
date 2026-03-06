package com.example;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseShutdownListener implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(DatabaseShutdownListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // no-op
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
                try {
                    DriverManager.deregisterDriver(driver);
                    LOGGER.log(Level.INFO, "Deregistered JDBC driver: {0}", driver);
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error deregistering driver: {0}", e.toString());
                }
            }
        }
    }
}
