package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

public class FileOutputStrategy implements OutputStrategy {

    // FIX: Field names must be lowerCamelCase
    // FIX: baseDirectory should be final since it is set in the constructor and should not change afterwards
    private final String baseDirectory;

    // FIX: Field names must be lowerCamelCase
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        // FIX: Local variable names must be lowerCamelCase
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        // Line wrapping must follow 100‑char limit and indentation rules
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(
                    Paths.get(filePath),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND))) {
            // FIX: Format string should be readable and follow spacing conventions 
            out.printf(
                "Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n",
                patientId, timestamp, label, data
            );

        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}