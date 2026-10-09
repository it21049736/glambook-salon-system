package com.glambook.servlet.common;

import com.glambook.util.DataPath;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Runs once when Tomcat starts the application.
 * It decides which folder holds the data files and copies the sample
 * data (packaged in WEB-INF/data) into that folder if a file is missing.
 */
@WebListener
public class AppStartupListener implements ServletContextListener {

    // every data file used by the system
    private static final String[] DATA_FILES = {
            "users.txt", "services.txt", "stylists.txt",
            "appointments.txt", "payments.txt", "reviews.txt"
    };

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        // 1. JVM option has first priority, 2. web.xml context-param, 3. default folder
        String dir = System.getProperty(DataPath.PROPERTY_NAME);
        if (dir == null || dir.trim().isEmpty()) {
            dir = context.getInitParameter(DataPath.PROPERTY_NAME);
        }
        DataPath.setDataDir(dir);

        File folder = new File(DataPath.getDataDir());
        if (!folder.exists()) {
            folder.mkdirs();
        }

        for (String fileName : DATA_FILES) {
            copySampleFile(context, fileName);
        }
        System.out.println("GlamBook data folder: " + folder.getAbsolutePath());
    }

    // copies a sample file only when it does not exist yet, so real data is never overwritten
    private void copySampleFile(ServletContext context, String fileName) {
        File target = DataPath.getFile(fileName);
        if (target.exists()) {
            return;
        }
        try (InputStream in = context.getResourceAsStream("/WEB-INF/data/" + fileName)) {
            if (in != null) {
                Files.copy(in, target.toPath());
            }
        } catch (IOException e) {
            System.out.println("Could not copy sample file " + fileName + ": " + e.getMessage());
        }
    }
}
