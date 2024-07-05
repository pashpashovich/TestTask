package ru.clevertec.check.exceptions;

public class InsufficientStockException extends AbstractException {
    public InsufficientStockException(String message,String path) {
        super(message);
        writeErrorToCSV(getExceptionStatus().name(),path);
    }

    @Override
    public String toString() {
        return "InsufficientStockException: " + getMessage()+" Статус: "+getExceptionStatus();
    }

    @Override
    public ExceptionStatus getExceptionStatus() {
        return ExceptionStatus.BAD_REQUEST;
    }
}
