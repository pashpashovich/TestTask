package main.java.ru.clevertec.check.exceptions;

public class InsufficientStockException extends AbstractException {
    public InsufficientStockException(String message) {
        super(message);
        writeErrorToCSV(getExceptionStatus().name());
    }

    @Override
    public String toString() {
        return "InsufficientStockException: " + getMessage();
    }

    @Override
    public ExceptionStatus getExceptionStatus() {
        return ExceptionStatus.BAD_REQUEST;
    }
}
