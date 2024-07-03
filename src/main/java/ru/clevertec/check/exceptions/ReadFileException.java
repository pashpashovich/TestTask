package main.java.ru.clevertec.check.exceptions;

public class ReadFileException extends AbstractException {

    public ReadFileException(String message) {
        super(message);
        writeErrorToCSV(getExceptionStatus().name());
    }

    @Override
    public String toString() {
        return "ReadFileException: " + getMessage();
    }

    @Override
    public ExceptionStatus getExceptionStatus() {
        return ExceptionStatus.INTERNAL_SERVER_ERROR;
    }
}
