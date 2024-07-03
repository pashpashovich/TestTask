package main.java.ru.clevertec.check.exceptions;

public class NoProductException extends AbstractException {
    public NoProductException(String message) {
        super(message);
        writeErrorToCSV(getExceptionStatus().name());
    }

    @Override
    public ExceptionStatus getExceptionStatus() {
        return ExceptionStatus.BAD_REQUEST;
    }

    @Override
    public String toString() {
        return "NoProductException: " + getMessage();
    }
}
