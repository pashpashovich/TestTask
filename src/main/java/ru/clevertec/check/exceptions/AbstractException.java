package main.java.ru.clevertec.check.exceptions;

import java.io.FileWriter;
import java.io.IOException;

public abstract class AbstractException extends Exception {
    public AbstractException(String message) {
        super(message);
    }

    public abstract ExceptionStatus getExceptionStatus();


    public void writeErrorToCSV(String errorMessage,String path) {
        String message = "ERROR\n" + errorMessage + "\n";
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(message);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

}
