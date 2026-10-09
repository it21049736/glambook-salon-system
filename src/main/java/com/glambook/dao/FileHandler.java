package com.glambook.dao;

import com.glambook.util.DataPath;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Shared helper used by every DAO to read and write the .txt data files.
 * Each record is one line and the fields are separated with the | character.
 * Methods are synchronized so two requests cannot write the same file at once.
 */
public class FileHandler {

    private FileHandler() {
    }

    // reads every non-empty line of a file; a missing file gives an empty list instead of an error
    public static synchronized List<String> readLines(String fileName) {
        List<String> lines = new ArrayList<>();
        File file = DataPath.getFile(fileName);
        if (!file.exists()) {
            return lines;
        }
        // try-with-resources closes the reader automatically
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName + ": " + e.getMessage());
        }
        return lines;
    }

    // overwrites the whole file with the given lines (used after update and delete)
    public static synchronized boolean writeLines(String fileName, List<String> lines) {
        File file = DataPath.getFile(fileName);
        file.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error writing " + fileName + ": " + e.getMessage());
            return false;
        }
    }

    // adds one new line to the end of the file (used when creating a record)
    public static synchronized boolean appendLine(String fileName, String line) {
        File file = DataPath.getFile(fileName);
        file.getParentFile().mkdirs();
        // "true" means append mode, so old lines are kept
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, true))) {
            writer.write(line);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error appending to " + fileName + ": " + e.getMessage());
            return false;
        }
    }
}
